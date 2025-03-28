package com.rafael.vr.dao;

import com.rafael.vr.config.MysqlConnection;
import com.rafael.vr.model.Customer;

import java.sql.*;

public class CustomerDao {

    public Customer create(Customer customer) {
        String sql = "INSERT INTO customers(name, purchase_limit, closing_day) VALUES (?, ?, ?)";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))  {

            stmt.setString(1, customer.getName());
            stmt.setBigDecimal(2, customer.getPurchaseLimit());
            stmt.setInt(3, customer.getClosingDay());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                customer.setCode(rs.getInt(1));
            }

            return customer;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente", e);
        }
    }

    public Customer findBycode(Integer code) {
        String sql = "SELECT * FROM customers WHERE code = ?";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, code);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Customer.builder()
                        .code(rs.getInt("code"))
                        .name(rs.getString("name"))
                        .closingDay(rs.getInt("closing_day"))
                        .purchaseLimit(rs.getBigDecimal("purchase_limit"))
                        .build();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID", e);
        }

        return null;
    }

    public void update(Customer customer) {
        String sql = "UPDATE customers SET name = ?, closing_day = ?, purchase_limit = ? WHERE code = ?";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getName());
            stmt.setInt(2, customer.getClosingDay());
            stmt.setBigDecimal(3, customer.getPurchaseLimit());
            stmt.setInt(4, customer.getCode());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }
    }

    public void delete(Integer code) {
        String sql = "DELETE FROM customers WHERE code = ?";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, code);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente", e);
        }
    }


}
