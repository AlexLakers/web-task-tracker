package com.alex.task.tracker.integration.repository;

import com.alex.task.tracker.entity.PermissionType;
import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.entity.enums.RoleName;
import com.alex.task.tracker.integration.BaseIntegrationTest;
import com.alex.task.tracker.repository.RoleRepository;
import lombok.Cleanup;
import org.assertj.core.api.Assertions;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryIT extends BaseIntegrationTest {
    private final static Integer ID=1;
    private static Role role;
    @BeforeAll
    static void setRole() {
        role=Role.builder().name(RoleName.TEST).permissions(List.of(PermissionType.TEST.name())).build();
    }

    @Test
    void findById_shouldReturnNotEmptyRole_whenIdIsExist() {
        @Cleanup var session= getSessionFactory().openSession();
        session.beginTransaction();
        var repository = new RoleRepository(session);

        var actualResult=repository.findById(ID);
        assertThat(actualResult.isPresent()).isTrue();
        assertThat(actualResult.get()).isEqualTo(role);
    }

    @Test
    void update_shouldUpdateRoleIntoDatabase_whenRoleExist() {
        @Cleanup var session= getSessionFactory().openSession();
        session.beginTransaction();
        RoleRepository repo = new RoleRepository(session);
        var expected=session.find(Role.class, ID);
        Hibernate.initialize(expected.getPermissions());
        session.detach(expected);
        expected.getPermissions().add(PermissionType.UPDATE.name());

        repo.update(expected);
        var actual= session.find(Role.class, ID);

        assertThat(actual.getPermissions())
                .hasSize(2).containsExactly(PermissionType.TEST.name(),PermissionType.UPDATE.name());

    }

    @Test
    void delete_shouldDeleteRole_whenRoleExist() {
        @Cleanup var session= getSessionFactory().openSession();
        session.beginTransaction();
        RoleRepository repo = new RoleRepository(session);
        var deletedRole=session.find(Role.class, ID);

        session.createNativeQuery("DELETE FROM account_role WHERE role_id=%s".formatted(deletedRole.getId()))
                .executeUpdate();
        repo.delete(deletedRole);

        assertThat(session.find(Role.class, ID)).isNull();
    }
    @Test
    void save_shouldReturnSavedRole_whenRoleIsTransient(){
        Integer expectedId=2;
        var givenRole=Role.builder().name(RoleName.ADMIN).permissions(List.of(PermissionType.READ_WRITE.name())).build();
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        RoleRepository repo = new RoleRepository(session);

        var savedRole = repo.save(givenRole);

        assertThat(savedRole.getId())
                .isNotNull().isEqualTo(expectedId);
    }
    @Test
    void findAll_shouldReturnAllRoles_whenRolesExist(){
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        RoleRepository repo = new RoleRepository(session);

        var accounts = repo.findAll();

        assertThat(accounts).hasSize(1)
                .first().hasFieldOrPropertyWithValue("id", ID);
    }
    @Test
    void findByName_shouldReturnRole_whenLRoleNameExist(){
        var roleName=RoleName.TEST;
        @Cleanup var session = getSessionFactory().openSession();
        session.beginTransaction();
        RoleRepository repo = new RoleRepository(session);

        var actual= repo.findByName(roleName.name());

        assertThat(actual).isNotNull().get()
                .hasFieldOrPropertyWithValue("name",roleName);
    }

}