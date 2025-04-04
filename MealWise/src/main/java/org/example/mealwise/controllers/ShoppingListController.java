package org.example.mealwise.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.mealwise.dao.UsageDAO;
import org.example.mealwise.models.Inventory;
import org.example.mealwise.models.ShoppingItem;
import org.example.mealwise.services.InventoryManager;
import org.example.mealwise.services.ShoppingListService;
import org.example.mealwise.utils.SessionManager;

public class ShoppingListController {

    @FXML private ListView<ShoppingItem> shoppingListView;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML public  TextField itemTextField;
    @FXML private Label toastLabel;
    @FXML private ToggleButton darkModeToggle;
    @FXML private CheckBox checkAllBox;
    @FXML private Scene scene;
    @FXML
    private HBox recommendedItemsBox;


    private final InventoryManager inventoryManager = new InventoryManager();
    private final ShoppingListService shoppingListService = new ShoppingListService(new UsageDAO());



    private final List<ShoppingItem> recentlyRemoved = new ArrayList<>();

    @FXML
private void handleUndoRemove() {
    if (recentlyRemoved.isEmpty()) {
        showToast("⚠️ Nothing to undo");
        return;
    }

    shoppingList.addAll(recentlyRemoved);
    showToast("↩️ Restored " + recentlyRemoved.size() + " item(s)");
    recentlyRemoved.clear();
}
@FXML private Label titleLabel;
@FXML private Label iconLabel;

