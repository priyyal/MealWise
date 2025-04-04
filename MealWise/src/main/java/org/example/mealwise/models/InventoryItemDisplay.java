package org.example.mealwise.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class InventoryItemDisplay {
    private final SimpleIntegerProperty serialNumber;
    private final int inventoryId;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty expiryDate;
    private final boolean isExpiryEditable;

    public InventoryItemDisplay(int serialNumber, int inventoryId, String name, int quantity, String expiryDate, boolean isExpiryEditable) {
        this.serialNumber = new SimpleIntegerProperty(serialNumber);
        this.inventoryId = inventoryId;
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.expiryDate = new SimpleStringProperty(expiryDate);
        this.isExpiryEditable = isExpiryEditable;
    }

    public int getInventoryId() { return inventoryId; }
    public int getSerialNumber() { return serialNumber.get(); }
    public String getName() { return name.get(); }
    public int getQuantity() { return quantity.get(); }
    public LocalDate getExpiryDate() {
        return LocalDate.parse(expiryDate.get());
    }


    public void setName(String name) { this.name.set(name); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public void setExpiryDate(String expiryDate) { this.expiryDate.set(expiryDate); }

    public SimpleIntegerProperty serialNumberProperty() { return serialNumber; }
    public SimpleStringProperty nameProperty() { return name; }
    public SimpleIntegerProperty quantityProperty() { return quantity; }
    public SimpleStringProperty expiryDateProperty() { return expiryDate; }

    public boolean isExpiryEditable() { return isExpiryEditable; }
}
