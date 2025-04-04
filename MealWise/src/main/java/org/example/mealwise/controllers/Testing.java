package org.example.mealwise.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Testing extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/org/example/mealwise/fxml/RecipePage.fxml")));
        primaryStage.setScene(scene);
        primaryStage.setTitle("MealWise");
        primaryStage.show();
    }
}
