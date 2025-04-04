package org.example.mealwise.dao;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import org.example.mealwise.database.DatabaseConnection;
import org.example.mealwise.models.FrequentItem;
import org.example.mealwise.models.Usage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsageDAO implements GenericDAO<Usage> {
    private static final String INSERT_USAGE = "INSERT INTO usage (user_id, item_id, usage_quantity, usage_date) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_USAGE = "SELECT * FROM usage";
    private static final String GET_USAGE_BY_USAGE_ID = "SELECT * FROM usage WHERE usage_id = ?";
    private static final String GET_USAGE_BY_USER_ID = "SELECT * FROM usage WHERE user_id = ?";
    private static final String GET_FREQUENTLY_USED_ITEMS = "SELECT items.item_id, items.name, SUM(usage.usage_quantity) As Total_Used FROM items " +
                                                            "JOIN usage ON items.item_id = usage.item_id " +
                                                            "WHERE usage.user_id = ? GROUP BY items.name, items.item_id " +
                                                            "ORDER BY Total_Used DESC LIMIT 5";
    private static final String GET_USAGE_PIE_CHART_DATA = "SELECT items.name, usage.usage_quantity FROM items " +
                                                            "JOIN usage ON items.item_id = usage.item_id " +
                                                            "WHERE usage.user_id = ? GROUP BY items.name, usage.usage_quantity " +
                                                            "ORDER BY usage_quantity DESC";
    private static final String DELETE_USAGE = "DELETE FROM usage WHERE usage_id = ?";
    private static final String UPDATE_USAGE = "UPDATE usage SET usage_quantity = ? WHERE usage_id = ?";
    private static final String GET_USAGE_BAR_CHART_DATA = "SELECT c.name AS category_name, SUM(u.usage_quantity) AS total_used " +
                                                                "FROM usage u " +
                                                                "JOIN items i ON u.item_id = i.item_id " +
                                                                "JOIN categories c ON i.category_id = c.category_id " +
                                                                "WHERE u.user_id = ? " +
                                                                "GROUP BY c.name " +
                                                                "ORDER BY total_used DESC";

    @Override
    public void insert(Usage obj) {
        try (Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(INSERT_USAGE);
            stmt.setInt(1, obj.getUserId());
            stmt.setInt(2, obj.getGroceryItemId());
            stmt.setInt(3, obj.getUsageQuantity());
            stmt.setDate(4, Date.valueOf(obj.getUsageDate()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Usage> getAll() {
        List<Usage> usages = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_USAGE);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usages.add(new Usage(rs.getInt("usage_id"),
                        rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getInt("usage_quantity"),
                        rs.getDate("usage_date").toLocalDate()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usages;
    }

    @Override
    public Usage getById(int id) {
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(GET_USAGE_BY_USAGE_ID);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usage(rs.getInt("usage_id"),
                        rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getInt("usage_quantity"),
                        rs.getDate("usage_date").toLocalDate());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean update(Usage obj) {
        boolean isUpdated = false;
        try (Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_USAGE);
            stmt.setInt(1, obj.getUsageQuantity());
            stmt.setInt(2, obj.getUsageId());
            stmt.executeUpdate();
            isUpdated = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return isUpdated;
    }

    @Override
    public boolean delete(int id) {
        boolean isDeleted = false;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USAGE)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            isDeleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public List<Usage> getAllUsageByUser(int userId){
        List<Usage> usageList = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(GET_USAGE_BY_USER_ID);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usageList.add(new Usage(rs.getInt("usage_id"),
                        rs.getInt("user_id"),
                        rs.getInt("item_id"),
                        rs.getInt("usage_quantity"),
                        rs.getDate("usage_date").toLocalDate()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usageList;
    }

    public List<FrequentItem> getFrequentlyUsedItems(int userId){
        List<FrequentItem> frequentItems = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()){
            PreparedStatement stmt = conn.prepareStatement(GET_FREQUENTLY_USED_ITEMS);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                    frequentItems.add(new FrequentItem(rs.getInt("item_id"),
                            rs.getString("name"),
                            rs.getInt("Total_Used")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return frequentItems;
    }

    public List<PieChart.Data> getUsagePieChartData(int userId){
        List<PieChart.Data> pieChartData = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(GET_USAGE_PIE_CHART_DATA);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pieChartData.add(new PieChart.Data(rs.getString("name"),
                        rs.getInt("usage_quantity")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pieChartData;
    }

    public List<XYChart.Data<String, Number>> getUsageBarChartData(int userId) {
        List<XYChart.Data<String, Number>> barChartData = new ArrayList<>();
        try(Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(GET_USAGE_BAR_CHART_DATA);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                barChartData.add(new XYChart.Data<>(rs.getString("name"),
                        rs.getInt("usage_quantity")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return barChartData;
    }
}