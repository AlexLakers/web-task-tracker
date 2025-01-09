package com.alex.task.tracker.mapper;


import com.alex.task.tracker.dto.AccountCreationDto;
import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.entity.Account;
import com.alex.task.tracker.entity.AccountInfo;
import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.entity.enums.RoleName;
import com.alex.task.tracker.repository.RoleRepository;
import com.alex.task.tracker.util.DateTimeHelper;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * This class is a mapper for account entity.
 *
 * @see Mapper mapper.
 */

@RequiredArgsConstructor
public class AccountMapper implements Mapper<Account, AccountResponseDto, AccountCreationDto> {
    private static final AccountMapper INSTANCE = new AccountMapper(
            RoleRepository.getInstance()
    );

    public static AccountMapper getInstance() {
        return INSTANCE;
    }

    private final RoleRepository roleRepository;

    @Override
    public AccountResponseDto toDto(Account account) {
        return AccountResponseDto.builder()
                .id(account.getId())
                .fullName(account.getAccountInfo().getFirstName() + " "
                        + account.getAccountInfo().getLastName())
                .roles(account.getRoles().stream()
                        .map(role -> role.getName().name())
                        .toList())
                .build();
    }

    public Account toEntity(AccountCreationDto dto) {

        return Account.builder()
                .accountInfo(AccountInfo.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName()).build())
                .login(dto.login())
                .password(dto.password())
                .birthday(DateTimeHelper.parseDate(dto.birthday()))
                .roles(List.of(roleRepository.findByName(RoleName.USER.name())
                        .orElse(Role.builder().name(RoleName.USER).build())))
                .build();
    }


}
