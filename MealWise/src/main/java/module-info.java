module org.example.mealwise {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jbcrypt;
    requires jakarta.mail;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires javafx.base;

    opens org.example.mealwise to javafx.fxml, com.fasterxml.jackson.databind;
    exports org.example.mealwise;
    
    exports org.example.mealwise.controllers.ForgotPasswordControllers;
    opens org.example.mealwise.controllers.ForgotPasswordControllers to javafx.fxml;
    exports org.example.mealwise.dao;
    opens org.example.mealwise.dao to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.mealwise.models;
    opens org.example.mealwise.models to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.mealwise.utils;
    opens org.example.mealwise.utils to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.mealwise.services;
    opens org.example.mealwise.services to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.mealwise.database;
    opens org.example.mealwise.database to com.fasterxml.jackson.databind, javafx.fxml;
    //exports org.example.mealwise.controllers;
    opens org.example.mealwise.controllers to com.fasterxml.jackson.databind, javafx.fxml;
    exports org.example.mealwise.controllers;
}