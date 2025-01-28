package com.alex.task.tracker.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.alex.task.tracker.dto.AccountCreationDto;
import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.entity.Account;
import com.alex.task.tracker.entity.AccountInfo;
import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.entity.enums.RoleName;
import com.alex.task.tracker.repository.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountMapperTest {

    private static Role role;
    private static Account account;

    @BeforeAll
    static void initRole(){
        role=Role.builder().name(RoleName.USER).build();

        account=Account.builder()
                .id(1L)
                .accountInfo(AccountInfo.builder().firstName("alex").lastName("Alexov").build())
                .birthday(LocalDate.parse("1993-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .login("Alex")
                .build();

        account.addRole(role);
    }

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AccountMapper accountMapper;

    @Test
    void toDto_shouldReturnDto_whenAccountIsNotNull() {
        var expected= AccountResponseDto.builder().id(1L).fullName("alex Alexov").roles(List.of(role.getName().name()))
                .build();

        var actual=accountMapper.toDto(account);

        assertThat(actual)
                .isExactlyInstanceOf(AccountResponseDto.class)
                .isEqualTo(expected);


    }

    @Test
    void toEntity_shouldReturnEntity_whenDtoIsNotNull() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        var givingDto= AccountCreationDto.builder().login("Alex").firstName("alex")
                .lastName("Alexov").password("Alex123a").birthday("1993-01-01").build();

        var actual=accountMapper.toEntity(givingDto);

        assertThat(actual)
                .isExactlyInstanceOf(Account.class)
                .isEqualTo(account);
    }



}