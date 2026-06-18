package use.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import lombok.Getter;
import use.controller.MazeController;

import java.util.Random;

@Getter
public class MainLayout {
    private final Scene scene;
    private final MazeCanvas canvas;
    private final ControlsPanel controlsPanel;

    private Star[] stars;
    private Random random = new Random();
    private Canvas starCanvas;

    public MainLayout(MazeController controller, Stage stage) {
        this.canvas = new MazeCanvas();
        this.controlsPanel = new ControlsPanel(controller, canvas, stage);

        starCanvas = new Canvas(1000, 800);

        // оборачиваем canvas в StackPane для центрирования
        StackPane canvasWrapper = new StackPane();
        canvasWrapper.setStyle("-fx-background-color: transparent;");
        canvasWrapper.getChildren().add(canvas);
        // центрируем canvas внутри wrapper
        StackPane.setAlignment(canvas, javafx.geometry.Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(canvasWrapper);
        root.setBottom(controlsPanel);
        root.setStyle(
                "-fx-padding: 15;" +
                        "-fx-background-color: transparent;"
        );

        canvas.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ec407a;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(233, 30, 99, 0.3), 10, 0, 0, 5);"
        );

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(starCanvas);
        stackPane.getChildren().add(root);
        StackPane.setAlignment(root, javafx.geometry.Pos.CENTER);

        this.scene = new Scene(stackPane, 1000, 800);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double height = scene.getHeight();
            starCanvas.setWidth(width);
            starCanvas.setHeight(height);
            generateStars(width, height);
            drawStarsOnCanvas();
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            double height = newVal.doubleValue();
            double width = scene.getWidth();
            starCanvas.setHeight(height);
            starCanvas.setWidth(width);
            generateStars(width, height);
            drawStarsOnCanvas();
        });

        generateStars(1000, 800);
        drawStarsOnCanvas();
    }

    private void generateStars(double width, double height) {
        stars = new Star[120];
        for (int i = 0; i < stars.length; i++) {
            double x = random.nextDouble() * width;
            double y = random.nextDouble() * height;
            double size = 5 + random.nextDouble() * 10;
            double opacity = 0.3 + random.nextDouble() * 0.5;
            stars[i] = new Star(x, y, size, opacity);
        }
    }

    private void drawStarsOnCanvas() {
        if (starCanvas == null) return;

        GraphicsContext gc = starCanvas.getGraphicsContext2D();
        double width = starCanvas.getWidth();
        double height = starCanvas.getHeight();

        LinearGradient gradient = new LinearGradient(
                0, 0, width, height,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#fce4ec")),
                new Stop(0.5, Color.web("#f8bbd0")),
                new Stop(1, Color.web("#f48fb1"))
        );

        gc.setFill(gradient);
        gc.fillRect(0, 0, width, height);

        if (stars == null || stars.length == 0) {
            generateStars(width, height);
        }

        for (Star star : stars) {
            gc.setFill(Color.web("#ec407a"));
            gc.setGlobalAlpha(star.opacity);

            double halfSize = star.size / 2;
            gc.fillOval(star.x - halfSize, star.y - 1, star.size, 2);
            gc.fillOval(star.x - 1, star.y - halfSize, 2, star.size);

            gc.setGlobalAlpha(star.opacity * 0.8);
            gc.fillOval(star.x - 2, star.y - 2, 4, 4);
        }
        gc.setGlobalAlpha(1.0);
    }

    private static class Star {
        double x, y;
        double size;
        double opacity;

        Star(double x, double y, double size, double opacity) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.opacity = opacity;
        }
    }
}