package org.example.mealwise.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.converter.IntegerStringConverter;
import org.example.mealwise.models.GroceryItem;
import org.example.mealwise.models.Inventory;
import org.example.mealwise.models.InventoryItemDisplay;
import org.example.mealwise.services.GroceryItemService;
import org.example.mealwise.services.InventoryManager;
import org.example.mealwise.utils.SessionManager;

import java.time.LocalDate;
import java.util.List;

public class InventoryPageController {

    @FXML private TableView<InventoryItemDisplay> inventoryTable;
    @FXML private TableColumn<InventoryItemDisplay, Integer> siNoColumn;
    @FXML private TableColumn<InventoryItemDisplay, String> itemColumn;
    @FXML private TableColumn<InventoryItemDisplay, Integer> quantityColumn;
    @FXML private TableColumn<InventoryItemDisplay, String> expirationDateColumn;
    @FXML private Button addItemButton;
    @FXML private Button removeItemButton;
    @FXML private Button saveButton;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private TextField searchField;
    @FXML private ListView<GroceryItem> suggestionListView;


    private final ObservableList<InventoryItemDisplay> inventoryList = FXCollections.observableArrayList();
    private final InventoryManager inventoryManager = new InventoryManager();
    private final GroceryItemService groceryService = new GroceryItemService();

    @FXML
    public void initialize() {
        loadInventoryFromDatabase();
        suggestionListView.setVisible(false);
        suggestionListView.setManaged(false);

        siNoColumn.setCellValueFactory(cellData -> cellData.getValue().serialNumberProperty().asObject());
        itemColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        expirationDateColumn.setCellValueFactory(cellData -> cellData.getValue().expiryDateProperty());

        itemColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        expirationDateColumn.setCellFactory(column -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();
            {
                datePicker.setOnAction(e -> {
                    InventoryItemDisplay rowItem = getTableView().getItems().get(getIndex());
                    if (rowItem != null && datePicker.getValue() != null) {
                        rowItem.setExpiryDate(datePicker.getValue().toString());
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isEmpty()) {
                    setGraphic(null);
                } else {
                    datePicker.setValue(LocalDate.parse(item));
                    setGraphic(datePicker);
                }
            }
        });

        inventoryTable.setItems(inventoryList);
        inventoryTable.setEditable(true);

        addItemButton.setOnAction(e -> addManualItem());
        removeItemButton.setOnAction(e -> removeItem());
        saveButton.setOnAction(e -> saveInventory());

        sortComboBox.getItems().addAll("Name", "Quantity", "Expiration Date");
        filterComboBox.getItems().addAll("Show All", "Low Stock (<3)", "Expired", "Soon to Expire (3 days)");
        filterComboBox.setValue("Show All");

        filterComboBox.setOnAction(e -> applyFilterAndSort());
        sortComboBox.setOnAction(e -> applyFilterAndSort());


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                suggestionListView.setVisible(false);
                suggestionListView.setManaged(false);
            } else {
                showSuggestions(newValue);
                suggestionListView.setVisible(true);
                suggestionListView.setManaged(true);
            }
        });

        quantityColumn.setOnEditCommit(event -> {
            InventoryItemDisplay item = event.getRowValue();
            item.setQuantity(event.getNewValue());
        });

        itemColumn.setOnEditCommit(event -> {
            InventoryItemDisplay item = event.getRowValue();
            item.setName(event.getNewValue());
        });



        suggestionListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(GroceryItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/ShoppingListCell.fxml"));
                        HBox root = loader.load();
                        ShoppingListCellController controller = loader.getController();
                        controller.setData(item, (groceryItem, quantity) -> {
                            int shelfLife = groceryService.getShelfLifeForItem(groceryItem.getId());
                            LocalDate expiry = LocalDate.now().plusDays(shelfLife);

                            inventoryList.add(new InventoryItemDisplay(
                                    inventoryList.size() + 1,
                                    -1,
                                    groceryItem.getName(),
                                    quantity,
                                    expiry.toString(),
                                    true
                            ));
                            suggestionListView.setVisible(false);
                            searchField.clear();
                        });
                        setGraphic(root);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadInventoryFromDatabase() {
        System.out.println("Halo");
        inventoryList.clear();
        List<Inventory> inventoryRecords = inventoryManager.getUserInventory(SessionManager.getUserId());
        System.out.println("bruhhh");
        System.out.println(SessionManager.getUserId());

        int count = 1;
        System.out.println("okayyy");
        for (Inventory inv : inventoryRecords) {
            System.out.println("Inside the loop");
            String name = inv.getGroceryName();
            System.out.println("Loaded item: " + name + ", Quantity: " + inv.getQuantity());
            inventoryList.add(new InventoryItemDisplay(count++, inv.getInventoryId(), name, inv.getQuantity(), inv.getExpiryDate().toString(), false));
        }
    }

    private void showSuggestions(String query) {
        List<GroceryItem> suggestions = groceryService.searchGroceryItems(query);
        suggestionListView.setItems(FXCollections.observableArrayList(suggestions));
    }

    private void addManualItem() {
        int newSiNo = inventoryList.size() + 1;
        inventoryList.add(new InventoryItemDisplay(newSiNo, -1, "", 0, LocalDate.now().toString(), true));
    }

    private void removeItem() {
        InventoryItemDisplay selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            inventoryList.remove(selectedItem);
            inventoryManager.removeItem(selectedItem.getInventoryId());
        }
    }

    private void saveInventory() {
        for (InventoryItemDisplay display : inventoryList) {
            if (display.getInventoryId() == -1) {
                if (display.getQuantity() <= 0) {
                    quantityColumn.getStyleClass().add("error");
                }
                inventoryManager.insertCustomItem(display.getName(), display.getQuantity(), display.getExpiryDate());
            } else {
                quantityColumn.getStyleClass().remove("error");
                inventoryManager.updateInventoryFromDisplay(display);
            }
        }
    }

    private void applyFilterAndSort() {
        String selectedFilter = filterComboBox.getValue();
        String selectedSort = sortComboBox.getValue();

        ObservableList<InventoryItemDisplay> filteredList = FXCollections.observableArrayList(inventoryList);

        if (selectedFilter != null && !selectedFilter.equals("Show All")) {
            LocalDate today = LocalDate.now();

            filteredList.removeIf(item -> {
                LocalDate expiry = item.getExpiryDate();
                switch (selectedFilter) {
                    case "Low Stock (<3)":
                        return item.getQuantity() >= 3;
                    case "Expired":
                        return !expiry.isBefore(today);
                    case "Soon to Expire (3 days)":
                        return expiry.isBefore(today) || expiry.isAfter(today.plusDays(3));
                    default:
                        return false;
                }
            });
        }

        if (selectedSort != null) {
            switch (selectedSort) {
                case "Name":
                    FXCollections.sort(filteredList, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                    break;
                case "Quantity":
                    FXCollections.sort(filteredList, (a, b) -> Integer.compare(a.getQuantity(), b.getQuantity()));
                    break;
                case "Expiration Date":
                    FXCollections.sort(filteredList, (a, b) -> a.getExpiryDate().compareTo(b.getExpiryDate()));
                    break;
            }
        }
        inventoryTable.setItems(filteredList);
    }

}
