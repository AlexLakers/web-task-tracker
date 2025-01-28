package com.alex.task.tracker.integration.repository;

import com.alex.task.tracker.entity.*;
import com.alex.task.tracker.entity.enums.RoleName;
import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.entity.enums.TaskStatus;
import com.alex.task.tracker.integration.BaseIntegrationTest;
import com.alex.task.tracker.repository.TaskRepository;
import lombok.Cleanup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryIT extends BaseIntegrationTest {
    private static Task task;
    private final static Long ID=1L;

    @BeforeAll
    static void setTask() {
        var role = Role.builder().name(RoleName.TEST).permissions(List.of(PermissionType.TEST.name())).build();
        var account = Account.builder().id(ID)
                .accountInfo(AccountInfo.builder().firstName("alex").lastName("Alexov").build())
                .birthday(LocalDate.parse("1993-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .roles(List.of(role))
                .login("Lakers393")
                .password("Lakers393")
                .build();
        task= Task.builder().performer(account)
                .customer(account)
                .taskInfo(TaskInfo.builder().name("TaskTESTTTT").description("DESSSCSCSCSCTESTT")
                        .creationDate(LocalDate.now()).deadlineDate(LocalDate.now().plusDays(1)).build())
                .priority(TaskPriority.MDM)
                .status(TaskStatus.PROGRESS)
                .build();
    }

    @Test
    void findById_shouldReturnNotEmptyTask_whenIdIsExist() {
        @Cleanup var session= getSessionFactory().openSession();
        session.beginTransaction();
        var repository = new TaskRepository(session);

        var actualResult=repository.findById(ID);

        assertThat(actualResult.isPresent()).isTrue();
        assertThat(actualResult.get().getId()).isEqualTo(ID);
    }

    @Test
    void update_shouldUpdateTaskIntoDatabase_whenTaskExist() {
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        TaskRepository repo = new TaskRepository(session);
        var expected = session.find(Task.class, ID);
        session.detach(expected);
        expected.setPriority(TaskPriority.LOW);
        expected.setStatus(TaskStatus.PROGRESS);

        repo.update(expected);


        assertThat(session.find(Task.class, ID))
                .hasFieldOrPropertyWithValue("priority", expected.getPriority())
                .hasFieldOrPropertyWithValue("status", expected.getStatus());
    }

    @Test
    void delete_shouldTaskAccount_whenTaskExist() {
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        TaskRepository repo = new TaskRepository(session);
        var deletedTask = session.find(Task.class, ID);

        repo.delete(deletedTask);

        assertThat(session.find(Task.class, ID)).isNull();
    }



    @Test
    void save_shouldReturnSavedTask_whenTaskIsTransient() {
        Long expectedId=2L;
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        TaskRepository repo = new TaskRepository(session);

        var savedTask = repo.save(task);

        assertThat(savedTask.getId())
                .isNotNull().isEqualTo(expectedId);
    }

    @Test
    void findAll_shouldReturnAllAccounts_whenRolesExist() {
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        TaskRepository repo = new TaskRepository(session);

        var tasks = repo.findAll();

        assertThat(tasks).hasSize(1)
                .first().hasFieldOrPropertyWithValue("id", ID);
    }

}