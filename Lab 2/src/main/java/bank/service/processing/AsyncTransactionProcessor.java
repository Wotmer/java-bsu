package bank.service.processing;

import bank.model.Transaction;
import bank.observer.TransactionObserver;
import bank.service.processing.command.TransactionCommand;
import bank.visitor.Visitor;

import java.util.List;
import java.util.concurrent.*;

public class AsyncTransactionProcessor {
    private final BlockingQueue<TransactionCommand> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executor;
    private final List<TransactionObserver> observers = new CopyOnWriteArrayList<>();
    private final List<TransactionCommand> commandHistory = new CopyOnWriteArrayList<>();

    public AsyncTransactionProcessor() {
        this.executor = Executors.newFixedThreadPool(4);
        startProcessing();
    }

    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void submit(TransactionCommand command) {
        queue.offer(command);
    }

    private void startProcessing() {
        for (int i = 0; i < 4; i++) {
            executor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        TransactionCommand cmd = queue.take();
                        try {
                            cmd.execute();
                            commandHistory.add(cmd);
                            notifyObservers(cmd.getTransaction(), true, "OK");
                        } catch (Exception e) {
                            notifyObservers(cmd.getTransaction(), false, e.getMessage());
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }

    private void notifyObservers(Transaction tx, boolean success, String msg) {
        for (TransactionObserver obs : observers) {
            obs.onTransactionProcessed(tx, success, msg);
        }
    }

    public void acceptVisitor(Visitor visitor) {
        for (TransactionCommand cmd : commandHistory) {
            cmd.accept(visitor);
        }
    }
}