package bank.service;

import bank.model.Account;
import bank.model.Transaction;
import bank.model.TransactionType;
import bank.repository.impl.InMemoryAccountRepository;
import bank.service.processing.AsyncTransactionProcessor;
import bank.service.processing.factory.CommandFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class AsyncProcessorTest {

    @Test
    void testConcurrency() throws InterruptedException {
        InMemoryAccountRepository repo = new InMemoryAccountRepository();
        AsyncTransactionProcessor processor = new AsyncTransactionProcessor();
        CommandFactory factory = new CommandFactory(repo);

        UUID userId = UUID.randomUUID();
        Account account = new Account(userId);
        account.setBalance(BigDecimal.valueOf(1000));
        repo.save(account);

        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                Transaction tx = new Transaction(TransactionType.DEPOSIT, BigDecimal.ONE, account.getId(), null);
                processor.submit(factory.createCommand(tx));
                latch.countDown();
            }).start();
        }

        latch.await();
        TimeUnit.SECONDS.sleep(2);

        // 1000 + 100 * 1 = 1100
        Assertions.assertEquals(0, BigDecimal.valueOf(1100).compareTo(repo.findById(account.getId()).getBalance()));
    }
}