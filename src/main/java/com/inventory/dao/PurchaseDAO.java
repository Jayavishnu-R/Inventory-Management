package com.inventory.dao;


import com.inventory.model.Purchase;
import com.inventory.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {
    private Connection connection;

    // Constructor initializes the database connection
    public PurchaseDAO() {
        try {

            connection = DatabaseUtil.getConnection(); // Get connection from utility class
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("PostgreSQL Driver not found", e);
        }
    }


    public void addPurchase(Purchase purchase) {
        String sql = "INSERT INTO purchase (item_id, quantity, purchase_price, purchase_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purchase.getItemId());
            stmt.setInt(2, purchase.getQuantity());
            stmt.setDouble(3, purchase.getPurchasePrice());
            stmt.setTimestamp(4, purchase.getPurchaseDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Purchase getPurchaseById(int id) {
        String sql = "SELECT * FROM purchase WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Purchase purchase = new Purchase();
                purchase.setId(rs.getInt("id"));
                purchase.setItemId(rs.getInt("item_id"));
                purchase.setQuantity(rs.getInt("quantity"));
                purchase.setPurchasePrice(rs.getDouble("purchase_price"));
                purchase.setPurchaseDate(rs.getTimestamp("purchase_date"));
                return purchase;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM purchase";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Purchase purchase = new Purchase();
                purchase.setId(rs.getInt("id"));
                purchase.setItemId(rs.getInt("item_id"));
                purchase.setQuantity(rs.getInt("quantity"));
                purchase.setPurchasePrice(rs.getDouble("purchase_price"));
                purchase.setPurchaseDate(rs.getTimestamp("purchase_date"));
                purchases.add(purchase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchases;
    }


    public void updatePurchase(Purchase purchase) {
        String sql = "UPDATE purchase SET item_id = ?, quantity = ?, purchase_price = ?, purchase_date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purchase.getItemId());
            stmt.setInt(2, purchase.getQuantity());
            stmt.setDouble(3, purchase.getPurchasePrice());
            stmt.setTimestamp(4, purchase.getPurchaseDate());
            stmt.setInt(5, purchase.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deletePurchase(int id) {
        String sql = "DELETE FROM purchase WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}