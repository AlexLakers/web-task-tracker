package com.alex.task.tracker.util;

import com.alex.task.tracker.converter.PasswordAttributeConverter;
import com.alex.task.tracker.entity.Account;
import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.entity.Task;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.lang.reflect.Proxy;

/**
 * This is utility class that provides all the necessary methods to configure Hibernate configuration.
 * Also  this class includes functional for building the main objects:SessionFactory,Session.
 */

@Slf4j
@UtilityClass
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = buildSessionFactory();
    }

    public static Session getSessionProxy() {

        return buildSessionProxy();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }

    /**
     * Return proxy object which gets current session using thread local strategy.
     * This strategy the fallowing: After transaction beginning a current session is added to local thread var,
     * then after the commit transaction session is removed from local thread var.
     *
     * @return proxy object as a session.
     */

    private static Session buildSessionProxy() {
        if(sessionFactory == null) {
            return null;
        }
        return (Session) Proxy.newProxyInstance(sessionFactory.getClass().getClassLoader(), new Class[]{Session.class},
                (proxy, method, args) -> {
                    return method.invoke(sessionFactory.getCurrentSession(), args);
                });
    }


    public boolean startNewTransactionIfNotActive(){
        Session session = buildSessionProxy();
        if(!session.getTransaction().isActive()){
            var transaction=session.beginTransaction();
            log.debug("The transaction has been started");
            return transaction.isActive();
        }
        return false;
    }
    public boolean commitCurrentTransactionIfOwn(boolean isOwner){
        Session session = buildSessionProxy();
        if(isOwner){
            session.getTransaction().commit();
            log.debug("The transaction has been commited");
            return true;
        }
        return false;
    }
    public boolean rollbackCurrentTransactionIfOwn(boolean isOwner){
        Session session = buildSessionProxy();
        if(isOwner){
            session.getTransaction().rollback();
            log.debug("The transaction has been rolled back.");
            return true;
        }
        return false;
    }

    private static SessionFactory buildSessionFactory() {
        var configuration = buildConfiguration();

        configuration.configure();
        String prodUsername=configuration.getProperty("connection.username");
        if(prodUsername!=null && !prodUsername.equals("test")){
            return configuration.buildSessionFactory();
        }
        return null;
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Task.class);
        configuration.addAnnotatedClass(Account.class);
        configuration.addAnnotatedClass(Role.class);
        //  configuration.addAnnotatedClass(Permission.class);
        configuration.addAttributeConverter(new PasswordAttributeConverter(), false);

        return configuration;

    }


}
