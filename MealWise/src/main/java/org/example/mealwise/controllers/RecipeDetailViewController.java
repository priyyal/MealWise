package org.example.mealwise.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.mealwise.models.Recipe;
import org.example.mealwise.utils.ImageLoader;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class RecipeDetailViewController implements Initializable {

    @FXML
    private Label recipeTitle;

    @FXML
    private Label recipeTime;

    @FXML
    private Label calories;

    @FXML
    private Label protein;

    @FXML
    private Label carbs;

    @FXML
    private Label fat;

    @FXML
    private Label summary;

    @FXML
    private Label instruction;

    @FXML
    private Pane imagePane;

    private double xOffset = 0;

    private double yOffset = 0;

    private Recipe recipe;

    private ImageView recipeImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recipeImage = new ImageView();
        recipeImage.setPreserveRatio(true);
        Rectangle clip = new Rectangle(imagePane.getWidth(), imagePane.getHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        clip.widthProperty().bind(imagePane.widthProperty());
        clip.heightProperty().bind(imagePane.heightProperty());
        imagePane.setClip(clip);

        recipeImage.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                adjustImageView(recipeImage, imagePane);
            }
        });

        imagePane.widthProperty().addListener((obs, oldWidth, newWidth) -> adjustImageView(recipeImage, imagePane));
        imagePane.heightProperty().addListener((obs, oldHeight, newHeight) -> adjustImageView(recipeImage, imagePane));
        imagePane.getChildren().clear();
        imagePane.getChildren().add(recipeImage);
    }

    private void adjustImageView(ImageView imageView, Pane imagePane) {
        double paneWidth = imagePane.getWidth();
        double paneHeight = imagePane.getHeight();
        double videoWidth = imageView.getImage().getWidth();
        double videoHeight = imageView.getImage().getHeight();

        double scaleWidth = paneWidth / videoWidth;
        double scaleHeight = paneHeight / videoHeight;
        double scale = Math.max(scaleWidth, scaleHeight);

        imageView.setFitWidth(videoWidth * scale);
        imageView.setFitHeight(videoHeight * scale);
        imageView.setTranslateX((paneWidth - imageView.getFitWidth()) / 2);
        imageView.setTranslateY((paneHeight - imageView.getFitHeight()) / 2);
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        ImageLoader.loadImageAsync(recipe.getImageUrl().getFirst() , recipeImage);
        recipeTitle.setText(recipe.getTitle());
        recipeTime.setText(recipe.getCookingTime() + "m");
        HashMap<String, Integer> nutrients = recipe.getNutrition();
        setNutritionValues(nutrients);
        if (recipe.getSummary() != null){
            summary.setText(recipe.getSummary());
        }
        instruction.setText(recipe.getInstructions());
    }


    public void setNutritionValues(HashMap<String, Integer> nutrients) {
        if (nutrients == null) {
            calories.setText("N/A");
            protein.setText("N/A");
            carbs.setText("N/A");
            fat.setText("N/A");
            return;
        }

        calories.setText(String.valueOf(nutrients.get("Calories")));
        protein.setText(String.valueOf(nutrients.get("Protein")));
        carbs.setText(String.valueOf(nutrients.get("Carbohydrates")));
        fat.setText(String.valueOf(nutrients.get("Fat")));
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) recipeTitle.getScene().getWindow();
        stage.close();
    }

    public void enableWindowDrag(Scene scene, Stage stage){
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
