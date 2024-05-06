package com.example.coursework;

import com.example.coursework.fxControllers.FxUtils;
import com.example.coursework.fxControllers.FxUtils;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        FxUtils.setStageParameters(stage, scene, false);

        //EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Shop");
    }

    public static void main(String[] args) {
        launch();
    }
}