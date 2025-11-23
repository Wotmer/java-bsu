package bank.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Transaction {
    private final UUID id;
    private final LocalDateTime timestamp;
    private final TransactionType type;
    private final BigDecimal amount;
    private final UUID sourceAccountId;
    private final UUID targetAccountId;

    public Transaction(TransactionType type, BigDecimal amount, UUID sourceAccountId, UUID targetAccountId) {
        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
    }
}