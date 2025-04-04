package org.example.mealwise.controllers.ForgotPasswordControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.mealwise.utils.SceneManager;
import org.example.mealwise.services.UserService;
import org.example.mealwise.utils.EmailAuth;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ForgotPasswordOTPController implements Initializable {

    @FXML
    private Label titleLabel;

    @FXML
    private Text emailText;

    @FXML
    private HBox inputFieldContainer;

    @FXML
    private Button resetButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String email = EmailAuth.getEmail();
        emailText.setText(email);
        setupOTPFields();
    }

    public void switchScene(ActionEvent event) throws IOException {
        if (isvalidOTP()){
            Stage stage = (Stage) titleLabel.getScene().getWindow();
            SceneManager.switchScene(stage, "ForgotPasswordReset.fxml");
        }
    }

    private void setupOTPFields() {
        Pattern pattern = Pattern.compile("[0-9]?");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        };

        List<TextField> textFields = new ArrayList<>();
        for (Node node : inputFieldContainer.getChildren()) {
            textFields.add((TextField) node);
        }

        for (int i = 0; i < textFields.size(); i++) {
            TextField currentField = textFields.get(i);
            int nextIndex = i + 1;
            int prevIndex = i - 1;

            currentField.setTextFormatter(new TextFormatter<>(filter));

            currentField.textProperty().addListener((obs, oldValue, newValue) -> {
                if (!newValue.isEmpty() && nextIndex < textFields.size()) {
                    textFields.get(nextIndex).requestFocus();
                }
            });

            currentField.setOnKeyPressed(
                    (KeyEvent event) -> {
                        if (event.getCode() == KeyCode.RIGHT && nextIndex < textFields.size()) {
                            textFields.get(nextIndex).requestFocus();
                        }
                    }
            );

            currentField.setOnKeyPressed(
                    (KeyEvent event) -> {
                        if (event.getCode() == KeyCode.LEFT && prevIndex >= 0) {
                            textFields.get(prevIndex).requestFocus();
                        }
                    }
            );

            currentField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.BACK_SPACE && currentField.getText().isEmpty() && prevIndex >= 0) {
                    textFields.get(prevIndex).requestFocus();
                }
            });
        }
    }

    private boolean validateOTPHelper() {
        boolean isAllFieldsValid = true;
        for (Node node : inputFieldContainer.getChildren()) {
            TextField currentField = (TextField) node;
            if (currentField.getText().trim().isEmpty()) {
                isAllFieldsValid = false;
            }
        }

        if (!isAllFieldsValid) {
            for (Node node : inputFieldContainer.getChildren()) {
                TextField currentField = (TextField) node;
                currentField.getStyleClass().add("error-border");
            }

        }
        return isAllFieldsValid;
    }

    private String getOTP(){
        StringBuilder builder = new StringBuilder();
        for (Node node : inputFieldContainer.getChildren()) {
            TextField currentField = (TextField) node;
            currentField.getStyleClass().remove("error-border");
            currentField.getStyleClass().add("success-border");
            builder.append(currentField.getText().trim());
        }
        return builder.toString();
    }

    public boolean isvalidOTP(){
        boolean isValidOTP = false;
        if (validateOTPHelper()){
            String inputOTP = getOTP();
            isValidOTP = UserService.verifyOTP(inputOTP);
        }
        return isValidOTP;
    }
}