package bank.repository;

import bank.model.Account;

import java.util.UUID;

public interface AccountRepository {
    void save(Account account);

    Account findById(UUID id);

    void update(Account account);
}