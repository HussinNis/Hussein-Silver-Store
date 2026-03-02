package com.husseinsilver.store.models;

public class CartItem {
    private String id;
    private String productId;
    private String productName;
    private double priceIls;
    private int quantity;
    private double weightGrams;
    private boolean isWeighted;
    private String category;

    public CartItem() {}

    public CartItem(String productId, String productName, double priceIls,
                    int quantity, double weightGrams, boolean isWeighted, String category) {
        this.productId = productId;
        this.productName = productName;
        this.priceIls = priceIls;
        this.quantity = quantity;
        this.weightGrams = weightGrams;
        this.isWeighted = isWeighted;
        this.category = category;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPriceIls() { return priceIls; }
    public void setPriceIls(double priceIls) { this.priceIls = priceIls; }

    /** @deprecated Use {@link #getPriceIls()} */
    public double getPrice() { return priceIls; }
    /** @deprecated Use {@link #setPriceIls(double)} */
    public void setPrice(double price) { this.priceIls = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getWeightGrams() { return weightGrams; }
    public void setWeightGrams(double weightGrams) { this.weightGrams = weightGrams; }

    public boolean isWeighted() { return isWeighted; }
    public void setWeighted(boolean weighted) { isWeighted = weighted; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getTotalPrice() { return priceIls * quantity; }
}
