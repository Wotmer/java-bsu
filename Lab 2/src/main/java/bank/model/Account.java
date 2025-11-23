package bank.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
@ToString
public class Account {
    private UUID id;
    private UUID userId;
    private BigDecimal balance;
    private boolean isFrozen;

    @ToString.Exclude
    private final Lock lock = new ReentrantLock();

    public Account(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.balance = BigDecimal.ZERO;
        this.isFrozen = false;
    }

    public Account(UUID id, UUID userId, BigDecimal balance, boolean isFrozen) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.isFrozen = isFrozen;
    }
}