package com.husseinsilver.store.utils;

public class Constants {

    // Firebase Collections
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_PRODUCTS = "products";
    public static final String COLLECTION_CARTS = "carts";
    public static final String COLLECTION_CART_ITEMS = "items";

    // Product Categories
    public static final String CATEGORY_BULLION = "bullion";
    public static final String CATEGORY_ACCESSORIES = "accessories";

    // Product Subcategories - Bullion
    public static final String SUBCATEGORY_BAR = "bar";
    public static final String SUBCATEGORY_ITALIAN = "italian";
    public static final String SUBCATEGORY_FRENCH = "french";
    public static final String SUBCATEGORY_SWISS = "swiss";

    // Product Subcategories - Accessories
    public static final String SUBCATEGORY_RING = "ring";
    public static final String SUBCATEGORY_BRACELET = "bracelet";
    public static final String SUBCATEGORY_CHAIN = "chain";

    // SharedPreferences
    public static final String PREFS_NAME = "hussein_silver_prefs";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_USER_NAME = "user_name";
    public static final String PREF_USER_EMAIL = "user_email";

    // Silver price
    public static final double TROY_OUNCE_TO_GRAM = 31.1034768;
    public static final double PRICE_MARGIN = 1.10;

    // Price refresh interval (milliseconds)
    public static final long PRICE_UPDATE_INTERVAL_MS = 60000;

    private Constants() {}
}
