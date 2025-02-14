package com.inventory.dao;


import com.inventory.model.Sales;
import com.inventory.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO{
    private Connection connection;

    // Constructor initializes the database connection
    public SalesDAO() {
        try {

            connection = DatabaseUtil.getConnection(); // Get connection from utility class
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addSale(Sales sale) {
        String sql = "INSERT INTO sales (invoice_id, item_id, quantity, sale_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sale.getInvoiceId());
            stmt.setInt(2, sale.getItemId());
            stmt.setInt(3, sale.getQuantity());
            stmt.setDouble(4, sale.getSalePrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Sales getSaleById(int id) {
        String sql = "SELECT * FROM sales WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Sales sale = new Sales();
                sale.setId(rs.getInt("id"));
                sale.setInvoiceId(rs.getInt("invoice_id"));
                sale.setItemId(rs.getInt("item_id"));
                sale.setQuantity(rs.getInt("quantity"));
                sale.setSalePrice(rs.getDouble("sale_price"));
//                sale.setSaleDate(rs.getTimestamp("sale_date"));
                return sale;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Sales> getAllSales() {
        List<Sales> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Sales sale = new Sales();
                sale.setId(rs.getInt("id"));
                sale.setInvoiceId(rs.getInt("invoice_id"));
                sale.setItemId(rs.getInt("item_id"));
                sale.setQuantity(rs.getInt("quantity"));
                sale.setSalePrice(rs.getDouble("sale_price"));
//                sale.setSaleDate(rs.getTimestamp("sale_date"));
                sales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }


    public void updateSale(Sales sale) {
        String sql = "UPDATE sales SET invoice_id = ?, item_id = ?, quantity = ?, sale_price = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sale.getInvoiceId());
            stmt.setInt(2, sale.getItemId());
            stmt.setInt(3, sale.getQuantity());
            stmt.setDouble(4, sale.getSalePrice());
            stmt.setInt(5, sale.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteSale(int id) {
        String sql = "DELETE FROM sales WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Sales> getSalesByInvoiceId(int invoiceId) throws SQLException {
        String query = "SELECT * FROM sales WHERE invoice_id = ?";
        List<Sales> salesList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Sales sale = new Sales();
                    sale.setId(rs.getInt("id"));
                    sale.setInvoiceId(rs.getInt("invoice_id"));
                    sale.setItemId(rs.getInt("item_id"));
                    sale.setQuantity(rs.getInt("quantity"));
                    sale.setSalePrice(rs.getDouble("sale_price"));
                    salesList.add(sale);
                }
            }
        }
        return salesList;
    }
}
