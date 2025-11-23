package bank;

import bank.repository.AccountRepository;
import bank.repository.impl.JdbcAccountRepository;
import bank.service.BankService;
import bank.observer.AuditLogger;
import bank.service.processing.AsyncTransactionProcessor;
import bank.ui.BankGui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AccountRepository repository = new JdbcAccountRepository();
        AsyncTransactionProcessor processor = new AsyncTransactionProcessor();
        processor.addObserver(new AuditLogger());

        BankService service = new BankService(repository, processor);

        SwingUtilities.invokeLater(() -> {
            BankGui gui = new BankGui(service, processor);
            gui.setVisible(true);
        });
    }
}