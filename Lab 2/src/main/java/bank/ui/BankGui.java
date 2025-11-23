package bank.ui;

import bank.model.Account;
import bank.model.Transaction;
import bank.model.TransactionType;
import bank.model.User;
import bank.service.BankService;
import bank.observer.TransactionObserver;
import bank.service.processing.AsyncTransactionProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.UUID;

public class BankGui extends JFrame implements TransactionObserver {

    private final BankService bankService;

    private JTextField sourceIdField;
    private JTextField targetIdField;
    private JTextField amountField;
    private JTextArea logArea;

    private DefaultListModel<String> accountListModel;

    public BankGui(BankService bankService, AsyncTransactionProcessor processor) {
        this.bankService = bankService;
        processor.addObserver(this);
        initUI();
    }

    private void initUI() {
        setTitle("Genshin Bank System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setPreferredSize(new Dimension(320, 700));

        sourceIdField = createLabeledField("Source Account ID:", controlPanel);
        targetIdField = createLabeledField("Target Account ID (for Transfer):", controlPanel);
        amountField = createLabeledField("Amount:", controlPanel);

        controlPanel.add(Box.createVerticalStrut(20));

        JButton createAccBtn = createStyledButton("1. Create New Account");
        JButton depositBtn = createStyledButton("2. Deposit Funds");
        JButton withdrawBtn = createStyledButton("3. Withdraw Funds");
        JButton transferBtn = createStyledButton("4. Transfer Money");
        JButton freezeBtn = createStyledButton("5. Freeze/Unfreeze Account");
        freezeBtn.setBackground(new Color(200, 200, 255));
        JButton clearBtn = createStyledButton("Clear Form");
        clearBtn.setBackground(new Color(255, 200, 200));

        controlPanel.add(createAccBtn);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(depositBtn);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(withdrawBtn);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(transferBtn);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(freezeBtn);
        controlPanel.add(Box.createVerticalStrut(30));
        controlPanel.add(clearBtn);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(new Color(0, 255, 0));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Transaction Log"));

        accountListModel = new DefaultListModel<>();
        JList<String> accountList = new JList<>(accountListModel);
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(accountList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Active Accounts (Nick | ID(Double Click -> Source | Right Click -> Target) | [Balance])"));

        accountList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                if (index >= 0) {
                    String item = accountListModel.getElementAt(index);
                    String uuid = extractUuid(item);

                    if (SwingUtilities.isRightMouseButton(evt)) {
                        targetIdField.setText(uuid);
                    } else if (evt.getClickCount() == 2) {
                        sourceIdField.setText(uuid);
                    }
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, logScroll, listScroll);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.7);

        createAccBtn.addActionListener(e -> {
            try {
                String name = JOptionPane.showInputDialog(this, "Enter User Nickname:");
                if (name != null && !name.isEmpty()) {
                    User user = new User(name);
                    Account acc = bankService.createAccount(user);

                    String listEntry = formatListEntry(user.getNickname(), acc.getId(), BigDecimal.ZERO, false);
                    accountListModel.addElement(listEntry);

                    log("SYSTEM: Account created for " + name);
                    sourceIdField.setText(acc.getId().toString());
                }
            } catch (Exception ex) {
                log("ERROR: " + ex.getMessage());
            }
        });

        depositBtn.addActionListener(e -> submitTransaction(TransactionType.DEPOSIT));
        withdrawBtn.addActionListener(e -> submitTransaction(TransactionType.WITHDRAW));
        transferBtn.addActionListener(e -> submitTransaction(TransactionType.TRANSFER));

        clearBtn.addActionListener(e -> {
            sourceIdField.setText("");
            targetIdField.setText("");
            amountField.setText("");
        });

        freezeBtn.addActionListener(e -> {
            amountField.setText("0");
            submitTransaction(TransactionType.FREEZE);
        });

        add(controlPanel, BorderLayout.WEST);
        add(splitPane, BorderLayout.CENTER);

        log("SYSTEM: Welcome to Genshin Bank. Create an account to start.");
    }

    private void submitTransaction(TransactionType type) {
        try {
            String srcText = sourceIdField.getText().trim();
            if (srcText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Source ID is required!");
                return;
            }
            UUID sourceId = UUID.fromString(srcText);

            String amtText = amountField.getText().trim();
            if (amtText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Amount is required!");
                return;
            }
            BigDecimal amount = new BigDecimal(amtText);

            if (type != TransactionType.FREEZE && amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UUID targetId = null;
            if (type == TransactionType.TRANSFER) {
                String trgText = targetIdField.getText().trim();
                if (trgText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Target ID is required for transfer!");
                    return;
                }
                targetId = UUID.fromString(trgText);
            }

            Transaction tx = new Transaction(type, amount, sourceId, targetId);
            bankService.processTransaction(tx);

            log("SYSTEM: Sending " + type + "...");

            amountField.setText("");
            sourceIdField.setText("");
            if (type == TransactionType.TRANSFER) {
                targetIdField.setText("");
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Format: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            log("ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void onTransactionProcessed(Transaction tx, boolean success, String message) {
        SwingUtilities.invokeLater(() -> {
            String status = success ? "[SUCCESS]" : "[FAILED]";
            log(String.format("TX: %s | %s | %s", tx.getType(), status, message));

            if (success) {
                updateBalanceInList(tx.getSourceAccountId());
                if (tx.getType() == TransactionType.TRANSFER) {
                    updateBalanceInList(tx.getTargetAccountId());
                }
            }
        });
    }

    private void updateBalanceInList(UUID accountId) {
        if (accountId == null) return;
        for (int i = 0; i < accountListModel.getSize(); i++) {
            String entry = accountListModel.getElementAt(i);
            if (entry.contains(accountId.toString())) {
                String[] parts = entry.split(" \\| ");
                String nickname = parts[0];
                Account updatedAccount = bankService.getAccount(accountId);

                if (updatedAccount != null) {
                    String newEntry = formatListEntry(
                            nickname,
                            accountId,
                            updatedAccount.getBalance(),
                            updatedAccount.isFrozen()
                    );
                    accountListModel.set(i, newEntry);
                }
                break;
            }
        }
    }

    private String formatListEntry(String nickname, UUID id, BigDecimal balance, boolean isFrozen) {
        String status = isFrozen ? " [FROZEN] ❄️" : "";
        return String.format("%s | %s | [%s]%s", nickname, id, balance.toPlainString(), status);
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private String extractUuid(String listEntry) {
        String[] parts = listEntry.split(" \\| ");
        if (parts.length >= 2) {
            return parts[1];
        }
        return listEntry;
    }

    private JTextField createLabeledField(String labelText, JPanel panel) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(field);
        panel.add(Box.createVerticalStrut(5));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        return btn;
    }
}