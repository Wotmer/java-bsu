package bank.visitor;

import bank.service.processing.command.DepositCommand;
import bank.service.processing.command.TransferCommand;
import bank.service.processing.command.WithdrawCommand;

public interface Visitor {
    void visit(DepositCommand command);

    void visit(WithdrawCommand command);

    void visit(TransferCommand command);
}