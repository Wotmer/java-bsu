package bank.service.processing.command;

import bank.model.Account;
import bank.model.Transaction;
import bank.repository.AccountRepository;
import bank.visitor.Visitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DepositCommand implements TransactionCommand {
    private final Transaction transaction;
    private final AccountRepository repository;

    @Override
    public void execute() {
        Account account = repository.findById(transaction.getSourceAccountId());
        if (account == null) throw new RuntimeException("Account not found");

        account.getLock().lock();
        try {
            if (account.isFrozen()) throw new IllegalStateException("Account is frozen");
            account.setBalance(account.getBalance().add(transaction.getAmount()));
            repository.update(account);
        } finally {
            account.getLock().unlock();
        }
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}