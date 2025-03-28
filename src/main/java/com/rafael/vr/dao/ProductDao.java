package com.rafael.vr.dao;

import com.rafael.vr.config.MysqlConnection;
import com.rafael.vr.model.Product;

import java.sql.*;

public class ProductDao {

    public Product create(Product product) {
        String sql = "INSERT INTO products(description, price) VALUES (?, ?)";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))  {

            stmt.setString(1, product.getDescription());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                product.setCode(rs.getInt(1));
            }

            return product;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar produto", e);
        }
    }

    public void update(Product product) {
        String sql = "UPDATE products SET description = ?, price = ? WHERE code = ?";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getDescription());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getCode());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto", e);
        }
    }

    public Product findBycode(Integer code) {
        String sql = "SELECT * FROM products WHERE code = ?";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, code);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Product.builder()
                        .code(rs.getInt("code"))
                        .description(rs.getString("description"))
                        .price(rs.getBigDecimal("price"))
                        .build();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por ID", e);
        }

        return null;
    }

    public void delete(Integer code) {
        String sql = "DELETE FROM products WHERE code = ?";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, code);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar produto", e);
        }
    }

}
