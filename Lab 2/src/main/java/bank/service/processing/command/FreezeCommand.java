package bank.service.processing.command;

import bank.model.Account;
import bank.model.Transaction;
import bank.repository.AccountRepository;
import bank.visitor.Visitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FreezeCommand implements TransactionCommand {
    private final Transaction transaction;
    private final AccountRepository repository;

    @Override
    public void execute() {
        Account account = repository.findById(transaction.getSourceAccountId());
        if (account == null) throw new RuntimeException("Account not found");

        account.getLock().lock();
        try {
            boolean newState = !account.isFrozen();
            account.setFrozen(newState);
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
    }
}