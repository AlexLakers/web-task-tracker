package com.alex.task.tracker.service;


import com.alex.task.tracker.dto.AccountCreationDto;
import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.entity.Account;
import com.alex.task.tracker.entity.AccountInfo;
import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.exception.auth.AccountLoginException;
import com.alex.task.tracker.exception.auth.AccountRegistrationException;
import com.alex.task.tracker.mapper.AccountMapper;
import com.alex.task.tracker.repository.AccountRepository;
import com.alex.task.tracker.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static Account account;
    private static final Long ID=1L;

    @BeforeAll
    static void setAccount(){
        account=Account.builder()
                .id(1L)
                .accountInfo(AccountInfo.builder().firstName("alex").lastName("Alexov").build())
                .birthday(LocalDate.parse("1993-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .login("Alex")
                .build();
    }

    @Mock
    private AccountMapper accountMapper;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PermissionService permissionService;
    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_shouldReturnAccountId_whenCreationAccountDtoIsValid() {
        var creationAccountDto= AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                .lastName("Alexov").password("Alex123a").birthday("1993-01-01").build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountMapper.toEntity(creationAccountDto)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);

        var actual=accountService.createAccount(creationAccountDto);

        verifyNoMoreInteractions(accountMapper,accountRepository);
        assertEquals(actual,ID);
    }

    @Test
    void createAccount_shouldThrowAccountRegistrationException_whenAnyException(){
        var creationAccountDto= AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                .lastName("Alexov").password("Alex123a").birthday("1993-01-01").build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountMapper.toEntity(creationAccountDto)).thenReturn(account);
        doThrow(IllegalArgumentException.class).when(accountRepository).save(account);

        assertThatExceptionOfType(AccountRegistrationException.class)
                .isThrownBy(() -> accountService.createAccount(creationAccountDto))
                .withCause(new IllegalArgumentException());
        //.withMessageContaining("Account already exists")*/;
        verifyNoMoreInteractions(accountMapper,accountRepository);

        // assertThrowsExactly(AccountRegistrationException.class,()->accountService.createAccount(creationAccountDto));
    }

    @Test
    public void loginAccount_shouldReturnNotEmptyOptionalDto_whenLoginAndPasswordMatches(){
        String pass="Lakers";
        String login="Alex";
        var expectedDto= AccountResponseDto.builder().fullName("Alex Alexov").id(ID).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountRepository.findByLoginAndPassword(login,pass)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(expectedDto);

        var actual=accountService.loginAccount(login,pass);

        verifyNoMoreInteractions(accountMapper,accountRepository);
        assertEquals(actual,Optional.of(expectedDto));
    }

    @Test
    public void loginAccount_shouldReturnEmptyOptionalDto_whenLoginAndPasswordNotMatches(){
        String unknownPass="Lakers";
        String unknownLogin="Alex";
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountRepository.findByLoginAndPassword(unknownLogin,unknownPass)).thenReturn(Optional.empty());

        var actual=accountService.loginAccount(unknownLogin,unknownPass);

        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(accountMapper);
        assertEquals(actual,Optional.empty());
    }


    @Test
    void loginAccount_shouldThrowAccountLoginException_whenAnyException() {
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        doThrow(IllegalArgumentException.class).when(accountRepository).findByLoginAndPassword(anyString(),anyString());

        assertThatExceptionOfType(AccountLoginException.class)
                .isThrownBy(() -> accountService.loginAccount("",""))
                .withCause(new IllegalArgumentException());
        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(accountMapper);
    }
    @Test
    void findById_shouldReturnNotEmptyOptionalDto_whenIdExist() {
        var expectedDto=AccountResponseDto.builder().fullName("Alex Alexov").id(ID).build();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountRepository.findById(ID)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(expectedDto);

        var actual=accountService.findById(ID);

        verifyNoMoreInteractions(accountMapper,accountRepository);
        assertEquals(actual,Optional.of(expectedDto));
    }
    @Test
    public void findById_shouldReturnEmptyOptionalDto_whenIdNotExist(){

        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountRepository.findById(ID)).thenReturn(Optional.empty());

        var actual=accountService.findById(ID);

        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(accountMapper);
        assertEquals(actual,Optional.empty());
    }


    @Test
    void findById_shouldServiceException_whenAnyException() {
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        doThrow(IllegalArgumentException.class).when(accountRepository).findById(anyLong());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> accountService.findById(ID))
                .withCause(new IllegalArgumentException());
        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(accountMapper);
    }

    @Test
    void findAll_shouldReturnNotEmptyListDto_whenAccountsExists() {
        var accountDto=AccountResponseDto.builder().fullName("Alex Alexov").id(ID).build();
        var accountDto1=AccountResponseDto.builder().fullName("Serg Sergeev").id(ID).build();
        var account1=Account.builder().build();
        var accounts= Stream.of(account,account1).collect(Collectors.toList());
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountRepository.findAll()).thenReturn(accounts);
        when(accountMapper.toDto(account)).thenReturn(accountDto);
        when(accountMapper.toDto(account1)).thenReturn(accountDto1);

        var actual=accountService.findAll();

        verifyNoMoreInteractions(accountMapper,accountRepository);
        assertThat(actual)
                .isNotNull().isNotEmpty().hasSize(2)
                .contains(accountDto,accountDto1);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenAccountsNotExists() {
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        var actual=accountService.findAll();

        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(accountMapper);
        assertEquals(actual,Collections.emptyList());
    }

    @Test
    void findAll_shouldServiceException_whenAnyException() {
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        doThrow(IllegalArgumentException.class).when(accountRepository).findAll();

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> accountService.findAll())
                .withCause(new IllegalArgumentException());

        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(accountMapper);
    }

    @Test
    void delete_shouldReturnTrue_whenIdExist() {
        Optional<Account> maybeAccount= Optional.of(Account.builder().build());
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountRepository.findById(ID)).thenReturn(maybeAccount);
        doNothing().when(accountRepository).delete(maybeAccount.get());

        var actual=accountService.delete(ID);

        Assertions.assertEquals(actual,maybeAccount.isPresent());
        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(accountMapper);

    }
    @Test
    void delete_shouldReturnFalse_whenIdNotExist() {
        Optional<Account> empty= Optional.empty();
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountRepository.findById(ID)).thenReturn(empty);
        // doNothing().when(accountRepository).delete(maybeAccount.get());

        var actual=accountService.delete(ID);

        Assertions.assertEquals(actual,empty.isPresent());
        verify(accountRepository,times(0)).delete(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(accountMapper);
    }
    @Test
    void delete_shouldServiceException_whenAnyException() {
        @Cleanup MockedStatic<HibernateUtil> hibernateUtilMockStatic= mockStatic(HibernateUtil.class);
        hibernateUtilMockStatic.when(HibernateUtil::startNewTransactionIfNotActive).thenReturn(false);
        when(accountRepository.findById(ID)).thenReturn(Optional.of(Account.builder().build()));

        doThrow(IllegalArgumentException.class).when(accountRepository).delete(any(Account.class));

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> accountService.delete(ID))
                .withCause(new IllegalArgumentException());
        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(accountMapper);
    }


}