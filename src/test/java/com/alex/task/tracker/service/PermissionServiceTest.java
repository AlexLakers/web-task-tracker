package com.alex.task.tracker.service;

import static org.junit.jupiter.api.Assertions.*;


import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.entity.Account;
import com.alex.task.tracker.entity.AccountInfo;
import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.entity.enums.RoleName;
import com.alex.task.tracker.exception.RoleNotExistException;
import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.repository.RoleRepository;
import com.alex.task.tracker.util.HibernateUtil;
import lombok.Cleanup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.ServerException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {
    private static Account account;
    private static AccountResponseDto accountResponseDto;
    private static List<Role> adminRoles;
    private static List<Role> userRoles;
    @BeforeAll
    static void setAccount() {
        account = Account.builder()
                .id(1L)
                .accountInfo(AccountInfo.builder().firstName("alex").lastName("Alexov").build())
                .birthday(LocalDate.parse("1993-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .login("Alex")
                .build();
        accountResponseDto= AccountResponseDto.builder().fullName("Alex Alexov").id(1L).build();
        adminRoles=List.of(
                Role.builder().name(RoleName.USER).permissions(Collections.singletonList("READ_WRITE")).id(1).build(),
                Role.builder().name(RoleName.ADMIN).permissions(Arrays.asList("UPDATE","DELETE")).id(2).build());
        userRoles=Collections.singletonList(
                Role.builder().name(RoleName.USER).permissions(Collections.singletonList("READ_WRITE")).id(1).build());
    }
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private PermissionService permissionService;

    @Test
    void checkPermissionUpdate_shouldReturnTrue_whenAccountIsAdmin() {
        var sRoles= List.of("USER","ADMIN");
        var givingAccountDto= AccountResponseDto.builder()
                .roles(sRoles).fullName("Alex Alexov").id(1L).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findAll(sRoles)).thenReturn(adminRoles);

        var actual=permissionService.checkPermissionUpdate(givingAccountDto);
        Assertions.assertThat(actual).isTrue();
    }
    @Test
    void checkPermissionUpdate_shouldReturnFalse_whenAccountIsUser() {
        var sRoles= List.of("USER");
        var givingAccountDto= AccountResponseDto.builder()
                .roles(sRoles).fullName("Alex Alexov").id(1L).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findAll(sRoles)).thenReturn(userRoles);

        var actual=permissionService.checkPermissionUpdate(givingAccountDto);
        Assertions.assertThat(actual).isFalse();
    }
    @Test
    void checkPermissionUpdate_shouldThrowServiceException_whenRoleNotExistException() {
        var sRoles= List.of("USER","ADMIN");
        var givingAccountDto= AccountResponseDto.builder()
                .roles(sRoles).fullName("Alex Alexov").id(1L).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findAll(sRoles)).thenReturn(Collections.emptyList());

        Assertions.assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(()-> permissionService.checkPermissionUpdate(givingAccountDto))
                .withCause(new RoleNotExistException( "The user roles:[%1$s] but the founded roles:[%2$s]"
                        .formatted(sRoles, Collections.emptyList())));

    }


    @Test
    void checkPermissionDelete_shouldReturnTrue_whenAccountIsAdmin() {
        var sRoles= List.of("USER","ADMIN");
        var givingAccountDto= AccountResponseDto.builder()
                .roles(sRoles).fullName("Alex Alexov").id(1L).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findAll(sRoles)).thenReturn(adminRoles);

        var actual=permissionService.checkPermissionDelete(givingAccountDto);
        Assertions.assertThat(actual).isTrue();
    }
    @Test
    void checkPermissionDelete_shouldReturnFalse_whenAccountIsUser() {
        var sRoles= List.of("USER");
        var givingAccountDto= AccountResponseDto.builder()
                .roles(sRoles).fullName("Alex Alexov").id(1L).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findAll(sRoles)).thenReturn(userRoles);

        var actual=permissionService.checkPermissionDelete(givingAccountDto);
        Assertions.assertThat(actual).isFalse();
    }
    @Test
    void checkPermissionDelete_shouldThrowServiceException_whenRoleNotExistException() {
        var sRoles= List.of("USER","ADMIN");
        var givingAccountDto= AccountResponseDto.builder()
                .roles(sRoles).fullName("Alex Alexov").id(1L).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findAll(sRoles)).thenReturn(Collections.emptyList());

        Assertions.assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(()-> permissionService.checkPermissionDelete(givingAccountDto))
                .withCause(new RoleNotExistException( "The user roles:[%1$s] but the founded roles:[%2$s]"
                        .formatted(sRoles, Collections.emptyList())));

    }

    @Test
    void saveDefaultRoleAndPermissionsIfNotExist_shouldUpdateRole_whenRoleAndPermissionsExists() {
        var maybeRole=userRoles.stream().findFirst();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findByName(RoleName.USER.name())).thenReturn(maybeRole);
        doNothing().when(roleRepository).update(maybeRole.get());

        permissionService.saveDefaultRoleAndPermissionsIfNotExist();

        verify(roleRepository,times(1)).update(maybeRole.get());
        verify(roleRepository,times(1)).findByName(RoleName.USER.name());
        verifyNoMoreInteractions(roleRepository);
    }
    @Test
    void saveDefaultRoleAndPermissionsIfNotExist_shouldSaveRole_whenRoleAndPermissionsNotExists() {
        var role=userRoles.get(0);
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findByName(RoleName.USER.name())).thenReturn(Optional.empty());
        doReturn(role).when(roleRepository).save(role);

        permissionService.saveDefaultRoleAndPermissionsIfNotExist();

        verify(roleRepository,times(1)).save(role);
        verify(roleRepository,times(1)).findByName(RoleName.USER.name());
        verifyNoMoreInteractions(roleRepository);
    }


    @Test
    void checkPermissionCreate_shouldReturnTrue_whenAccountIsAdmin() {
        var sRoles= List.of("USER");
        var givingAccountDto= AccountResponseDto.builder()
                .roles(sRoles).fullName("Alex Alexov").id(1L).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findAll(sRoles)).thenReturn(userRoles);

        var actual=permissionService.checkPermissionCreate(givingAccountDto);
        Assertions.assertThat(actual).isTrue();
    }
    @Test
    void checkPermissionCreate_shouldThrowServiceException_whenRoleNotExistException() {
        var sRoles= List.of("USER");
        var givingAccountDto= AccountResponseDto.builder()
                .roles(sRoles).fullName("Alex Alexov").id(1L).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(roleRepository.findAll(sRoles)).thenReturn(Collections.emptyList());

        Assertions.assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(()-> permissionService.checkPermissionCreate(givingAccountDto))
                .withCause(new RoleNotExistException( "The user roles:[%1$s] but the founded roles:[%2$s]"
                        .formatted(sRoles, Collections.emptyList())));

    }


    static Stream<Arguments> getUpdateArgs() {

        return Stream.of(
                Arguments.of(AccountResponseDto.builder()
                        .roles(List.of("USER","ADMIN")).build(),adminRoles,true)

        );
    }
}