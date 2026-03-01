package com.husseinsilver.store.models;

public class Product {
    private String id;
    private String name;
    private String description;
    private String category; // "bullion", "ounce", "accessories"
    private String subcategory; // "ring", "bracelet", "chain" or "italian", "french", "swiss"
    private double weightGrams;
    private double price; // calculated from silver price
    private String gender; // "male", "female", "unisex"

    public Product() {}

    public Product(String id, String name, String description, String category,
                   String subcategory, double weightGrams, double price, String gender) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        this.weightGrams = weightGrams;
        this.price = price;
        this.gender = gender;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }

    public double getWeightGrams() { return weightGrams; }
    public void setWeightGrams(double weightGrams) { this.weightGrams = weightGrams; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
