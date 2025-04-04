package org.example.mealwise.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SuggestionItem {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty quantity;

    public SuggestionItem(String name) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(0);
    }

    public String getName() { return name.get(); }
    public int getQuantity() { return quantity.get(); }

    public void setQuantity(int q) { this.quantity.set(q); }

    public SimpleIntegerProperty quantityProperty() { return quantity; }
    public SimpleStringProperty nameProperty() { return name; }
}