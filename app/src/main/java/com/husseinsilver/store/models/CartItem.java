package com.husseinsilver.store.models;

public class CartItem {
    private String id;
    private String productId;
    private String productName;
    private double price;
    private int quantity;
    private double weightGrams;

    public CartItem() {}

    public CartItem(String productId, String productName, double price,
                    int quantity, double weightGrams) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.weightGrams = weightGrams;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getWeightGrams() { return weightGrams; }
    public void setWeightGrams(double weightGrams) { this.weightGrams = weightGrams; }

    public double getTotalPrice() { return price * quantity; }
}
