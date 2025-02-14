package com.inventory.model;
import java.sql.Timestamp;

public class Item {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private Timestamp updated_at;


    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) {
        this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}