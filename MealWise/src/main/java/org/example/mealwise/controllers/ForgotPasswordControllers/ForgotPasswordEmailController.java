package org.example.mealwise.controllers.ForgotPasswordControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.mealwise.utils.SceneManager;
import java.io.IOException;

import org.example.mealwise.services.UserService;
import org.example.mealwise.utils.EmailAuth;


public class ForgotPasswordEmailController {

    @FXML
    private Button resetButton;

    @FXML
    private VBox root;

    @FXML
    private TextField emailField;


    public void switchScene(ActionEvent event) throws IOException {
        Stage stage = (Stage) resetButton.getScene().getWindow();
        String email = emailField.getText();

        if (email.isEmpty()){
            emailField.getStyleClass().add("error");
        }else if(EmailAuth.isValidEmail(email)){
            if (UserService.initiatePasswordReset(email)){
                emailField.getStyleClass().remove("error");
                EmailAuth.setEmail(emailField.getText());
                SceneManager.switchScene(stage, "ForgotPasswordOTP.fxml");
            }
        }
    }
}
