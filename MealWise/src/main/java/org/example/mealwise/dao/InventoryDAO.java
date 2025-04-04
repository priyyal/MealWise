package org.example.mealwise.dao;

import javafx.scene.chart.XYChart;
import org.example.mealwise.database.DatabaseConnection;
import org.example.mealwise.models.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO implements GenericDAO<Inventory> {
    private static final String INSERT_INTO_INVENTORY = "INSERT INTO inventory (user_id, item_id, quantity, expiry_date, purchase_batch_id) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_INVENTORY_BY_USER_ID =
            "SELECT i.inventory_id, g.name AS grocery_name, i.quantity, i.expiry_date " +
                    "FROM inventory i " +
                    "JOIN items g ON i.item_id = g.item_id " +
                    "WHERE i.user_id = ?";
    private static final String GET_ALL_INVENTORY = "SELECT * FROM inventory";
    private static final String GET_INVENTORY_BY_INVENTORY_ID = "SELECT * FROM inventory WHERE inventory_id = ?";
    private static final String UPDATE_INVENTORY = "UPDATE inventory SET quantity = ? WHERE inventory_id = ?";
    private static final String GET_EXPIRED_ITEMS =
            "SELECT inv.inventory_id, inv.item_id, i.name, inv.quantity, inv.expiry_date " +
                    "FROM inventory inv " +
                    "JOIN items i ON inv.item_id = i.item_id " +
                    "WHERE inv.user_id = ?" +
                    "AND expiry_date < CURRENT_DATE " +
                    "ORDER BY expiry_date ASC";
    private static final String GET_SOON_TO_EXPIRE_ITEMS =
            "SELECT inv.inventory_id, inv.item_id, i.name, inv.quantity, inv.expiry_date " +
                    "FROM inventory inv " +
                    "JOIN items i ON inv.item_id = i.item_id " +
                    "WHERE inv.user_id = ? " +
                    "AND expiry_date BETWEEN CURRENT_DATE AND (CURRENT_DATE + (? * INTERVAL '1 day')) " +
                    "ORDER BY expiry_date ASC";
    private static final String DELETE_INVENTORY = "DELETE FROM inventory WHERE inventory_id = ?";
    private static final String DELETE_EXPIRED_ITEMS = "DELETE FROM INVENTORY WHERE user_id = ? AND expiry_date < CURRENT_DATE";

    private static final String GET_ITEM_PER_CATEGORY =
            "SELECT c.name AS category_name, SUM(inv.quantity) AS total_quantity " +
                    "FROM inventory inv " +
                    "JOIN items i ON inv.item_id = i.item_id " +
                    "JOIN categories c ON i.category_id = c.category_id " +
                    "WHERE inv.user_id = ? " +
                    "GROUP BY c.name " +
                    "ORDER BY total_quantity DESC";


    @Override
    public void insert(Inventory obj) {
        try(Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_INTO_INVENTORY);
            stmt.setInt(1, obj.getUserId());
            stmt.setInt(2, obj.getItemId());
            stmt.setInt(3, obj.getQuantity());
            stmt.setDate(4, Date.valueOf(obj.getExpiryDate()));
            stmt.setInt(5, obj.getPurchaseBatchId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inventory> getAll() {
        List<Inventory> inventory = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_INVENTORY);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                inventory.add(new Inventory(rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date").toLocalDate(),
                        rs.getInt("purchase_batch_id")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return inventory;
    }

    @Override
    public Inventory getById(int id) {
        try(Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(GET_INVENTORY_BY_INVENTORY_ID);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Inventory(rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date").toLocalDate(),
                        rs.getInt("purchase_batch_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public boolean update(Inventory obj) {
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stm = conn.prepareStatement(UPDATE_INVENTORY);
            stm.setInt(1, obj.getQuantity());
            stm.setInt(2, obj.getInventoryId());
            stm.executeUpdate();
            return true;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(DELETE_INVENTORY);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Inventory> getAllUserInventory(int userId) {
        List<Inventory> inventoryList = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(GET_INVENTORY_BY_USER_ID);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                inventoryList.add(new Inventory(rs.getInt("inventory_id"),
                        rs.getString("grocery_name"),
                        userId,
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date").toLocalDate()));
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return inventoryList;
    }

    public List<Inventory> getExpiredItems(int userId) {
        List<Inventory> expiredItems = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(GET_EXPIRED_ITEMS);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                expiredItems.add(new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date").toLocalDate()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return expiredItems;
    }

    public List<Inventory> getSoonToExpireItems(int userId, int days) {
        List<Inventory> soonToExpireItems = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(GET_SOON_TO_EXPIRE_ITEMS);
            stmt.setInt(1, userId);
            stmt.setInt(2, days);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                soonToExpireItems.add(new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("item_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date").toLocalDate()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return soonToExpireItems;
    }

    public void deleteExpiredItems(int userId) {
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(DELETE_EXPIRED_ITEMS);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<XYChart.Data<String, Number>> getInventoryByCategory(int userId){
        List<XYChart.Data<String, Number>> inventoryByCategory = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(GET_ITEM_PER_CATEGORY);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                inventoryByCategory.add(new XYChart.Data<>(rs.getString("category_name"), rs.getInt("total_quantity")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return inventoryByCategory;
    }
}