package org.example.mealwise.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.mealwise.models.Recipe;
import org.example.mealwise.utils.ImageLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeCardController implements Initializable {
    @FXML
    private VBox imagePane;

    @FXML
    private Label recipeTitle;

    @FXML
    private Label recipeTime;

    @FXML
    private Label recipeLikes;

    private ImageView recipeImage;

    private Recipe recipe;

    private RecipePageController parentController;

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

        Image img = imageView.getImage();
        if (img == null || img.getProgress() < 1.0 ) return;

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

    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
        ImageLoader.loadImageAsync(recipe.getImageUrl().getFirst(), recipeImage);
        recipeTitle.setText(recipe.getTitle());
        recipeTime.setText(recipe.getCookingTime() + "m");
        if (recipe.getNutrition() != null && recipe.getNutrition().get("Calories") != null) {
            recipeLikes.setText(recipe.getNutrition().get("Calories").intValue() + " kcal");
        } else {
            recipeLikes.setText("N/A");
        }
    }

    public void switchToDetailView(ActionEvent event) throws IOException {
        if (parentController != null) {
            parentController.getDarkOverlay().setVisible(true);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/RecipeDetailView.fxml"));
        Scene scene = new Scene(loader.load());

        RecipeDetailViewController controller = loader.getController();
        controller.setRecipe(recipe);

        Stage stage = new Stage();
        controller.enableWindowDrag(scene, stage);
        stage.setScene(scene);
        stage.setTitle("Recipe");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);

        stage.setOnHidden(e -> {
            if (parentController != null) {
                parentController.getDarkOverlay().setVisible(false);
            }
        });
        stage.show();
    }

    public void setParentController(RecipePageController parentController) {
        this.parentController = parentController;
    }

}
