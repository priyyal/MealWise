package org.example.mealwise.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.mealwise.services.UserService;
import org.example.mealwise.utils.PasswordManager;

public class UserAuthenticationController implements Initializable {

    @FXML
    private Pane imagePane;

    @FXML
    private VBox signupSection;

    @FXML
    private VBox loginSection;

    @FXML
    private StackPane container;

    @FXML
    private HBox root;

    @FXML
    private Button signupButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField loginPasswordField;

    private boolean isLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String url = "https://images.unsplash.com/photo-1628102491629-778571d893a3?q=80&w=2080&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        Rectangle clip = new Rectangle(imagePane.getWidth(), imagePane.getHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        clip.widthProperty().bind(imagePane.widthProperty());
        clip.heightProperty().bind(imagePane.heightProperty());
        imagePane.setClip(clip);

        imagePane.widthProperty().addListener((obs, oldWidth, newWidth) -> adjustImageView(imageView, imagePane));
        imagePane.heightProperty().addListener((obs, oldHeight, newHeight) -> adjustImageView(imageView, imagePane));
        imagePane.getChildren().clear();
        imagePane.getChildren().add(imageView);

        loginSection.setVisible(false);
        loginSection.setManaged(false);
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

    public void switchToLogin(ActionEvent event){
        if (!isLogin){
            TranslateTransition imageAnimation = new TranslateTransition();
            imageAnimation.setDuration(Duration.seconds(1));
            imageAnimation.setByX(container.getLayoutX() - imagePane.getLayoutX());
            imageAnimation.setInterpolator(Interpolator.EASE_BOTH);
            imageAnimation.setNode(imagePane);

            TranslateTransition formTransition = new TranslateTransition();
            formTransition.setDuration(Duration.seconds(1));
            formTransition.setByX(imagePane.getScene().getWidth() - container.getLayoutX() - container.getLayoutBounds().getWidth());
            formTransition.setInterpolator(Interpolator.EASE_BOTH);
            formTransition.setNode(container);

            FadeTransition fadeoutTransition = new FadeTransition();
            fadeoutTransition.setDuration(Duration.seconds(0.3));
            fadeoutTransition.setFromValue(1);
            fadeoutTransition.setToValue(0);
            fadeoutTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeoutTransition.setNode(signupSection);

            FadeTransition fadeinTransition = new FadeTransition();
            fadeinTransition.setDuration(Duration.seconds(0.3));
            fadeinTransition.setFromValue(0);
            fadeinTransition.setToValue(1);
            fadeinTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeinTransition.setNode(loginSection);

            fadeoutTransition.setOnFinished(e -> {
                signupSection.setManaged(false);
                signupSection.setVisible(false);
                loginSection.setManaged(true);
                loginSection.setVisible(true);
                fadeinTransition.play();
            });

            formTransition.play();
            imageAnimation.play();
            fadeoutTransition.play();

            System.out.println(container.getChildren());
            isLogin = !isLogin;
        }
    }

    public void switchToSignUp(ActionEvent event){
        if (isLogin){
            TranslateTransition imageAnimation = new TranslateTransition();
            imageAnimation.setDuration(Duration.seconds(1));
            imageAnimation.setByX(-(container.getLayoutX() - imagePane.getLayoutX()));
            imageAnimation.setInterpolator(Interpolator.EASE_BOTH);
            imageAnimation.setNode(imagePane);

            TranslateTransition imageTransition = new TranslateTransition();
            imageTransition.setDuration(Duration.seconds(1));
            imageTransition.setByX(-(imagePane.getScene().getWidth() - container.getLayoutX() - container.getLayoutBounds().getWidth()));
            imageTransition.setInterpolator(Interpolator.EASE_BOTH);
            imageTransition.setNode(container);

            FadeTransition fadeoutTransition = new FadeTransition();
            fadeoutTransition.setDuration(Duration.seconds(0.3));
            fadeoutTransition.setFromValue(1);
            fadeoutTransition.setToValue(0);
            fadeoutTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeoutTransition.setNode(loginSection);

            FadeTransition fadeinTransition = new FadeTransition();
            fadeinTransition.setDuration(Duration.seconds(0.3));
            fadeinTransition.setFromValue(0);
            fadeinTransition.setToValue(1);
            fadeinTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeinTransition.setNode(signupSection);

            fadeoutTransition.setOnFinished(e -> {
                signupSection.setManaged(true);
                signupSection.setVisible(true);
                loginSection.setManaged(false);
                loginSection.setVisible(false);
                fadeinTransition.play();
            });

            imageTransition.play();
            imageAnimation.play();
            fadeoutTransition.play();

            System.out.println(container.getChildren());
            isLogin = !isLogin;
        }
    }

    public void forgotPasswordWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/ForgotPasswordEmail.fxml"));
        Scene scene = new Scene(loader.load(), 500, 500);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Forgot Password");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    public void createAccount(ActionEvent event){
        String name = nameField.getText();
        String email = emailField.getText();
        String password = signupPasswordField.getText();

        UserService.createAccount(name, email, password);
        switchToLogin(new ActionEvent());
    }

    public void loginUser(ActionEvent event) throws IOException {
        String userIdentifier = usernameField.getText();
        String userPassword = loginPasswordField.getText();

        boolean isAuthenticated = UserService.login(userIdentifier, userPassword);

        if (PasswordManager.isValidPasswordFormat(userPassword)) {
            if (!isAuthenticated) {
                addErrorStyling();
                System.out.println("Bad pass bruh");
            }else{
                System.out.println("✅ User logged in successfully!");
                removeErrorStyling();
                slideToDashboard();
            }
        }
    }

    public void slideToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mealwise/fxml/Dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            double sceneWidth = root.getScene().getWidth();
            dashboardRoot.setTranslateX(sceneWidth);

            StackPane parentStack = (StackPane) root.getScene().getRoot();
            parentStack.getChildren().add(dashboardRoot);

            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), dashboardRoot);
            slideIn.setFromX(sceneWidth);
            slideIn.setToX(0);
            slideIn.setInterpolator(Interpolator.EASE_BOTH);

            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(1), root);
            slideOut.setFromX(0);
            slideOut.setToX(-sceneWidth);
            slideOut.setInterpolator(Interpolator.EASE_BOTH);

            slideOut.setOnFinished(event -> {
                parentStack.getChildren().remove(root);
            });

            slideIn.play();
            slideOut.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addErrorStyling(){
        usernameField.getStyleClass().add("error");
        loginPasswordField.getStyleClass().add("error");
    }

    public void removeErrorStyling(){
        usernameField.getStyleClass().remove("error");
        loginPasswordField.getStyleClass().remove("error");
    }
}