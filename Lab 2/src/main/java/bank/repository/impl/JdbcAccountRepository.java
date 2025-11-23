package bank.repository.impl;

import bank.config.DatabaseConnection;
import bank.model.Account;
import bank.repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class JdbcAccountRepository implements AccountRepository {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Account account) {
        String sql = "INSERT INTO accounts (id, user_id, balance, is_frozen) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, account.getId());
            ps.setObject(2, account.getUserId());
            ps.setBigDecimal(3, account.getBalance());
            ps.setBoolean(4, account.isFrozen());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account", e);
        }
    }

    @Override
    public Account findById(UUID id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        (UUID) rs.getObject("id"),
                        (UUID) rs.getObject("user_id"),
                        rs.getBigDecimal("balance"),
                        rs.getBoolean("is_frozen")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding account", e);
        }
        return null;
    }

    @Override
    public void update(Account account) {
        String sql = "UPDATE accounts SET balance = ?, is_frozen = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, account.getBalance());
            ps.setBoolean(2, account.isFrozen());
            ps.setObject(3, account.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account", e);
        }
    }
}