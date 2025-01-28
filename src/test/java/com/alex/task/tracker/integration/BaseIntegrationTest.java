package com.alex.task.tracker.integration;

import com.alex.task.tracker.integration.util.DatabaseData;
import com.alex.task.tracker.integration.util.HibernateUtilTest;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Getter
public class BaseIntegrationTest {
    private SessionFactory sessionFactory;
    @BeforeAll
    void setSessionFactory() throws Exception {
        sessionFactory= HibernateUtilTest.buildSessionFactory();
    }
    @BeforeEach
    void importData() {

        System.out.println("Importing data...");
        DatabaseData.addData(sessionFactory);
    }
    @AfterEach
    void clearData() {
        System.out.println("cearing data..");
        DatabaseData.clearData(sessionFactory);
    }
    @AfterAll
    void closeSessionFactory() {
        System.out.println("closing sessionFactory..");
        sessionFactory.close();
    }
}
