package org.example.mealwise.dao;

import org.example.mealwise.database.DatabaseConnection;
import org.example.mealwise.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements GenericDAO<User> {
    private static final String INSERT_USER = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
    private static final String GET_ALL_USERS = "SELECT * FROM users";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    private static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String GET_USER_BY_NAME = "SELECT * FROM users WHERE username = ?";
    private static final String UPDATE_USER = "UPDATE users SET username = ?, email = ?, password_hash = ? WHERE user_id = ?";
    private static final String UPDATE_USER_BY_EMAIL = "UPDATE users SET password_hash = ? WHERE email = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE user_id = ?";

    @Override
    public void insert(User user) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.executeUpdate();
            System.out.println("✅ User inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getById(int id) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email){
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_EMAIL)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByName(String name){
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_NAME)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(User user) {
        boolean isUpdated = false;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.executeUpdate();
            isUpdated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    public boolean updateUserPassword(String password, String email){
        boolean isUpdated = false;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_BY_EMAIL)) {
            stmt.setString(1, password);
            stmt.setString(2, email);
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
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            isDeleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }
}