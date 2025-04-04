package org.example.mealwise.utils;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class SceneManager {

    public static void switchScene(Stage stage, String file) throws IOException {
        Parent currentRoot = stage.getScene().getRoot();

        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(String.format("/org/example/mealwise/fxml/%s", file)));
        Parent newRoot = loader.load();

        double sceneWidth = stage.getScene().getWidth();
        newRoot.setTranslateX(sceneWidth);

        StackPane transitionPane = new StackPane(currentRoot, newRoot);
        Scene transitionScene = new Scene(transitionPane, sceneWidth, stage.getScene().getHeight());

        stage.setScene(transitionScene);

        TranslateTransition translateOut = new TranslateTransition(Duration.seconds(0.7), currentRoot);
        translateOut.setInterpolator(Interpolator.EASE_BOTH);
        translateOut.setToX(sceneWidth);

        TranslateTransition translateIn = new TranslateTransition(Duration.seconds(0.7), newRoot);
        translateIn.setInterpolator(Interpolator.EASE_BOTH);
        translateIn.setFromX(-sceneWidth);
        translateIn.setToX(0);

        ParallelTransition parallelTransition = new ParallelTransition(translateIn, translateOut);
        parallelTransition.setInterpolator(Interpolator.EASE_BOTH);
        parallelTransition.setOnFinished(e -> {
            Scene newScene = new Scene(newRoot, sceneWidth, stage.getScene().getHeight());
            stage.setScene(newScene);
        });

        parallelTransition.play();
    }

}
