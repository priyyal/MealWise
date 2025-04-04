package org.example.mealwise.controllers;

import javafx.animation.FadeTransition;
import java.io.IOException;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

//import javafx.stage.Stage;
import javafx.util.Duration;

public class DashboardController {

    @FXML
    public Button inventoryButton;

    @FXML
    public VBox mainSection;
    @FXML
    public VBox sideBar;
    @FXML
    public Button dashboardButton;
    @FXML
    public VBox topSection;
    @FXML
    public GridPane bottomSection;

    private ActionEvent event;

    public void initialize()
    {

    }

    public void openInventory(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/InventoryPage.fxml"));
        Node newView = loader.load();
        inventoryButton.getScene().getWindow();
        mainSection.getChildren().clear();
        mainSection.getChildren().add(newView);

    }

    public void switchToDashboard(ActionEvent event) {
        if (mainSection != null && bottomSection != null) {
            mainSection.getChildren().clear();
            mainSection.getChildren().addAll(topSection, bottomSection);
        } else {
            System.out.println("⚠️ topRow or bottomRow is null. Can't switch to dashboard.");
        }
    }


    public ActionEvent getEvent() {
        return event;
    }

    public void setEvent(ActionEvent event) {
        this.event = event;
    }


    public void switchToDashboardView(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/Dashboard.fxml"));
        Node newView = loader.load();
        mainSection.getChildren().clear();
        mainSection.getChildren().add(newView);
    }

    public void openRecipe(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/RecipePage.fxml"));
        Node newView = loader.load();
        inventoryButton.getScene().getWindow();
        mainSection.getChildren().clear();
        mainSection.getChildren().add(newView);
    }

    public void openShoppingList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/ShoppingListPage.fxml"));
        Node newView = loader.load();
        inventoryButton.getScene().getWindow();
        mainSection.getChildren().clear();
        mainSection.getChildren().add(newView);
    }

    public void switchToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/UserAuthentication.fxml"));
            Parent loginRoot = loader.load();

            double sceneWidth = sideBar.getScene().getWidth();
            loginRoot.setTranslateX(sceneWidth);

            StackPane parentStack = (StackPane) sideBar.getScene().getRoot();
            HBox dashboardRoot = (HBox) sideBar.getParent();

            parentStack.getChildren().add(loginRoot);

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), loginRoot);
            slideIn.setFromX(sceneWidth);
            slideIn.setToX(0);
            slideIn.setInterpolator(Interpolator.EASE_BOTH);

            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(1), dashboardRoot);
            slideOut.setFromX(0);
            slideOut.setToX(-sceneWidth);
            slideOut.setInterpolator(Interpolator.EASE_BOTH);

            slideOut.setOnFinished(e -> parentStack.getChildren().remove(dashboardRoot));
            slideIn.play();
            slideOut.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
