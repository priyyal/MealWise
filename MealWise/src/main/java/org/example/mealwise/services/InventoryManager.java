package org.example.mealwise.services;

import org.example.mealwise.dao.*;
import org.example.mealwise.models.*;
import org.example.mealwise.utils.SessionManager;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class InventoryManager {
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final PurchaseDAO purchaseDAO = new PurchaseDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final GroceryItemDAO groceryItemDAO = new GroceryItemDAO();
    private final UsageDAO usageDAO = new UsageDAO();

    public void insertItem(String name, GroceryItem item, int quantity, int categoryId) {
        int userId = SessionManager.getUserId();
        int itemId = item.getId();
        LocalDate expiryLocalDate = getExpiryDate(itemId);
        Date expiryDate = Date.valueOf(expiryLocalDate);

        int purchaseId = createPurchase(userId, itemId, quantity, LocalDate.now()).getPurchaseId();
        Inventory inventory = new Inventory(userId, itemId, quantity, expiryLocalDate, purchaseId);
        inventoryDAO.insert(inventory);
    }

    public void insertCustomItem(String name, int quantity, LocalDate expiryDate) {
        int userId = SessionManager.getUserId();
        int purchaseId = createPurchase(userId, 0, quantity, expiryDate).getPurchaseId();

        Inventory inventory = new Inventory(userId, 0, quantity, expiryDate, purchaseId);
        inventoryDAO.insert(inventory);
    }


    public List<Inventory> removeExpiredItems(){
        int userId = SessionManager.getUserId();
        List<Inventory> expiredItems = inventoryDAO.getExpiredItems(userId);
        inventoryDAO.deleteExpiredItems(userId);
        return expiredItems;
    }

    public List<Inventory> getUserInventory(int userId){
       return inventoryDAO.getAllUserInventory(userId);
    }

    public boolean removeItem(int inventoryId) {
        return inventoryDAO.delete(inventoryId);
    }

    public List<Inventory> getExpiredItems(int userId) {
        return inventoryDAO.getExpiredItems(userId);
    }

    public List<Inventory> getSoonToExpireItems(int userId){
        int defaultDays = 4;
        return inventoryDAO.getSoonToExpireItems(userId, defaultDays);
    }

    public void logUsageFromInventoryUpdate(Inventory inventory, int newQuantity) {
        int userId = SessionManager.getUserId();
        int quantityUsed = inventory.getQuantity() - newQuantity;
        if (quantityUsed > 0) {
            logUsage(userId, inventory.getItemId(), quantityUsed);
        }
        updateItem(inventory, newQuantity);
    }

    public void updateInventoryFromDisplay(InventoryItemDisplay display) {
        Inventory original = inventoryDAO.getById(display.getInventoryId());

        if (display.getQuantity() < original.getQuantity()) {
            int usedQty = original.getQuantity() - display.getQuantity();
            logUsage(original.getUserId(), original.getItemId(), usedQty);
        }

        original.setQuantity(display.getQuantity());
        if (display.isExpiryEditable()) {
            original.setExpiryDate(display.getExpiryDate());
        }
        inventoryDAO.update(original);
    }


    private void logUsage(int userId, int itemId, int quantity){
        Usage usage = new Usage(userId, itemId, quantity, LocalDate.now());
        usageDAO.insert(usage);
    }

    public void updateItem(Inventory inventory, int quantity) {
        inventory.setQuantity(quantity);
        inventoryDAO.update(inventory);
    }

    public Purchase createPurchase(int userId, int groceryItemId, int quantity, LocalDate purchaseDate) {
        Purchase purchase = new Purchase(userId, groceryItemId, quantity, purchaseDate);
        int id = purchaseDAO.insertPurchaseAndGetId(purchase);
        purchase.setPurchaseId(id);
        return purchase;
    }


    private LocalDate getExpiryDate(int id) {
        int categoryID = groceryItemDAO.getById(id).getCategoryId();
        int shelfLife = categoryDAO.getById(categoryID).getCategoryShelfLife();

        LocalDate expiryDate = LocalDate.now().plusDays(shelfLife);

        return expiryDate;
    }
}
