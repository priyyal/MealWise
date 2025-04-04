package org.example.mealwise.dao;

import org.example.mealwise.database.DatabaseConnection;
import org.example.mealwise.models.GroceryItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroceryItemDAO implements GenericDAO<GroceryItem> {
    private static final String INSERT_INGREDIENT = "INSERT INTO items (name, category_id) VALUES (?, ?)" +
                                                    "ON CONFLICT (name, category_id) DO UPDATE SET name = EXCLUDED.name";
    private static final String GET_ALL_INGREDIENTS = "SELECT * FROM items";
    private static final String GET_INGREDIENT_BY_ID = "SELECT * FROM items WHERE item_id = ?";
    private static final String GET_INGREDIENT_BY_NAME = "SELECT * FROM items WHERE name = ?";
    private static final String GET_INGREDIENT_SHELF_LIFE_BY_ID = "SELECT average_shelf_life FROM items " +
                                                                 "join categories on items.category_id = categories.category_id WHERE item_id = ?";
    private static final String DELETE_INGREDIENT = "DELETE FROM items WHERE item_id = ?";
    private static final String UPDATE_INGREDIENT = "UPDATE items SET name = ?, category_id = ? WHERE item_id = ?";
    private static final String GET_MATCHING_INGREDIENTS = "SELECT * FROM items WHERE name ILIKE ? order by length(name), name limit 8";

    @Override
    public void insert(GroceryItem obj) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INGREDIENT)) {
            stmt.setString(1, obj.getName());
            stmt.setInt(2, obj.getCategoryId());
            stmt.executeUpdate();
            System.out.println("✅ GroceryItem inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<GroceryItem> getAll() {
        List<GroceryItem> items = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_INGREDIENTS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                items.add(new GroceryItem(
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        rs.getInt("category_id")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public GroceryItem getById(int id) {
        try(Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(GET_INGREDIENT_BY_ID);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new GroceryItem(
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        rs.getInt("category_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean update(GroceryItem obj) {
        boolean isUpdated = false;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_INGREDIENT)) {
            stmt.setString(1, obj.getName());
            stmt.setInt(2, obj.getCategoryId());
            stmt.setInt(3, obj.getId());
            stmt.executeUpdate();
            isUpdated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    @Override
    public boolean delete(int id) {
        boolean isDeleted = false;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_INGREDIENT)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            isDeleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public List<GroceryItem> getMatchingGroceryItems(String query){
        List<GroceryItem> matchingItems = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(GET_MATCHING_INGREDIENTS);
            stmt.setString(1,  query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                matchingItems.add(new GroceryItem(
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        rs.getInt("category_id")));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return matchingItems;
    }

    public int getShelfLifeForItem(int id) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_INGREDIENT_SHELF_LIFE_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("average_shelf_life");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}