package bank.service;

import bank.model.Account;
import bank.model.Transaction;
import bank.model.User;
import bank.repository.AccountRepository;
import bank.service.processing.AsyncTransactionProcessor;
import bank.service.processing.factory.CommandFactory;

import java.math.BigDecimal;
import java.util.UUID;

public class BankService {
    private final AccountRepository accountRepository;
    private final AsyncTransactionProcessor processor;
    private final CommandFactory commandFactory;

    public BankService(AccountRepository accountRepository, AsyncTransactionProcessor processor) {
        this.accountRepository = accountRepository;
        this.processor = processor;
        this.commandFactory = new CommandFactory(accountRepository);
    }

    public Account createAccount(User user) {
        Account account = new Account(user.getId());
        accountRepository.save(account);
        return account;
    }

    public void processTransaction(Transaction transaction) {
        processor.submit(commandFactory.createCommand(transaction));
    }

    public BigDecimal getBalance(UUID accountId) {
        Account account = accountRepository.findById(accountId);
        return account != null ? account.getBalance() : BigDecimal.ZERO;
    }

    public Account getAccount(UUID accountId) {
        return accountRepository.findById(accountId);
    }
}