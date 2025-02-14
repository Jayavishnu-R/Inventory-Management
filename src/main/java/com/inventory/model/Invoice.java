package com.inventory.model;
import java.sql.Timestamp;
import java.util.List;

public class Invoice {
    private int id; // Primary Key
    private Timestamp invoiceDate; // Defaults to current timestamp
    private double totalAmount; // Total amount for the invoice
    private List<Sales> sales;


    public Invoice(){

    }

    public Invoice(int id, Timestamp invoiceDate, double totalAmount) {
        this.id = id;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Timestamp invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Sales> getSales() {
        return sales;
    }

    public void setSales(List<Sales> sales) {
        this.sales = sales;
    }
}