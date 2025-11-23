package bank.service.processing.factory;

import bank.model.Transaction;
import bank.repository.AccountRepository;
import bank.service.processing.command.*;

public class CommandFactory {
    private final AccountRepository accountRepository;

    public CommandFactory(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public TransactionCommand createCommand(Transaction tx) {
        return switch (tx.getType()) {
            case DEPOSIT -> new DepositCommand(tx, accountRepository);
            case WITHDRAW -> new WithdrawCommand(tx, accountRepository);
            case TRANSFER -> new TransferCommand(tx, accountRepository);
            case FREEZE -> new FreezeCommand(tx, accountRepository);
            default -> throw new IllegalArgumentException("Unknown transaction type");
        };
    }
}