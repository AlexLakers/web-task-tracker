package com.alex.task.tracker.service;

import com.alex.task.tracker.dto.AccountCreationDto;
import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.entity.Account;
import com.alex.task.tracker.entity.Role;
import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.exception.auth.AccountLoginException;
import com.alex.task.tracker.exception.auth.AccountRegistrationException;
import com.alex.task.tracker.mapper.AccountMapper;
import com.alex.task.tracker.repository.AccountRepository;
import com.alex.task.tracker.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Log4j

/**
 * This class is a part of a service layer of this app.
 * It consists of  methods which represent the business logic part about {@link Account entity}.
 * This is a single tone class.
 */

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountService {

    private static final AccountService INSTANCE;

    static {
        INSTANCE = initAccountService();
    }

    private static AccountService initAccountService() {
        return new AccountService(AccountMapper.getInstance(),
                AccountRepository.getInstance());

    }

    public static AccountService getInstance() {
        return INSTANCE;
    }

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    /**
     * Returns id of created account by input {@link AccountCreationDto dto}.
     * In this app input dto contains all the entered params using the registration form.
     * Firstly, occurs the validation process of transmitted dto using 'HibernateValidator'.
     * If input dto is valid then performs mapping process using {@link AccountMapper mapper}
     * from input {@link AccountCreationDto dto} to {@link Account account-entity}.
     * After it performs the main functional of this method-creating
     * a new account and writing it to the database using {@link AccountRepository repository}.
     * If something went wrong them current transaction will be rollback and throw new
     * {@link AccountRegistrationException exception}
     *
     * @param dto input data-dto.
     * @return if account has been created successfully then return not null account id.
     */

    public Long createAccount(AccountCreationDto dto) {
        log.debug("The method of creation user with input data: {} has been started", dto);
        //validation
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        log.debug("The validation process for input data: {} is successful", dto);
        boolean transactionStartedIsHere = false;

        Optional<Role> maybeRole;
        try {
            transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

            var account = accountMapper.toEntity(dto);

            var createdAccount = accountRepository.save(account);

            HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);
            log.debug("The user: {} has been created.", createdAccount);
            return createdAccount.getId();

        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new AccountRegistrationException(e);
        }


    }

    /**
     * Returns output dto wrapped in {@link Optional optional}
     * with the main data of logged account by login and password.
     * We use null safe opportunities of Optional class.
     * In this app login and password entered using login form.
     * Firstly, occurs account search using {@link AccountRepository repository}.
     * If the account has been found then logg in process is successfully for current account.
     * After it occurs mapping process using {@link AccountMapper mapper}
     * from {@link Account account} to {@link AccountResponseDto dto}.
     * If something went wrong then current transaction will be rollback and throw new
     * {@link AccountLoginException exception}.
     *
     * @param login entered login.
     * @param pass  entered pass.
     * @return Not empty  output dto wrapped in optional if logg in process performed successfully.
     * Else-an empty optional output dto.
     */

    public Optional<AccountResponseDto> loginAccount(String login, String pass) {
        log.debug("The method of log in account with login: {} and pass: {} has been started", login, pass);
        boolean transactionStartedIsHere = false;

        try {
            transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

            var maybeAccount = accountRepository.findByLoginAndPassword(login, pass);
            var maybeReadAccountDto = maybeAccount.map(accountMapper::toDto);

            HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);

            log.debug("The returned account: {} ", maybeAccount);
            return maybeReadAccountDto;

        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new AccountLoginException(e);
        }

    }

    /**
     * Returns output {@link AccountResponseDto dto} wrapped in {@link Optional optional}
     * with the main data of founded {@link Account account} by id.
     * We use null safe opportunities of Optional class.
     * Firstly, occurs account search using {@link AccountRepository repository} by transmitted id.
     * If the account has been found then occurs mapping process using {@link AccountMapper mapper}
     * from {@link Account account} to {@link AccountResponseDto dto}.
     * If something went wrong then current transaction will be rollback
     * and throw new {@link ServiceException exception}.
     *
     * @param id account id.
     * @return Not empty  output dto wrapped in optional if account exists.
     * Else-an empty optional output dto.
     */

    public Optional<AccountResponseDto> findById(Long id) {
        log.debug("The the method of getting account with id: {} was started", id);
        boolean transactionStartedIsHere = false;

        Optional<AccountResponseDto> maybeFoundAccountDto = Optional.empty();
        try {
            HibernateUtil.startNewTransactionIfNotActive();

            maybeFoundAccountDto = accountRepository.findById(id)
                    .map(accountMapper::toDto);

            HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);

            log.debug("The returned account dto : {}", maybeFoundAccountDto);
            return maybeFoundAccountDto;
        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new ServiceException(e);
        }


    }

    /**
     * Returns list of available account as a{@link AccountResponseDto dto}
     * with all the necessary params.
     * This process occurs using {@link AccountRepository repository}
     * and {@link AccountMapper mapper} functional.
     * If something went wrong then current transaction will be rollback
     * and throw new {@link ServiceException exception}.
     *
     * @return list of output dto.
     */

    public List<AccountResponseDto> findAll() {
        log.debug("The the method of getting all accounts were started");
        boolean transactionStartedIsHere = false;
        try {
            transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

            var accounts = accountRepository.findAll().stream()
                    .map(accountMapper::toDto)
                    .collect(Collectors.toList());

            HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);

            log.debug("The result of getting all accounts: {}", accounts);
            return accounts;
        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new ServiceException("An error of the reading all the accounts", e);
        }
    }

    /**
     * Returns boolean result of deleting process a specific
     * {@link Account account} by transmitted id.
     * But before the deleting process we should try to find it by id.
     * And then if account has been found - occurs deleting process
     * using {@link AccountRepository repository}.
     *
     * @param id account id.
     * @return true if the deleting process has been successfully,
     * else-false.
     */

    public boolean delete(Long id) {
        log.debug("The  method of deleting account with id: {} has been started", id);
        boolean transactionStartedIsHere = false;
        try {
            transactionStartedIsHere = HibernateUtil.startNewTransactionIfNotActive();

            var foundAccount = accountRepository.findById(id);
            foundAccount.ifPresent(
                    accountRepository::delete
            );

            HibernateUtil.commitCurrentTransactionIfOwn(transactionStartedIsHere);

            log.debug("The user with id: {} has been deleted successfully.", id);
            return foundAccount.isPresent();

        } catch (Exception e) {
            HibernateUtil.rollbackCurrentTransactionIfOwn(transactionStartedIsHere);
            throw new ServiceException(e);
        }
    }


}