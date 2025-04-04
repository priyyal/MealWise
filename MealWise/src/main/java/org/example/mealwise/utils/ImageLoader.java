package org.example.mealwise.utils;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ImageLoader {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static void loadImageAsync(String url, ImageView imageView) {
        new Thread(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("User-Agent", "Mozilla/5.0")
                        .GET()
                        .build();

                HttpResponse<InputStream> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
                if (response.statusCode() == 200) {
                    Image image = new Image(response.body());
                    Platform.runLater(() -> {
                        imageView.setImage(image);

                        if (!image.isBackgroundLoading()) {
                            image.progressProperty().addListener((obs, oldVal, newVal) -> {
                                if (newVal.doubleValue() >= 1.0) {
                                    imageView.setFitWidth(image.getWidth());
                                    imageView.setFitHeight(image.getHeight());
                                }
                            });
                        }
                    });
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
