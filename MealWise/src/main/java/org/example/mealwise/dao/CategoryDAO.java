package org.example.mealwise.dao;

import org.example.mealwise.database.DatabaseConnection;
import org.example.mealwise.models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDAO implements GenericDAO<Category> {
    private static final String INSERT_CATEGORY = "INSERT INTO categories (name, average_shelf_life) VALUES (?, ?)";
    private static final String GET_CATEGORY_BY_NAME = "SELECT * FROM categories WHERE name = ?";
    private static final String GET_CATEGORY_BY_ID = "SELECT * FROM categories WHERE category_id = ?";

    @Override
    public void insert(Category obj) {

    }

    @Override
    public List<Category> getAll() {
        return List.of();
    }

    @Override
    public Category getById(int id) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_CATEGORY_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Category(rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getInt("average_shelf_life"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Category getByName(String name) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_CATEGORY_BY_NAME)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Category(rs.getInt("category_id"),
                                    rs.getString("name"),
                                    rs.getInt("average_shelf_life"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Category obj) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
