package com.alex.task.tracker.service;



import com.alex.task.tracker.dto.*;
import com.alex.task.tracker.entity.*;
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
import com.alex.task.tracker.util.HibernateUtil;
import lombok.Cleanup;
import org.assertj.core.api.Assertions;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private PermissionService permissionService;

    private static Task task;
    private static Account account;
    private static AccountResponseDto accountResponseDto;
    private static final Long ID = 1L;

    @BeforeAll
    static void setInit() {
        accountResponseDto=AccountResponseDto.builder().fullName("Alex Alexov").id(ID).build();
        account=Account.builder()
                .id(1L)
                .accountInfo(AccountInfo.builder().firstName("alex").lastName("Alexov").build())
                .birthday(LocalDate.parse("1993-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .login("Alex")
                .build();

        task=Task.builder().performer(account)
                .customer(Account.builder()
                        .id(1L)
                        .accountInfo(AccountInfo.builder().firstName("serg").lastName("Sergeev").build())
                        .birthday(LocalDate.parse("1993-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .login("Serg")
                        .build())
                .taskInfo(TaskInfo.builder().name("Task").description("DESSSCSCSCSC")
                        .creationDate(LocalDate.now()).deadlineDate(LocalDate.now().plusDays(1)).build())
                .priority(TaskPriority.MDM)
                .status(TaskStatus.PROGRESS)
                .build();
    }

    @InjectMocks
    private TaskService taskService;

    @Test
    void findAllBy_shouldReturnNotEmptyListTaskDto_whenTasksExistsByFilter() {
        var taskDto = TaskResponseDto.builder().build();
        var taskDto1 = TaskResponseDto.builder().build();
        var task1 = Task.builder().build();
        var tasks = Stream.of(task,task1).toList();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(taskRepository.findAll(any(TaskFilterDto.class))).thenReturn(tasks);
        when(taskMapper.toDto(task)).thenReturn(taskDto);
        when(taskMapper.toDto(task1)).thenReturn(taskDto1);

        var actual = taskService.findAllBy(TaskFilterDto.builder().build());

        assertThat(actual)
                .isNotNull().isNotEmpty()
                .hasSize(2)
                .contains(taskDto,taskDto1);
        verifyNoMoreInteractions(taskRepository, accountRepository, accountService, taskMapper);




    }
    @Test
    void findAllBy_shouldReturnEmptyListTaskDto_whenTasksNotExistsByFilter(){
        var taskDto = TaskResponseDto.builder().build();
        var taskDto1 = TaskResponseDto.builder().build();
        var task1 = Task.builder().build();
        var tasks = Stream.of(task,task1).toList();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(taskRepository.findAll(any(TaskFilterDto.class))).thenReturn(Collections.emptyList());

        var actual = taskService.findAllBy(TaskFilterDto.builder().build());

        assertThat(actual)
                .isNotNull().isEmpty();
        verifyNoMoreInteractions(taskRepository);
        verifyNoInteractions(taskMapper);
    }

    @Test
    void findAllBy_shouldThrowTaskFilteringException_whenAnyException() {
        var filter=TaskFilterDto.builder().build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);

        doThrow(IllegalArgumentException.class).when(taskRepository).findAll(filter);

        assertThatExceptionOfType(TasksFilteringException.class)
                .isThrownBy(()->taskService.findAllBy(filter))
                .withCause(new IllegalArgumentException());
        verifyNoMoreInteractions(taskRepository);
        verifyNoInteractions(taskMapper);
    }

    @Test
    void create_shouldReturnLongId_whenTaskCreationDtoIsValid() {
        var givingDto= TaskCreationDto.builder().name("task")
                .deadLineDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .priority(TaskPriority.MDM.name())
                .description("descriptionDescription")
                .performerId(ID)
                .loggedAccountId(ID)
                .build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(taskRepository.save(task)).thenReturn(task);
        when(accountService.findById(ID)).thenReturn(Optional.of(accountResponseDto));
        when(permissionService.checkPermissionCreate(accountResponseDto)).thenReturn(true);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toEntity(givingDto)).thenReturn(task);
        var expected=task.getId();

        var actual = taskService.create(givingDto);

        assertThat(actual).isEqualTo(expected);
        verifyNoMoreInteractions(taskRepository, taskRepository, accountService, taskMapper,permissionService);

    }

    @Test
    void create_shouldThrowAccountNotExistException_whenLoggedQAccountNotExists() {
        var givingDto= TaskCreationDto.builder().name("task")
                .deadLineDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .priority(TaskPriority.MDM.name())
                .description("descriptionDescription")
                .performerId(ID)
                .loggedAccountId(ID)
                .build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.empty());

        //   var actual = taskService.create(givingDto);

        assertThatExceptionOfType(TaskCreationException.class)
                .isThrownBy(()->taskService.create(givingDto))
                .withCause(new AccountNotExistException("The user with id=%1$d does not exist".formatted(ID)));
        verifyNoMoreInteractions(accountService);
        verifyNoInteractions(permissionService,taskRepository,taskMapper);
    }

    @Test
    void create_shouldThrowUnsupportedCreateOperationException_whenLoggedQAccountNotAcceptPermit() {
        var givingDto= TaskCreationDto.builder().name("task")
                .deadLineDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .priority(TaskPriority.MDM.name())
                .description("descriptionDescription")
                .performerId(ID)
                .loggedAccountId(ID)
                .build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.of(accountResponseDto));
        when(permissionService.checkPermissionCreate(accountResponseDto)).thenReturn(false);

        assertThatExceptionOfType(TaskCreationException.class)
                .isThrownBy(()->taskService.create(givingDto))
                .withCause(new UnsupportedCreateOperationException(
                        "The operation [%s] is not allowed for this logged account"
                                .formatted(PermissionType.READ_WRITE.name())));
        verifyNoMoreInteractions(accountService,permissionService);
        verifyNoInteractions(taskRepository,taskMapper);
    }
    @Test
    void findById_shouldReturnNotEmptyOptionalTaskResponseDto_whenTaskIdExists(){
        var expectedDto= Optional.of(TaskResponseDto.builder().build());
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(expectedDto.get());

        var actual = taskService.findById(ID);

        assertThat(actual).isEqualTo(expectedDto);
        verifyNoMoreInteractions(accountService,taskMapper);
    }
    @Test
    void findById_shouldReturnEmptyOptional_whenTaskIdNotExists() {
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(taskRepository.findById(ID)).thenReturn(Optional.empty());

        var actual= taskService.findById(ID);

        assertThat(actual).isEmpty();
        verifyNoMoreInteractions(taskMapper);
    }

    @Test
    void findById_shouldThrowServiceException_whenAnyException(){
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        doThrow(IllegalArgumentException.class).when(taskRepository).findById(ID);

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(()->taskService.findById(ID))
                .withCause(new IllegalArgumentException());

        verifyNoMoreInteractions(taskMapper);
    }

    @Test
    void delete_shouldReturnTrue_whenTaskIdExists() {
        /*var expectedDto= TaskResponseDto.builder().build();*/
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.of(accountResponseDto));
        when(permissionService.checkPermissionDelete(accountResponseDto)).thenReturn(true);
        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        var actual=taskService.delete(ID,ID);

        assertThat(actual).isTrue();
        verifyNoMoreInteractions(accountService,permissionService,taskRepository);

    }
    @Test
    void delete_shouldReturnFalse_whenTaskIdNotExists() {
        /*var expectedDto= TaskResponseDto.builder().build();*/
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.of(accountResponseDto));
        when(permissionService.checkPermissionDelete(accountResponseDto)).thenReturn(true);
        when(taskRepository.findById(ID)).thenReturn(Optional.empty());
        /*doNothing().when(taskRepository).delete(task);*/

        var actual=taskService.delete(ID,ID);

        assertThat(actual).isFalse();
        verifyNoMoreInteractions(accountService,permissionService,taskRepository);

    }

    @Test
    void delete_shouldThrowTaskDeletingException_whenAccountNotExistException(){
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(TaskDeletingException.class)
                .isThrownBy(()->taskService.delete(ID,ID))
                .withCause(new AccountNotExistException(
                        "The user with id=%1$d does not exist"
                                .formatted(ID)));
        verifyNoMoreInteractions(taskMapper,permissionService,taskRepository);
    }

    @Test
    void delete_shouldThrowTaskDeletingException_whenUnsupportedDeleteOperationException(){
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.of(accountResponseDto));
        when(permissionService.checkPermissionDelete(accountResponseDto)).thenReturn(false);

        assertThatExceptionOfType(TaskDeletingException.class)
                .isThrownBy(()->taskService.delete(ID,ID))
                .withCause(new UnsupportedDeleteOperationException(
                        "The operation [%s] is not allowed for this logged account"
                                .formatted(PermissionType.DELETE.name())));
        verifyNoMoreInteractions(taskMapper,taskRepository);
    }

    @Test
    void update_shouldReturnTrue_whenTaskIdExists() {
        var givingDto= TaskUpdatingDto.builder().id(ID)
                .performerId(ID).loggedAccountId(ID).description("descccccccccccccccccccccc")
                .deadLineDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .priority("HGH").status("PERFORMED").build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.of(accountResponseDto));
        when(accountRepository.findById(ID)).thenReturn(Optional.of(account));
        when(permissionService.checkPermissionUpdate(accountResponseDto)).thenReturn(true);
        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).update(task);

        var actual=taskService.update(givingDto);

        assertThat(actual).isTrue();
        verifyNoMoreInteractions(accountService,accountRepository,permissionService,taskRepository);
    }
    @Test
    void update_shouldReturnFalse_whenTaskNotIdExists() {
        var givingDto= TaskUpdatingDto.builder().id(ID)
                .performerId(ID).loggedAccountId(ID).description("descccccccccccccccccccccc")
                .deadLineDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .priority("HGH").status("PERFORMED").build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.of(accountResponseDto));
        when(permissionService.checkPermissionUpdate(accountResponseDto)).thenReturn(true);
        when(taskRepository.findById(ID)).thenReturn(Optional.empty());

        var actual=taskService.update(givingDto);

        assertThat(actual).isFalse();
        verifyNoMoreInteractions(accountService,accountRepository,permissionService,taskRepository);
    }
    @Test
    void update_shouldThrowTaskUpdatingException_whenAccountNotExistException() {
        var givingDto= TaskUpdatingDto.builder().id(ID)
                .performerId(ID).loggedAccountId(ID).description("descccccccccccccccccccccc")
                .deadLineDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .priority("HGH").status("PERFORMED").build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(TaskUpdatingException.class)
                .isThrownBy(()->taskService.update(givingDto))
                .withCause(new AccountNotExistException(
                        "The user with id=%1$d does not exist"
                                .formatted(ID)));
        verifyNoMoreInteractions(accountService);
        verifyNoInteractions(permissionService,taskRepository);
    }
    @Test
    void update_shouldThrowTaskUpdatingException_whenUnsupportedOperationException() {
        var givingDto= TaskUpdatingDto.builder().id(ID)
                .performerId(ID).loggedAccountId(ID).description("descccccccccccccccccccccc")
                .deadLineDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .priority("HGH").status("PERFORMED").build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountService.findById(ID)).thenReturn(Optional.of(accountResponseDto));
        when(permissionService.checkPermissionUpdate(accountResponseDto)).thenReturn(false);

        assertThatExceptionOfType(TaskUpdatingException.class)
                .isThrownBy(()->taskService.update(givingDto))
                .withCause(new UnsupportedUpdateOperationException(
                        "The operation [%s] is not allowed for this logged account"
                                .formatted(PermissionType.UPDATE.name())));
        verifyNoMoreInteractions(accountService,permissionService);
        verifyNoInteractions(taskRepository);
    }



}