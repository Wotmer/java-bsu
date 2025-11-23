package bank.service.processing.command;

import bank.model.Transaction;
import bank.visitor.Visitor;

public interface TransactionCommand {
    void execute();

    Transaction getTransaction();

    void accept(Visitor visitor);
}