    private final ObservableList<ShoppingItem> shoppingList = FXCollections.observableArrayList();

@FXML
public void initialize() {
    loadSmartRecommendations();
    quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

    // Sample dummy data
    shoppingList.addAll(
            new ShoppingItem("Milk"),
            new ShoppingItem("Eggs"),
            new ShoppingItem("Apples")
    );

    // Sorting unchecked items first, alphabetically
    SortedList<ShoppingItem> sortedList = new SortedList<>(shoppingList);
    sortedList.setComparator((a, b) -> {
        boolean aChecked = a.selectedProperty().get();
        boolean bChecked = b.selectedProperty().get();

        if (aChecked == bChecked) {
            return a.getName().compareToIgnoreCase(b.getName());
        } else {
            return Boolean.compare(aChecked, bChecked); // unchecked first
        }
    });

    // Auto-resort when item is checked/unchecked
    shoppingList.forEach(item -> {
        item.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            sortedList.setComparator(sortedList.getComparator());
        });
    });

    shoppingListView.setItems(sortedList);

    // Custom ListCell for each shopping item
    shoppingListView.setCellFactory(listView -> new ListCell<>() {
        private final CheckBox checkBox = new CheckBox();
        private final Label nameLabel = new Label();
        private final Label quantityLabel = new Label();
        private final VBox textBox = new VBox(nameLabel, quantityLabel);
        private final HBox content = new HBox(10, checkBox, textBox);
        private ShoppingItem currentItem;

        {
            content.setStyle("-fx-padding: 8 12; -fx-alignment: CENTER_LEFT;");
            nameLabel.getStyleClass().add("item-name");
            quantityLabel.getStyleClass().add("item-quantity");
            getStyleClass().add("custom-cell");

            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (currentItem != null) {
                    currentItem.selectedProperty().set(newVal);
                    updateCheckAllBox();
                    updateStrikethroughStyle(currentItem);
                }
            });
        }

        private void updateStrikethroughStyle(ShoppingItem item) {
            boolean isChecked = item.selectedProperty().get();
            if (isChecked) {
                nameLabel.getStyleClass().add("strikethrough");
                quantityLabel.getStyleClass().add("strikethrough");
            } else {
                nameLabel.getStyleClass().remove("strikethrough");
                quantityLabel.getStyleClass().remove("strikethrough");
            }
        }

        @Override
        protected void updateItem(ShoppingItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                currentItem = null;
                setGraphic(null);
            } else {
                currentItem = item;
                nameLabel.setText(item.getName());
                quantityLabel.setText("x" + item.quantityProperty().get());

                checkBox.setSelected(item.selectedProperty().get());
                updateStrikethroughStyle(item);
                setGraphic(content);
            }
        }
    });

    // 🔥 Animate title label (optional)
    titleLabel.setOpacity(0);
    titleLabel.setTranslateX(-40);

    FadeTransition fade = new FadeTransition(Duration.millis(600), titleLabel);
    fade.setFromValue(0);
    fade.setToValue(1);

    TranslateTransition slide = new TranslateTransition(Duration.millis(600), titleLabel);
    slide.setFromX(-40);
    slide.setToX(0);

    fade.play();
    slide.play();
}


    @FXML void handleAddItem() {
        String itemName = itemTextField.getText().trim();
        int quantity = quantitySpinner.getValue();

        if (itemName.isEmpty()) {
            showToast("⚠️ Item name is empty.");
            return;
        }

        for (ShoppingItem item : shoppingList) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.quantityProperty().set(item.quantityProperty().get() + quantity);
                shoppingListView.refresh();
                showToast("🔁 Updated quantity of " + itemName);
                return;
            }
        }

        ShoppingItem newItem = new ShoppingItem(itemName);
        newItem.quantityProperty().set(quantity);
        shoppingList.add(newItem);
        showToast("✅ " + itemName + " x" + quantity + " added!");
        itemTextField.clear();
    }

    @FXML
    private void handleRemoveItem() {
        List<ShoppingItem> toRemove = new ArrayList<>();
        for (ShoppingItem item : shoppingList) {
            if (item.selectedProperty().get()) {
                toRemove.add(item);
            }
        }
        shoppingList.removeAll(toRemove);
        showToast("🗑️ Removed selected items");
    }

    private void handleAddRecommended(String itemName, int quantity) {
        for (ShoppingItem item : shoppingList) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.quantityProperty().set(item.quantityProperty().get() + quantity);
                shoppingListView.refresh();
                showToast("🔁 Updated quantity of " + itemName);
                return;
            }
        }

        ShoppingItem newItem = new ShoppingItem(itemName);
        newItem.quantityProperty().set(quantity);
        shoppingList.add(newItem);
        showToast("✅ " + itemName + " x" + quantity + " added!");
    }


    @FXML
    private void handleAddRecommendedItem(javafx.event.ActionEvent event) {
        Hyperlink clickedLink = (Hyperlink) event.getSource();
        String itemName = clickedLink.getText().replaceAll("[^a-zA-Z ]", "").trim();
        int quantity = quantitySpinner.getValue();

        for (ShoppingItem item : shoppingList) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.quantityProperty().set(item.quantityProperty().get() + quantity);
                shoppingListView.refresh();
                showToast("🔁 Updated quantity of " + itemName);
                return;
            }
        }

        ShoppingItem newItem = new ShoppingItem(itemName);
        newItem.quantityProperty().set(quantity);
        shoppingList.add(newItem);
        showToast("✅ " + itemName + " x" + quantity + " added!");
    }

    private void loadSmartRecommendations() {
        int userId = SessionManager.getUserId();
        List<Inventory> inventory = inventoryManager.getUserInventory(userId);

        List<ShoppingItem> recommended = shoppingListService.getRecommendedItems(userId, inventory);

        recommendedItemsBox.getChildren().clear();

        for (ShoppingItem item : recommended) {
            Hyperlink pill = new Hyperlink("🍽️ " + item.getName());
            pill.getStyleClass().add("recommend-pill");

            pill.setOnAction(e -> {
                int quantity = quantitySpinner.getValue();
                handleAddRecommended(item.getName(), quantity);
            });

            recommendedItemsBox.getChildren().add(pill);
        }
    }


    @FXML
    private void handleCheckAll() {
        boolean selectAll = checkAllBox.isSelected();
        for (ShoppingItem item : shoppingList) {
            item.selectedProperty().set(selectAll);
        }

        checkAllBox.setText(selectAll ? "☑️ Deselect All" : "🔲 Select All");
        shoppingListView.refresh();
    }

    private void updateCheckAllBox() {
        boolean allChecked = shoppingList.stream().allMatch(i -> i.selectedProperty().get());
        boolean noneChecked = shoppingList.stream().noneMatch(i -> i.selectedProperty().get());

        checkAllBox.setSelected(allChecked);
        if (allChecked) {
            checkAllBox.setText("☑️ Deselect All");
        } else if (noneChecked) {
            checkAllBox.setText("🔲 Select All");
        } else {
            checkAllBox.setText("➖ Some Selected");
        }
    }

    private void showToast(String message) {
        toastLabel.setText(message);
        toastLabel.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), toastLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), toastLabel);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        pause.setOnFinished(e -> fadeOut.play());
        fadeOut.setOnFinished(e -> toastLabel.setVisible(false));
        pause.play();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @FXML
    public void toggleDarkMode() {
        if (scene == null) return;

        if (darkModeToggle.isSelected()) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("ShoppingListDark.css").toExternalForm());
            darkModeToggle.setText("☀️ Light Mode");
        } else {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("org/example/mealwise/css/ShoppingListPage.css").toExternalForm());
            darkModeToggle.setText("🌙 Dark Mode");
        }
    }
}
