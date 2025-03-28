package com.rafael.vr.dao;

import com.rafael.vr.config.MysqlConnection;
import com.rafael.vr.model.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDao {

    public Sale create(Sale sale){
        String saleSql = "INSERT INTO sales(customer_code, total_price) VALUES (?, ?)";
        String itemSql = "INSERT INTO sale_item(sale_code, product_code, product_description, quantity, unit_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement saleStmt = conn.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {

            saleStmt.setInt(1, sale.getCustomerCode());
            saleStmt.setBigDecimal(2, sale.getTotalPrice());
            saleStmt.executeUpdate();

            ResultSet rs = saleStmt.getGeneratedKeys();
            if (rs.next()) {
                sale.setCode(rs.getInt(1));
            }

            for (Sale.SaleItem item : sale.getItems()) {
                itemStmt.setInt(1, sale.getCode());
                itemStmt.setInt(2, item.getProductCode());
                itemStmt.setString(3, item.getProductDescription());
                itemStmt.setInt(4, item.getQuantity());
                itemStmt.setBigDecimal(5, item.getUnitPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            return sale;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente", e);
        }
    }

    public Sale getByCode(int code) {
        String saleSql = "SELECT code, customer_code, total_price FROM sales WHERE code = ?";
        String itemSql = "SELECT product_code, product_description, quantity, unit_price FROM sale_item WHERE sale_code = ?";

        try (Connection conn = MysqlConnection.getConnection();
             PreparedStatement saleStmt = conn.prepareStatement(saleSql);
             PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {

            saleStmt.setInt(1, code);
            ResultSet saleRs = saleStmt.executeQuery();

            if (!saleRs.next()) {
                return null;
            }

            Sale sale = new Sale();
            sale.setCode(saleRs.getInt("code"));
            sale.setCustomerCode(saleRs.getInt("customer_code"));
            sale.setTotalPrice(saleRs.getBigDecimal("total_price"));

            itemStmt.setInt(1, code);
            ResultSet itemRs = itemStmt.executeQuery();
            List<Sale.SaleItem> items = new ArrayList<>();

            while (itemRs.next()) {
                Sale.SaleItem item = new Sale.SaleItem(
                        itemRs.getInt("product_code"),
                        itemRs.getString("product_description"),
                        itemRs.getInt("quantity"),
                        itemRs.getBigDecimal("unit_price")
                );
                items.add(item);
            }

            sale.setItems(items);
            return sale;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar venda", e);
        }
    }


}
