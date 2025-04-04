package org.example.mealwise.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ShoppingItem {
    private final String name;
    private final BooleanProperty selected;
    private final IntegerProperty quantity;
    private final String imageUrl;
    private final DoubleProperty weight;
    private final String unit;

    public ShoppingItem(String name, String imageUrl, double weight, int quantity, String unit) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.weight = new SimpleDoubleProperty(weight);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unit = unit;
        this.selected = new SimpleBooleanProperty(false);
    }

    // Optional constructor for legacy uses like new ShoppingItem("Milk")
    public ShoppingItem(String name) {
        this(name, "", 0.0, 1, "");
    }

    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getUnit() { return unit; }

    public BooleanProperty selectedProperty() { return selected; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty weightProperty() { return weight; }

    public void increaseQuantity() {
        quantity.set(quantity.get() + 1);
    }

    public void decreaseQuantity() {
        if (quantity.get() > 1) {
            quantity.set(quantity.get() - 1);
        }
    }
}


