package com.alex.task.tracker.mapper;


import com.alex.task.tracker.dto.TaskCreationDto;
import com.alex.task.tracker.dto.TaskResponseDto;
import com.alex.task.tracker.entity.Task;
import com.alex.task.tracker.entity.TaskInfo;
import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.entity.enums.TaskStatus;
import com.alex.task.tracker.repository.AccountRepository;
import com.alex.task.tracker.util.DateTimeHelper;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * This class is a mapper for {@link Task entity}.
 * @see Mapper mapper.
 *
 */

@RequiredArgsConstructor
public class TaskMapper implements Mapper<Task, TaskResponseDto, TaskCreationDto> {

    private static final TaskMapper INSTANCE = new TaskMapper(AccountRepository.getInstance());

    public static TaskMapper getInstance() {
        return INSTANCE;
    }

    private final AccountRepository accountRepository;


    @Override
    public TaskResponseDto toDto(Task entity) {
        return TaskResponseDto.builder()
                .id(entity.getId())
                .name(entity.getTaskInfo().getName())
                .costumer(entity.getCustomer().getAccountInfo().getFirstName() + " " +
                        entity.getCustomer().getAccountInfo().getLastName())
                .performer(entity.getPerformer().getAccountInfo().getFirstName() + " " +
                        entity.getPerformer().getAccountInfo().getLastName())
                .creationDate(entity.getTaskInfo().getCreationDate())
                .deadLineDate(entity.getTaskInfo().getDeadlineDate())
                .description(entity.getTaskInfo().getDescription())
                .priority(entity.getPriority().name())
                .status(entity.getStatus().name())
                .build();
    }

    @Override
    public Task toEntity(TaskCreationDto dto) {
        return Task.builder()
                .taskInfo(TaskInfo.builder()
                        .creationDate(LocalDate.now())
                        .name(dto.name())
                        .deadlineDate(DateTimeHelper.parseDate(dto.deadLineDate()))
                        .description(dto.description())
                        .build()
                )
                .priority(TaskPriority.valueOf(dto.priority()))
                .customer(accountRepository.findById(dto.loggedAccountId()).orElse(null))
                .performer(accountRepository.findById(dto.performerId()).orElse(null))
                .status(TaskStatus.PROGRESS)
                .build();
    }
}
