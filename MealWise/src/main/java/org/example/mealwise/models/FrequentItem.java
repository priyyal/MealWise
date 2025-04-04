package org.example.mealwise.models;

public class FrequentItem {
    private int itemId;
    private String itemName;
    private int usageCount;
    
    public FrequentItem(int itemId, String itemName, int usageFrequency) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.usageCount = usageFrequency;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getUsageFrequency() {
        return usageCount;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setUsageFrequency(int usageFrequency) {
        this.usageCount = usageFrequency;
    }
}
