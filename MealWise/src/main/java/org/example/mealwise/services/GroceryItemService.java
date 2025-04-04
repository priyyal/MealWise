package org.example.mealwise.services;

import org.example.mealwise.dao.GroceryItemDAO;
import org.example.mealwise.models.GroceryItem;

import java.io.IOException;
import java.util.List;

public class GroceryItemService {
    private static final GroceryItemDAO GROCERY_ITEM_DAO = new GroceryItemDAO();

    public String getItemNameById(int id){
        GroceryItem groceryItem = GROCERY_ITEM_DAO.getById(id);
        return groceryItem.getName();
    }

    public GroceryItem getItemById(int id){
        return GROCERY_ITEM_DAO.getById(id);
    }

    public List<GroceryItem> searchGroceryItems(String query){
        return GROCERY_ITEM_DAO.getMatchingGroceryItems(query);
    }

    public int getShelfLifeForItem(int id) {
        return GROCERY_ITEM_DAO.getShelfLifeForItem(id);
    }
}
