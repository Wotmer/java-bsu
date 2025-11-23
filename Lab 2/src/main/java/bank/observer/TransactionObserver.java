package bank.observer;

import bank.model.Transaction;

public interface TransactionObserver {
    void onTransactionProcessed(Transaction tx, boolean success, String message);
}