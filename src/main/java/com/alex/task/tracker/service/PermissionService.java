package com.alex.task.tracker.service;


import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.entity.PermissionType;
import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.entity.enums.RoleName;
import com.alex.task.tracker.exception.RoleNotExistException;
import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.repository.RoleRepository;
import com.alex.task.tracker.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is a part of a service layer of this app.
 * It consists of  methods which allow check different {@link PermissionType permissions}
 * for {@link AccountResponseDto loggedAccountDto} before calling a specific operation.
 * This is a single tone class.
 */

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class PermissionService {

    private static final PermissionService INSTANCE;

    public static PermissionService getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new PermissionService(RoleRepository.getInstance());
    }

    private final RoleRepository roleRepository;

    /**
     * Returns boolean result of checking {@link PermissionType permissions}
     * for the updating task operation.
     * The checking process occurs by {@link AccountResponseDto dto} if to be more precise
     * this method uses also standard {@link Role role} 'ADMIN'
     * and standard {@link PermissionType permission} 'UPDATE'.
     * The main algorithm of checking described into the
     * {@link this#checkPermissionGeneral(AccountResponseDto acc, PermissionType perm, RoleName... roles)}.
     *
     * @param loggedAccount logged account dto.
     * @return true if logged account has the 'UPDATE' permission and 'ADMIN' role.
     * Else-false.
     */

    public boolean checkPermissionUpdate(AccountResponseDto loggedAccount) {
        log.debug("The checking for update operation is started with arg: {}", loggedAccount);
        return checkPermissionGeneral(loggedAccount, PermissionType.UPDATE, RoleName.ADMIN);
    }

    /**
     * Returns boolean result of checking {@link PermissionType permissions}
     * for the deleting task operation.
     * The checking process occurs by {@link AccountResponseDto dto} if to be more precise
     * this method uses also standard {@link Role role} 'ADMIN'
     * and standard {@link PermissionType permission} 'DELETE'.
     * The main algorithm of checking described into the
     * {@link this#checkPermissionGeneral(AccountResponseDto acc, PermissionType perm, RoleName... roles)}.
     *
     * @param loggedAccount logged account dto.
     * @return true if logged account has the 'DELETE' permission and 'ADMIN' role.
     * Else-false.
     */

    public boolean checkPermissionDelete(AccountResponseDto loggedAccount) {
        log.debug("The checking for delete operation is started with arg: {}", loggedAccount);
        return checkPermissionGeneral(loggedAccount, PermissionType.DELETE, RoleName.ADMIN);
    }

    public void saveDefaultRoleAndPermissionsIfNotExist() {
        log.debug("The checking roles by available is started");

        boolean transactionStartedIsHere = false;

        transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();
        Optional<Role> maybeRole = Optional.empty();
        maybeRole = roleRepository.findByName(RoleName.USER.name());
        maybeRole.ifPresentOrElse(
                (role) -> {
                    if (role.getPermissions().stream()
                            .anyMatch(perm -> perm.equals(PermissionType.READ_WRITE.name()))) {
                        role.setPermissions(
                                Stream.of(PermissionType.READ_WRITE.name())
                                        .collect(Collectors.toList())
                        );
                        log.warn("The default role:{} does not contain default permission:[{}]",
                                role.getName().name(), PermissionType.READ_WRITE.name());

                        roleRepository.update(role);
                        log.debug("The default permission 'READ_WRITE' is saved");
                    }
                },
                () -> {

                    log.warn("The default role:{} is not exist", RoleName.USER.name());
                    roleRepository.save(Role.builder()
                            .name(RoleName.USER)
                            .permissions(Stream.of(PermissionType.READ_WRITE.name())
                                    .collect(Collectors.toList()))
                            .build());
                    log.debug("The default permission 'READ_WRITE' is saved");
                }
        );
        HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);
    }

    /**
     * Returns boolean result of checking {@link PermissionType permissions}
     * for the creation a new task operation.
     * The checking process occurs by {@link AccountResponseDto dto} if to be more precise
     * this method uses also standard {@link Role role} 'USER'
     * and standard {@link PermissionType permission} 'READ_WRITE'.
     * The main algorithm of checking described into the
     * {@link this#checkPermissionGeneral(AccountResponseDto acc, PermissionType perm, RoleName... roles)}.
     *
     * @param loggedAccount logged account dto.
     * @return true if logged account has the 'READ_WRITE' permission and 'USER' role.
     * Else-false.
     */

    public boolean checkPermissionCreate(AccountResponseDto loggedAccount) {
        log.debug("The checking for create operation is started with arg:{}", loggedAccount);
        return checkPermissionGeneral(loggedAccount, PermissionType.READ_WRITE, RoleName.USER);
    }

    /**
     * Returns boolean result of checking {@link PermissionType permissions} by
     * specific account {@link Role role}.
     * Firstly, we try to find roles of the logged account
     * using {@link RoleRepository repository}.Then if the found roles is not empty
     * we call {@link this#testRoles(List accRoles, RoleName[] names, PermissionType perm) method}
     * which returns true if account roles contains standard roles and has permissions.
     * Bellow the main algorithm.
     *
     * @param loggedAccount  logged account dto.
     * @param permissionType standard type of permission.
     * @param standardRoles  standard name of roles.
     * @return true if logged account has given permissions and roles.
     * Else-false.
     */

    private boolean checkPermissionGeneral(AccountResponseDto loggedAccount,
                                           PermissionType permissionType,
                                           RoleName... standardRoles) {
        log.debug("The check permissionGeneral method with args:{},{},{} has been started",
                loggedAccount, permissionType, standardRoles);
        boolean transactionStartedIsHere = false;

        transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

        try {


            var loggedAccountRoles = loggedAccount.getRoles();

            var foundRoles = roleRepository.findAll(loggedAccountRoles);
            log.debug("The found roles: {}", foundRoles);
            if (!foundRoles.isEmpty() &&
                    (foundRoles.size() == loggedAccountRoles.size())) {
                var isPermit = testRoles(foundRoles, standardRoles, permissionType);

                HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);
                return isPermit;

            } else {
                throw new RoleNotExistException(
                        "The user roles:[%1$s] but the founded roles:[%2$s]"
                                .formatted(loggedAccountRoles,
                                        foundRoles)
                );
            }
        } catch (ServiceException e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new ServiceException(e);
        }
    }


    private boolean testRoles(List<Role> usedRoles, RoleName[] roleNames, PermissionType permissionType) {
        log.debug("The test roles method with args:{},{},{}",
                usedRoles, roleNames, permissionType);

        Predicate<Role> predicatePermissions = role -> role.getPermissions().stream()
                .anyMatch(perm -> perm.equals(permissionType.name()));

        Predicate<Role> predicateRoles = role -> Arrays.stream(roleNames)
                .anyMatch(roleName -> Objects.equals(roleName, role.getName()));

        return usedRoles.stream()
                .anyMatch(predicateRoles.and(predicatePermissions));
    }


}

