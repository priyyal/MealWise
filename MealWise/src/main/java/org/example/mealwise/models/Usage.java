package org.example.mealwise.models;

import java.time.LocalDate;

public class Usage {
    private int usageId;
    private int userId;
    private int groceryItemId;
    private int usageQuantity;
    private LocalDate usageDate;

    public Usage(int usage_id, int user_id, int grocery_item_id, int usage_quantity, LocalDate usage_date) {
        this.usageId = usage_id;
        this.userId = user_id;
        this.groceryItemId = grocery_item_id;
        this.usageQuantity = usage_quantity;
        this.usageDate = usage_date;
    }

    public Usage(int userId, int groceryItemId, int usageQuantity, LocalDate usageDate) {
        this.userId = userId;
        this.groceryItemId = groceryItemId;
        this.usageQuantity = usageQuantity;
        this.usageDate = usageDate;
    }

    public int getUsageId() {
        return usageId;
    }

    public void setUsageId(int usageId) {
        this.usageId = usageId;
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

    public int getUsageQuantity() {
        return usageQuantity;
    }

    public void setUsageQuantity(int usageQuantity) {
        this.usageQuantity = usageQuantity;
    }

    public LocalDate getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    public String getItemName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
