package com.inventory.dao;


import com.inventory.model.Invoice;
import com.inventory.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private Connection connection;

    // Constructor initializes the database connection
    public InvoiceDAO() {
        try {

            connection = DatabaseUtil.getConnection(); // Get connection from utility class
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoice (invoice_date, total_amount) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, invoice.getInvoiceDate());
            stmt.setDouble(2, invoice.getTotalAmount());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoice WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("id"));
                invoice.setInvoiceDate(rs.getTimestamp("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                return invoice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("id"));
                invoice.setInvoiceDate(rs.getTimestamp("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }


    public void updateInvoice(Invoice invoice) {
        String sql = "UPDATE invoice SET invoice_date = ?, total_amount = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, invoice.getInvoiceDate());
            stmt.setDouble(2, invoice.getTotalAmount());
            stmt.setInt(3, invoice.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteInvoice(int id) {
        String sql = "DELETE FROM invoice WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
