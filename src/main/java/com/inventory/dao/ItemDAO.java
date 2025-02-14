package com.inventory.dao;

import com.inventory.model.Item;
import com.inventory.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private Connection connection;

    public ItemDAO() throws SQLException, ClassNotFoundException {


            connection = DatabaseUtil.getConnection();

    }

    // Method to get all items
    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM items";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id")); // Ensure 'id' is included
                item.setName(rs.getString("name"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
        }
        return items;
    }

    // Method to get an item by ID
    public Item getItemById(int id) throws SQLException {
        String query = "SELECT * FROM items WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getDouble("price"));
                    return item;
                }
            }
        }
        return null; // Return null if no item is found
    }

    // Method to add a new item
    public void addItem(Item item) throws SQLException {
        String query = "INSERT INTO items (name, quantity, price) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getQuantity());
            pstmt.setDouble(3, item.getPrice());
            pstmt.executeUpdate();
        }
    }

    // Method to update an existing item (PUT)
    public boolean updateItem(Item updatedItem) throws SQLException {
        String query = "UPDATE items SET name = ?, quantity = ?, price = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, updatedItem.getName());
            pstmt.setInt(2, updatedItem.getQuantity());
            pstmt.setDouble(3, updatedItem.getPrice());
            pstmt.setInt(4, updatedItem.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Method to partially update an item (PATCH)
    public boolean partialUpdateItem(int id, Item partialItem) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("UPDATE items SET ");
        List<Object> params = new ArrayList<>();

        if (partialItem.getName() != null) {
            queryBuilder.append("name = ?, ");
            params.add(partialItem.getName());
        }
        if (partialItem.getQuantity() > 0) { // Avoid using '0' as a valid update
            queryBuilder.append("quantity = ?, ");
            params.add(partialItem.getQuantity());
        }
        if (partialItem.getPrice() > 0) { // Avoid using '0.0' as a valid update
            queryBuilder.append("price = ?, ");
            params.add(partialItem.getPrice());
        }

        if (params.isEmpty()) {
            return false; // No fields to update
        }

        queryBuilder.deleteCharAt(queryBuilder.length() - 2); // Remove last comma and space
        queryBuilder.append("WHERE id = ?");
        params.add(id);

        try (PreparedStatement pstmt = connection.prepareStatement(queryBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            return pstmt.executeUpdate() > 0;
        }
    }

    // Method to delete an item
    public boolean deleteItem(int id) throws SQLException {
        String query = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}