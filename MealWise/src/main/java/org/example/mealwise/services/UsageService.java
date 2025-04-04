package org.example.mealwise.services;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import org.example.mealwise.dao.UsageDAO;
import org.example.mealwise.models.FrequentItem;
import org.example.mealwise.models.Usage;
import org.example.mealwise.utils.SessionManager;

import java.util.List;

public class UsageService {
    private final UsageDAO usageDAO = new UsageDAO();

    public List<Usage> getAllUserUsage() {
        return usageDAO.getAllUsageByUser(SessionManager.getUserId());
    }

    public List<FrequentItem> getTopFrequentlyUsedItems() {
        return usageDAO.getFrequentlyUsedItems(SessionManager.getUserId());
    }

    public List<PieChart.Data> getUsagePieChartData() {
        return usageDAO.getUsagePieChartData(SessionManager.getUserId());
    }

    public List<XYChart.Data<String, Number>> getUsageBarChartData() {
        return usageDAO.getUsageBarChartData(SessionManager.getUserId());
    }

    public boolean deleteUsage(int usageId) {
        return usageDAO.delete(usageId);
    }

    public boolean updateUsage(Usage usage) {
        return usageDAO.update(usage);
    }
}
