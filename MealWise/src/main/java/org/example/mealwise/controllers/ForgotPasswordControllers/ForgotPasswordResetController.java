package org.example.mealwise.controllers.ForgotPasswordControllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.mealwise.utils.EmailAuth;
import org.example.mealwise.utils.PasswordManager;
import org.example.mealwise.services.UserService;


public class ForgotPasswordResetController {

    @FXML
    private PasswordField firstInputField;

    @FXML
    private PasswordField secondInputField;

    public void resetPassword(ActionEvent event) {
        if (isValidPassword()){
            removeErrorStyling();
            String password = firstInputField.getText();
            String email = EmailAuth.getEmail();
            UserService.resetPassword(email, password);
            removeErrorStyling();
            PauseTransition pause = new PauseTransition(Duration.seconds(1));

            pause.setOnFinished(e -> {
                Stage stage = (Stage) firstInputField.getScene().getWindow();
                stage.close();
            });

            pause.play();;
        }
    }

    public boolean isValidPassword(){
        if (firstInputField.getText().isEmpty() || secondInputField.getText().isEmpty() || !firstInputField.getText().equals(secondInputField.getText())){
            addErrorStyling();
            return false;
        }
        String password = firstInputField.getText();
        return PasswordManager.isValidPasswordFormat(password);
    }

    private void addErrorStyling(){
        firstInputField.getStyleClass().add("error");
        secondInputField.getStyleClass().add("error");
    }

    private void removeErrorStyling(){
        firstInputField.getStyleClass().remove("error");
        secondInputField.getStyleClass().remove("error");
    }
}
