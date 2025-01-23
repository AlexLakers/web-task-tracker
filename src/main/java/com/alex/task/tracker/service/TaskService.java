package com.alex.task.tracker.service;

import com.alex.task.tracker.dto.TaskCreationDto;
import com.alex.task.tracker.dto.TaskFilterDto;
import com.alex.task.tracker.dto.TaskResponseDto;
import com.alex.task.tracker.dto.TaskUpdatingDto;
import com.alex.task.tracker.entity.PermissionType;
import com.alex.task.tracker.entity.Task;
import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.entity.enums.TaskStatus;
import com.alex.task.tracker.exception.AccountNotExistException;
import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.exception.access.UnsupportedCreateOperationException;
import com.alex.task.tracker.exception.access.UnsupportedDeleteOperationException;
import com.alex.task.tracker.exception.access.UnsupportedUpdateOperationException;
import com.alex.task.tracker.exception.task.TaskCreationException;
import com.alex.task.tracker.exception.task.TaskDeletingException;
import com.alex.task.tracker.exception.task.TaskUpdatingException;
import com.alex.task.tracker.exception.task.TasksFilteringException;
import com.alex.task.tracker.mapper.TaskMapper;
import com.alex.task.tracker.repository.AccountRepository;
import com.alex.task.tracker.repository.TaskRepository;
import com.alex.task.tracker.util.DateTimeHelper;
import com.alex.task.tracker.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is a part of a service layer of this app.
 * It consists of  methods which represent the business logic part about {@link Task entity}.
 * This is a single tone class.
 */

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskService {
    private final static TaskService INSTANCE = new TaskService(TaskRepository.getInstance(),
            AccountService.getInstance(),
            AccountRepository.getInstance(),
            TaskMapper.getInstance(),
            PermissionService.getInstance()
    );

    public static TaskService getInstance() {
        return INSTANCE;
    }

    private final TaskRepository taskRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final TaskMapper taskMapper;
    private final PermissionService permissionService;

    /**
     * Returns list of available tasks as a{@link TaskResponseDto dto}
     * by {@link TaskFilterDto filter} with all the necessary params.
     * It allows some user of this app to find tasks using {@link TaskRepository}
     * by any filter params.
     * The filter params can be entered using form on the page of logged account.
     *
     * @param taskFilterDto
     * @return list of output dto if some tasks exist.
     * Else-empty list.
     */

    public List<TaskResponseDto> findAllBy(TaskFilterDto taskFilterDto) {
        log.debug("The the method of getting all tasks was started with filter: {}", taskFilterDto);
        boolean transactionStartedIsHere = false;
        List<TaskResponseDto> tasks = null;
        try {
            transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

            tasks = taskRepository.findAll(taskFilterDto)
                    .stream()
                    .map(taskMapper::toDto)
                    .collect(Collectors.toList());

            HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);

            log.debug("The result of getting all tasks: {}", tasks);
            return tasks;

        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new TasksFilteringException(e);
        }
    }

    /**
     * Returns id of created task by input {@link TaskCreationDto dto}.
     * In this app input dto contains all the entered params using the 'tasks' page of logged account.
     * Firstly, occurs the validation process of transmitted dto using 'HibernateValidator'.
     * If input dto is valid then occurs getting the logged account by id
     * using {@link AccountService accountService} and check his permissions
     * using {@link PermissionService service}. If the current account does not have necessary permissions
     * then throw {@link UnsupportedCreateOperationException exception}.
     * After the checking rights step beginning mapping process using {@link TaskMapper mapper}
     * from input {@link TaskCreationDto dto} to {@link Task task-entity}.
     * After it performs the main functional of this method-creating
     * a new task for the current logged account and writing it to the database using {@link TaskRepository taskRepository}.
     * If something went wrong them current transaction will be rollback and throw new
     * {@link TaskCreationException exception}
     *
     * @param taskDto input task data - dto.
     * @return if task has been created successfully then return not null task id.
     */

    public Long create(TaskCreationDto taskDto) {
        //validation to another method move ,because it repeat.To check it
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();
        var violations = validator.validate(taskDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        log.debug("The validation process for input data: {} is successful", taskDto);

        boolean transactionStartedIsHere = false;

        Task savedTask = null;
        try {
            transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

            var loggedUserDto = accountService.findById(taskDto.loggedAccountId())
                    .orElseThrow(() -> new AccountNotExistException("The user with id=%1$d does not exist"
                            .formatted(taskDto.loggedAccountId())));

            if (permissionService.checkPermissionCreate(loggedUserDto)) {
                savedTask = taskRepository.save(taskMapper.toEntity(taskDto));

                HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);

                log.info("The task: {} has been created.", savedTask);
                return savedTask.getId();
            } else {
                throw new UnsupportedCreateOperationException(
                        "The operation [%s] is not allowed for this logged account"
                                .formatted(PermissionType.READ_WRITE.name()));
            }

        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new TaskCreationException(e);
        }


    }

    /**
     * Returns output {@link TaskResponseDto dto} wrapped in {@link Optional optional}
     * with the main data of founded {@link Task task} by id.
     * We use null safe opportunities of Optional class.
     * Firstly, occurs task search using {@link TaskRepository repository} by transmitted id.
     * If the task has been found then occurs mapping process using {@link TaskMapper mapper}
     * from {@link Task task} to {@link TaskResponseDto dto}.
     * If something went wrong then current transaction will be rollback
     * and throw new {@link com.alex.task.tracker.exception.ServiceException exception}.
     *
     * @param id task id.
     * @return Not empty  output dto wrapped in optional if task exists.
     * Else-an empty optional output dto.
     */

    public Optional<TaskResponseDto> findById(Long id) {
        log.debug("The  method of getting task with id: {} was started", id);
        boolean transactionStartedIsHere = false;

        Optional<TaskResponseDto> maybeFoundTaskDto = Optional.empty();
        try {
            transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

            maybeFoundTaskDto = taskRepository.findById(id)
                    .map(taskMapper::toDto);

            HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);

            log.debug("The returned task dto: {}", maybeFoundTaskDto);
            return maybeFoundTaskDto;

        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new ServiceException(e);
        }
    }

    /**
     * Returns boolean result of deleting process a specific
     * {@link Task task} by transmitted id.
     * Firstly, occurs getting the logged account by id
     * using {@link AccountService accountRepository} and check his permissions
     * using {@link PermissionService service}.
     * If the current account does not have necessary permissions
     * then throw {@link UnsupportedDeleteOperationException exception}.
     * If the current account has all the necessary permissions
     * we should try to find it by id before the deleting process.
     * And then if task has been found -
     * occurs deleting process using {@link TaskRepository repository}.
     * If something went wrong them current transaction will be rollback and throw new
     * {@link TaskDeletingException exception}
     *
     * @param id task id.
     * @return true if the deleting process has been successfully,
     * else-false.
     */

    public boolean delete(Long id, Long loggedUserId) {
        log.debug("The removing task with id: {} was started", id);
        boolean transactionStartedIsHere = false;
        Optional<Task> maybeFoundTask = Optional.empty();
        try {
            transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

            var loggedUserDto = accountService.findById(loggedUserId).
                    orElseThrow(
                            () -> new AccountNotExistException(
                                    "The user with id=%1$d does not exist"
                                            .formatted(loggedUserId)));
            if (permissionService.checkPermissionDelete(loggedUserDto)) {

                maybeFoundTask = taskRepository.findById(id);
                maybeFoundTask.ifPresent(
                        taskRepository::delete
                );
                HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);
                log.debug("The founded task for removing: {}", maybeFoundTask);

                return maybeFoundTask.isPresent();
            } else {
                throw new UnsupportedDeleteOperationException(
                        "The operation [%s] is not allowed for this logged account"
                                .formatted(PermissionType.DELETE.name()));
            }
        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new TaskDeletingException(e);
        }

    }

    /**
     * Returns boolean result of updating process a specific
     * {@link Task task} by transmitted {@link TaskUpdatingDto dto}
     * with  updating data for the current task.
     * In this app the input params(dto) entered on the 'tasks' page
     * of logged account.
     * Firstly, occurs getting the logged account by id
     * using {@link AccountService accountRepository} and check his permissions
     * using {@link PermissionService service}.
     * If the current account does not have necessary permissions
     * then throw {@link UnsupportedUpdateOperationException exception}.
     * If the current account has all the necessary permissions
     * we should try to find it by id before the deleting process.
     * And then if task has been found -
     * occurs updating process using {@link TaskRepository repository}.
     * If something went wrong them current transaction will be rollback and throw new
     * {@link TaskDeletingException exception}
     *
     * @param dto input task data-dto.
     * @return true if the updating process has been successfully,
     * else-false.
     */


    public boolean update(TaskUpdatingDto dto) {
        boolean transactionStartedIsHere = false;
        Optional<Task> maybeFoundTask = Optional.empty();

        try {
            transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

            var loggedUserDto = accountService.findById(dto.loggedAccountId()).
                    orElseThrow(
                            () -> new AccountNotExistException(
                                    "The user with id=%1$d does not exist"
                                            .formatted(dto.loggedAccountId())));
            if (permissionService.checkPermissionUpdate(loggedUserDto)) {
                maybeFoundTask = taskRepository.findById(dto.id());
                maybeFoundTask.ifPresent(
                        foundTask -> {
                            var updatedTask = updateTaskByUpdateTaskDto(foundTask, dto);
                            taskRepository.update(updatedTask);
                        }
                );
                HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);

                log.debug("The founded task for updating: {}", maybeFoundTask);
                return maybeFoundTask.isPresent();
            } else {
                throw new UnsupportedUpdateOperationException(
                        "The operation [%s] is not allowed for this logged account"
                                .formatted(PermissionType.UPDATE.name()));
            }
        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new TaskUpdatingException(e);
        }
    }

    private Task updateTaskByUpdateTaskDto(Task task, TaskUpdatingDto dto) {
        System.out.println(task.getPerformer());
        System.out.println(dto.performerId());
        task.setPerformer(dto.performerId() == null
                ? task.getPerformer()
                : accountRepository.findById(dto.performerId()).orElse(null));
        var taskInfo = task.getTaskInfo();

        taskInfo.setDescription(dto.description().isEmpty()
                ? taskInfo.getDescription()
                : dto.description());
        taskInfo.setDeadlineDate(dto.deadLineDate().isEmpty()
                ? taskInfo.getDeadlineDate()
                : DateTimeHelper.parseDate(dto.deadLineDate()));
        task.setStatus(dto.status() == null
                ? task.getStatus()
                : TaskStatus.valueOf(dto.status()));
        task.setPriority(dto.priority() == null
                ? task.getPriority()
                : TaskPriority.valueOf(dto.priority()));
        return task;
    }

}

