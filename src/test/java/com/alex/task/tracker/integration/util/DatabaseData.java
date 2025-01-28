package com.alex.task.tracker.integration.util;

import com.alex.task.tracker.entity.*;
import com.alex.task.tracker.entity.enums.RoleName;
import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.entity.enums.TaskStatus;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DatabaseData {
    private final static String SQL_RESET_ID="""
                ALTER TABLE account ALTER COLUMN id RESTART WITH 1;
                ALTER TABLE role ALTER COLUMN id RESTART WITH 1;
                ALTER TABLE task ALTER COLUMN id RESTART WITH 1;
                """;
    private final static String SQL_SET_SEQ_ROLE = """
            SELECT SETVAL('role_id_seq', (SELECT MAX(id) FROM role));""";
    private final static String SQL_SET_SEQ_ACCOUNT = """
            SELECT SETVAL('account_id_seq', (SELECT MAX(id) FROM account));""";
    private final static String SQL_SET_SEQ_TASK = """
            SELECT SETVAL('task_id_seq', (SELECT MAX(id) FROM task));""";



    public static void addData(SessionFactory sessionFactory){
        var session=sessionFactory.openSession();
        session.beginTransaction();
        session.createNativeQuery(SQL_RESET_ID).executeUpdate();
        var role = Role.builder().name(RoleName.TEST).permissions(List.of(PermissionType.TEST.name())).build();

        session.save(role);

        var account = Account.builder().
                accountInfo(AccountInfo.builder().firstName("alex").lastName("Alexov").build())
                .birthday(LocalDate.parse("1993-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .roles(List.of(role))
                .login("Lakers393")
                .password("Lakers393")
                .build();

        session.save(account);

        var task= Task.builder().performer(account)
                .customer(account)
                .taskInfo(TaskInfo.builder().name("TaskTESTTTT").description("DESSSCSCSCSCTESTT")
                        .creationDate(LocalDate.now()).deadlineDate(LocalDate.now().plusDays(1)).build())
                .priority(TaskPriority.MDM)
                .status(TaskStatus.PROGRESS)
                .build();

        session.save(task);




        session.createNativeQuery(SQL_SET_SEQ_ROLE).uniqueResult();
        session.createNativeQuery(SQL_SET_SEQ_ACCOUNT).uniqueResult();
        session.createNativeQuery(SQL_SET_SEQ_TASK).uniqueResult();
        session.getTransaction().commit();

        session.close();
    }

    public static void clearData(SessionFactory sessionFactory){
        var session=sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("delete from Task").executeUpdate();
        session.createQuery("delete from Account").executeUpdate();
        session.createQuery("delete from Role").executeUpdate();
        session.getTransaction().commit();
        session.close();

    }
}
