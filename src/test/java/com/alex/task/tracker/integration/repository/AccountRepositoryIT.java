package com.alex.task.tracker.integration.repository;


import com.alex.task.tracker.entity.Account;
import com.alex.task.tracker.entity.AccountInfo;
import com.alex.task.tracker.entity.PermissionType;
import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.entity.enums.RoleName;
import com.alex.task.tracker.integration.BaseIntegrationTest;
import com.alex.task.tracker.repository.AccountRepository;
import lombok.Cleanup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.yaml.snakeyaml.events.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountRepositoryIT extends BaseIntegrationTest {
    private static Account account;
    private final static Long ID = 1L;

    @BeforeAll
    void setAccount() {
        var role = Role.builder().name(RoleName.TEST).permissions(List.of(PermissionType.TEST.name())).build();
        account = Account.builder()
                .accountInfo(AccountInfo.builder().firstName("alex").lastName("Alexov").build())
                .birthday(LocalDate.parse("1993-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .roles(List.of(role))
                .login("Lakers292")
                .password("Lakers292")
                .build();
    }

    @Test
    void findById_shouldReturnNotEmptyAccount_whenIdIsExist() {
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        var repository = new AccountRepository(session);

        var actualResult = repository.findById(ID);

        assertThat(actualResult.isPresent()).isTrue();
        assertThat(actualResult.get().getId()).isEqualTo(ID);
        /*  Assertions.assertThat(actualResult*/

        //Assertions.assertThat(actual).isNotNull().isInstanceOf(Account.class)

    }

    @Test
    void update_shouldUpdateAccountIntoDatabase_whenAccountExist() {
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        AccountRepository repo = new AccountRepository(session);
        var expected = session.find(Account.class, ID);
        session.detach(expected);
        expected.setPassword("changedPass");
        expected.setLogin("changedLogin");

        repo.update(expected);

        assertThat(session.find(Account.class, ID))
                .hasFieldOrPropertyWithValue("password", expected.getPassword())
                .hasFieldOrPropertyWithValue("login", expected.getLogin());

    }

    @Test
    void delete_shouldDeleteAccount_whenAccountExist() {
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        AccountRepository repo = new AccountRepository(session);
        var deletedAccount = session.find(Account.class, ID);

        repo.delete(deletedAccount);

        assertThat(session.find(Account.class, ID)).isNull();
    }

    @Test
    void save_shouldReturnSavedAccount_whenAccountIsTransient() {
        Long expectedId=2L;
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        AccountRepository repo = new AccountRepository(session);

        var savedAccount = repo.save(account);

        assertThat(savedAccount.getId())
                .isNotNull().isEqualTo(expectedId);
    }

    @Test
    void findAll_shouldReturnAllAccounts_whenAccountsExist() {
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        AccountRepository repo = new AccountRepository(session);

        var accounts = repo.findAll();

        assertThat(accounts).hasSize(1)
                .first().hasFieldOrPropertyWithValue("id", ID);
    }

    @Test
    void findByIdLoginAndPassword_shouldReturnAccount_whenLoginAndPasswordExist() {
        var pass="Lakers393";
        var login="Lakers393";
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        AccountRepository repo = new AccountRepository(session);

        var actual = repo.findByLoginAndPassword(login,pass);

        assertThat(actual).isNotNull()
                .get().hasFieldOrPropertyWithValue("password", pass)
                .hasFieldOrPropertyWithValue("login",login);
    }
}