package bank.repository.impl;

import bank.model.Account;
import bank.repository.AccountRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<UUID, Account> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Account account) {
        storage.put(account.getId(), account);
    }

    @Override
    public Account findById(UUID id) {
        return storage.get(id);
    }

    @Override
    public void update(Account account) {
        storage.put(account.getId(), account);
    }
}