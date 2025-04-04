package org.example.mealwise.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.mealwise.models.ShoppingItem;
import org.example.mealwise.dao.UsageDAO;
import org.example.mealwise.models.FrequentItem;
import org.example.mealwise.models.Inventory;
import org.example.mealwise.models.Usage;

public class ShoppingListService {

    private final UsageDAO usageDAO;

    public ShoppingListService(UsageDAO usageDAO) {
        this.usageDAO = usageDAO;
    }

    /**
     * Combines core logic: frequent items + low stock + expired items
     */
    public List<ShoppingItem> getRecommendedItems(int userId, List<Inventory> inventory) {
        Set<String> recommended = new HashSet<>();
        List<ShoppingItem> smartList = new ArrayList<>();

        List<FrequentItem> frequentItems = usageDAO.getFrequentlyUsedItems(userId);

        for (FrequentItem item : frequentItems) {
            boolean inStock = inventory.stream()
                    .anyMatch(inv -> inv.getItemId() == item.getItemId() && inv.getQuantity() > 2);

            if (!inStock) {
                recommended.add(item.getItemName());
                smartList.add(new ShoppingItem(item.getItemName()));
            }
        }

        // Add items that are expiring soon (within 3 days)
        for (Inventory inv : inventory) {
            boolean isExpiring = inv.getExpiryDate().isBefore(LocalDate.now().plusDays(3));
            if (isExpiring && !recommended.contains(inv.getGroceryName())) {
                smartList.add(new ShoppingItem(inv.getGroceryName()));
                recommended.add(inv.getGroceryName());
            }
        }

        return smartList;
    }



    /**
     * Items that will expire soon (within 3 days)
     */
    public List<ShoppingItem> getExpiringItems(List<Inventory> inventory) {
        LocalDate now = LocalDate.now();
        return inventory.stream()
                .filter(inv -> inv.getExpiryDate().isBefore(now.plusDays(3)))
                .map(inv -> new ShoppingItem(inv.getGroceryName()))
                .collect(Collectors.toList());
    }

    /**
     * Predict needs for next 7 days based on average usage
     */
    public List<ShoppingItem> getWeeklyUsagePredictions(int userId, List<Inventory> inventory) {
        List<Usage> usageList = usageDAO.getAllUsageByUser(userId);
        Map<Integer, Integer> usageCountMap = new HashMap<>();
        Map<Integer, String> itemNames = new HashMap<>();

        for (Usage u : usageList) {
            usageCountMap.put(u.getGroceryItemId(),
                    usageCountMap.getOrDefault(u.getGroceryItemId(), 0) + u.getUsageQuantity());
        }

        for (Inventory inv : inventory) {
            itemNames.putIfAbsent(inv.getItemId(), inv.getGroceryName());
        }

        List<ShoppingItem> predicted = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : usageCountMap.entrySet()) {
            int itemId = entry.getKey();
            int avgWeeklyUse = entry.getValue() / 4; // Assume ~4 weeks

            int currentStock = inventory.stream()
                    .filter(inv -> inv.getItemId() == itemId)
                    .mapToInt(Inventory::getQuantity)
                    .sum();

            if (currentStock < avgWeeklyUse) {
                predicted.add(new ShoppingItem(itemNames.get(itemId)));
            }
        }

        return predicted;
    }

    /**
     * Suggest pantry restocks for items previously used but no longer in inventory
     */
    public List<ShoppingItem> suggestPantryRefills(int userId, List<Inventory> inventory) {
        List<Usage> usageList = usageDAO.getAllUsageByUser(userId);

        Set<Integer> usedItemIds = usageList.stream()
                .map(Usage::getGroceryItemId)
                .collect(Collectors.toSet());

        Set<Integer> inStockIds = inventory.stream()
                .map(Inventory::getItemId)
                .collect(Collectors.toSet());

        List<ShoppingItem> refills = new ArrayList<>();
        for (Usage u : usageList) {
            if (!inStockIds.contains(u.getGroceryItemId())) {
                refills.add(new ShoppingItem(u.getItemName()));
            }
        }

        return refills;
    }

    /**
     * Filter inventory items by category
     */
    public List<ShoppingItem> suggestByCategory(List<Inventory> inventory, String category) {
        return inventory.stream()
                .filter(i -> i.getGroceryName().toLowerCase().contains(category.toLowerCase()))
                .map(i -> new ShoppingItem(i.getGroceryName()))
                .collect(Collectors.toList());
    }
}
