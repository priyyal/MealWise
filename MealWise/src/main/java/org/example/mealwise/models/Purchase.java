package org.example.mealwise.models;

import java.time.LocalDate;

public class Purchase {
    private int purchaseId;
    private int userId;
    private int groceryItemId;
    private int quantity;
    private LocalDate purchaseDate;

    public Purchase(int userId, int groceryItemId, int quantity, LocalDate purchaseDate) {
        this.userId = userId;
        this.groceryItemId = groceryItemId;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
    }

    public Purchase(int purchaseId, int userId, int groceryItemId, int quantity, LocalDate purchaseDate) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.groceryItemId = groceryItemId;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroceryItemId() {
        return groceryItemId;
    }

    public void setGroceryItemId(int groceryItemId) {
        this.groceryItemId = groceryItemId;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }
}
