package com.alex.task.tracker.mapper;

import com.alex.task.tracker.dto.TaskCreationDto;
import com.alex.task.tracker.dto.TaskResponseDto;
import com.alex.task.tracker.entity.*;
import com.alex.task.tracker.entity.enums.RoleName;
import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.entity.enums.TaskStatus;
import com.alex.task.tracker.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {
    private static Account account;
    private static Task task;
    private final static DateTimeFormatter DEFAULT_PATTERN=DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeAll
    static void initAccount() {
        account = Account.builder()
                .id(1L)
                .accountInfo(AccountInfo.builder().firstName("alex").lastName("Alexov").build())
                .birthday(LocalDate.parse("1993-01-01", DEFAULT_PATTERN))
                .login("Alex")
                .build();

        account.addRole(Role.builder().name(RoleName.USER).build());

        task = Task.builder()
                .taskInfo(
                        TaskInfo.builder().name("TASK1")
                                .creationDate(LocalDate.now())
                                .deadlineDate(LocalDate.now().plusDays(1))
                                .description("desc").build())
                .performer(account)
                .customer(account)
                .status(TaskStatus.PROGRESS)
                .priority(TaskPriority.HGH)
                .build();

        account.addCustomerTask(task);
        account.addPerformerTask(task);
    }

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private TaskMapper taskMapper;

    @Test
    void toDto_shouldReturnTaskDto_whenTaskNotNull() {
        var expected = TaskResponseDto.builder().name("TASK1").costumer("alex Alexov").performer("alex Alexov")
                .creationDate(LocalDate.now())
                .deadLineDate(LocalDate.now().plusDays(1))
                .description("desc").priority(TaskPriority.HGH.name()).status(TaskStatus.PROGRESS.name())
                .build();

        var actual = taskMapper.toDto(task);

        Assertions.assertThat(actual)
                .isExactlyInstanceOf(TaskResponseDto.class)
                .isEqualTo(expected);

    }

    @Test
    void toEntity_shouldReturnTask_whenTaskDtoNotNull() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        var givingDto= TaskCreationDto.builder().name("TASK1").loggedAccountId(1L).performerId(1L)
                .deadLineDate(LocalDate.now().plusDays(1).format(DEFAULT_PATTERN)).description("desc")
                .priority(TaskPriority.HGH.name()).build();

        var actual= taskMapper.toEntity(givingDto);

        Assertions.assertThat(actual)
                .isExactlyInstanceOf(Task.class)
                .isEqualTo(task);

    }


}