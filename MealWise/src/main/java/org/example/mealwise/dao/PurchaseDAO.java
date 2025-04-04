package org.example.mealwise.dao;

import org.example.mealwise.database.DatabaseConnection;
import org.example.mealwise.models.Purchase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO implements GenericDAO<Purchase>{
    private static final String INSERT_PURCHASE = "INSERT INTO purchases (user_id, item_id, quantity, purchase_date) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_PURCHASE = "SELECT * FROM purchases";
    private static final String GET_PURCHASE_BY_USER_ID = "SELECT * FROM purchases WHERE user_id = ?";
    private static final String DELETE_PURCHASE = "DELETE FROM purchases WHERE purchase_id = ?";


    @Override
    public void insert(Purchase purchase) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_PURCHASE)) {
            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getGroceryItemId());
            stmt.setInt(3, purchase.getQuantity());
            stmt.setDate(4, Date.valueOf(purchase.getPurchaseDate()));
            stmt.executeUpdate();
            System.out.println("✅ Purchase inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertPurchaseAndGetId(Purchase purchase) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_PURCHASE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getGroceryItemId());
            stmt.setInt(3, purchase.getQuantity());
            stmt.setDate(4, Date.valueOf(purchase.getPurchaseDate()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            System.out.println("✅ Purchase inserted successfully!");
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Purchase> getAll() {
        List<Purchase> purchases = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_PURCHASE);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                purchases.add(new Purchase(rs.getInt("purchase_id"),
                        rs.getInt("user_id"),
                        rs.getInt("grocery_item_id"),
                        rs.getInt("quantity"),
                        rs.getDate("purchase_date").toLocalDate()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return purchases;
    }

    @Override
    public Purchase getById(int id) {
        return null;
    }

    @Override
    public boolean update(Purchase obj) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
