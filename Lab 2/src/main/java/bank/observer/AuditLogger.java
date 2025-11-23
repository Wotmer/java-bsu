package bank.observer;

import bank.model.Transaction;

public class AuditLogger implements TransactionObserver {
    @Override
    public void onTransactionProcessed(Transaction tx, boolean success, String message) {
        String status = success ? "SUCCESS" : "FAILED";
        System.out.printf("[AUDIT] Tx: %s | Type: %s | Status: %s | Msg: %s%n",
                tx.getId(), tx.getType(), status, message);
    }
}