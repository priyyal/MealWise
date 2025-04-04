package org.example.mealwise.models;

import java.time.LocalDate;

public class Inventory {
    private int inventory_id;
    private int user_id;
    private int item_id;
    private int quantity;
    private LocalDate expiry_date;
    private int purchase_batch_id;
    private GroceryItem groceryItem;
    private String groceryName;
    private String customName;

    public Inventory(int inventory_id, int user_id, int item_id, int quantity, LocalDate expiry_date, int purchase_batch_id) {
        this.inventory_id = inventory_id;
        this.user_id = user_id;
        this.item_id = item_id;
        this.quantity = quantity;
        this.expiry_date = expiry_date;
        this.purchase_batch_id = purchase_batch_id;
    }

    public Inventory(int user_id, int item_id, int quantity, LocalDate expiry_date, int purchase_batch_id) {
        this.user_id = user_id;
        this.item_id = item_id;
        this.quantity = quantity;
        this.expiry_date = expiry_date;
        this.purchase_batch_id = purchase_batch_id;
    }

    public Inventory(int inventoryId, String groceryName, int userId,  int quantity, LocalDate expiryDate) {
        this.inventory_id = inventoryId;
        this.user_id = userId;
        this.groceryName = groceryName;
        this.quantity = quantity;
        this.expiry_date = expiryDate;
    }

    public Inventory(int inventoryId, int itemId, String name, int quantity, LocalDate expiryDate) {
        this.inventory_id = inventoryId;
        this.groceryName = name;
        this.item_id = itemId;
        this.quantity = quantity;
        this.expiry_date = expiryDate;
    }

    public Inventory(int inventoryId, int itemId, String name, int userId, int quantity, LocalDate expiryDate) {
        this.inventory_id = inventoryId;
        this.groceryName = name;
        this.item_id = itemId;
        this.quantity = quantity;
        this.expiry_date = expiryDate;
    }

    public int getInventoryId() {
        return inventory_id;
    }

    public void setInventoryId(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getItemId() {
        return item_id;
    }

    public void setItemId(int item_id) {
        this.item_id = item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiry_date;
    }

    public void setExpiryDate(LocalDate expiry_date) {
        this.expiry_date = expiry_date;
    }

    public int getPurchaseBatchId() {
        return purchase_batch_id;
    }

    public void setPurchaseBatchId(int purchase_batch_id) {
        this.purchase_batch_id = purchase_batch_id;
    }

    public String getGroceryName() {
        return groceryName;
    }

    public void setGroceryName(String groceryName) {
        this.groceryName = groceryName;
    }

}