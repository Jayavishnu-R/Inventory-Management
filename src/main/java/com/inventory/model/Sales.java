package com.inventory.model;



public class Sales {
    private int id; // Primary Key
    private int invoiceId; // Foreign Key referencing Invoice
    private int itemId; // Foreign Key referencing Items
    private int quantity; // Quantity sold
    private double salePrice; // Price at which the item was sold

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", invoiceId=" + invoiceId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", salePrice=" + salePrice +
                '}';
    }
}