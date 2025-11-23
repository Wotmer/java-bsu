package bank.visitor;

import bank.service.processing.command.DepositCommand;
import bank.service.processing.command.TransferCommand;
import bank.service.processing.command.WithdrawCommand;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransactionReportVisitor implements Visitor {
    private BigDecimal totalDeposits = BigDecimal.ZERO;
    private BigDecimal totalWithdrawals = BigDecimal.ZERO;
    private int transferCount = 0;

    @Override
    public void visit(DepositCommand command) {
        totalDeposits = totalDeposits.add(command.getTransaction().getAmount());
    }

    @Override
    public void visit(WithdrawCommand command) {
        totalWithdrawals = totalWithdrawals.add(command.getTransaction().getAmount());
    }

    @Override
    public void visit(TransferCommand command) {
        transferCount++;
    }
}