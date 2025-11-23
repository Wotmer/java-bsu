package bank.service.processing.command;

import bank.model.Account;
import bank.model.Transaction;
import bank.repository.AccountRepository;
import bank.visitor.Visitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransferCommand implements TransactionCommand {
    private final Transaction transaction;
    private final AccountRepository repository;

    @Override
    public void execute() {
        Account from = repository.findById(transaction.getSourceAccountId());
        Account to = repository.findById(transaction.getTargetAccountId());

        if (from == null || to == null) throw new RuntimeException("One of the accounts not found");

        Account first = from.getId().compareTo(to.getId()) < 0 ? from : to;
        Account second = from.getId().compareTo(to.getId()) < 0 ? to : from;

        first.getLock().lock();
        try {
            second.getLock().lock();
            try {
                if (from.isFrozen() || to.isFrozen()) throw new IllegalStateException("Account frozen");
                if (from.getBalance().compareTo(transaction.getAmount()) < 0)
                    throw new IllegalStateException("Insufficient funds");

                from.setBalance(from.getBalance().subtract(transaction.getAmount()));
                to.setBalance(to.getBalance().add(transaction.getAmount()));

                repository.update(from);
                repository.update(to);
            } finally {
                second.getLock().unlock();
            }
        } finally {
            first.getLock().unlock();
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