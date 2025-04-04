package org.example.mealwise.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.mealwise.models.GroceryItem;

import java.util.function.BiConsumer;

public class ShoppingListCellController {
    @FXML private Label itemName;
    @FXML private Label imagePlaceholder;
    @FXML private Button minusBtn, plusBtn, addBtn;
    @FXML private Label quantityLabel;

    private GroceryItem item;
    private int quantity = 0;
    private BiConsumer<GroceryItem, Integer> onAddCallback;

    public void setData(GroceryItem item, BiConsumer<GroceryItem, Integer> onAddCallback) {
        this.item = item;
        this.onAddCallback = onAddCallback;
        itemName.setText(item.getName());
        quantityLabel.setText("0×");

        plusBtn.setOnAction(e -> {
            quantity++;
            quantityLabel.setText(quantity + "×");
        });

        minusBtn.setOnAction(e -> {
            if (quantity > 0) quantity--;
            quantityLabel.setText(quantity + "×");
        });

        addBtn.setOnAction(e -> {
            if (quantity > 0) {
                onAddCallback.accept(item, quantity);
                quantity = 0;
                quantityLabel.setText("0×");
            }
        });
    }
}
