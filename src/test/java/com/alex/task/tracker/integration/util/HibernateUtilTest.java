package com.alex.task.tracker.integration.util;

import com.alex.task.tracker.converter.PasswordAttributeConverter;
import com.alex.task.tracker.entity.Account;
import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.entity.Task;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HibernateUtilTest {

    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");
    static {
        postgreSQLContainer.start();
    }



    public static SessionFactory buildSessionFactory(){
        var configuration=buildConfiguration();
        configuration.configure();
        return configuration.buildSessionFactory();

    }
    private static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Task.class);
        configuration.addAnnotatedClass(Account.class);
        configuration.addAnnotatedClass(Role.class);
        //  configuration.addAnnotatedClass(Permission.class);
        configuration.setProperty("hibernate.connection.url", postgreSQLContainer.getJdbcUrl());
        /*configuration.setProperty("hibernate.connection.username", postgreSQLContainer.getUsername());
        configuration.setProperty("hibernate.connection.password", postgreSQLContainer.getPassword());*/
        configuration.addAttributeConverter(new PasswordAttributeConverter(), false);

        return configuration;

    }




}
