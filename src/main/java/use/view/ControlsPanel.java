package use.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import use.controller.MazeController;
import use.model.Maze;
import use.model.PointMaze;
import use.model.SolutionMaze;

import java.io.File;
import java.io.IOException;


public class ControlsPanel extends VBox {
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 50;

    private final MazeController controller;
    private final MazeCanvas canvas;
    private final Stage stage;

    private TextField rowsField;
    private TextField colsField;
    private Button generateBtn;
    private Button loadBtn;
    private Button saveBtn;
    private Button clearBtn;
    private Button solveBtn;
    private Button clearPointsBtn;
    private ToggleGroup modeGroup;
    private RadioButton startModeBtn;
    private RadioButton endModeBtn;
    private Label statusLabel;

    public ControlsPanel(MazeController controller, MazeCanvas canvas, Stage stage) {
        this.controller = controller;
        this.canvas = canvas;
        this.stage = stage;

        setSpacing(10);
        setPadding(new Insets(15, 10, 15, 10));

        initComponents();
        setupEventHandlers();
    }

    private void initComponents() {
        // панель генерации
        HBox generatePanel = createGeneratePanel();

        // панель выбора точек
        HBox pointPanel = createPointPanel();

        // панель файловых операций
        HBox filePanel = createFilePanel();

        // статус бар
        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #c2185b; -fx-font-size: 12px;");
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(generatePanel, pointPanel, filePanel, statusLabel);
    }

    private HBox createGeneratePanel() {
        HBox panel = new HBox(10);
        panel.setAlignment(Pos.CENTER);

        Label rowsLabel = new Label("Rows:");
        rowsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #880e4f;");

        rowsField = new TextField("10");
        rowsField.setPrefWidth(60);
        rowsField.setPromptText("1-50");
        rowsField.setStyle("-fx-border-color: #f06292; -fx-border-radius: 3px;");

        Label colsLabel = new Label("Cols:");
        colsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #880e4f;");

        colsField = new TextField("10");
        colsField.setPrefWidth(60);
        colsField.setPromptText("1-50");
        colsField.setStyle("-fx-border-color: #f06292; -fx-border-radius: 3px;");

        generateBtn = new Button("Generate");
        generateBtn.setStyle("-fx-font-weight: bold; -fx-background-color: #ec407a; -fx-text-fill: white; -fx-background-radius: 5px;");
        generateBtn.setPrefWidth(100);

        panel.getChildren().addAll(
                rowsLabel, rowsField,
                colsLabel, colsField,
                generateBtn
        );

        return panel;
    }

    private HBox createPointPanel() {
        HBox panel = new HBox(10);
        panel.setAlignment(Pos.CENTER);
        panel.setStyle("-fx-padding: 5 0 5 0;");

        Label modeLabel = new Label("Select:");
        modeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #880e4f;");

        modeGroup = new ToggleGroup();

        startModeBtn = new RadioButton("Start");
        startModeBtn.setToggleGroup(modeGroup);
        startModeBtn.setSelected(true);
        startModeBtn.setStyle("-fx-text-fill: #f06292; -fx-font-weight: bold;");

        endModeBtn = new RadioButton("End");
        endModeBtn.setToggleGroup(modeGroup);
        endModeBtn.setStyle("-fx-text-fill: #d81b60; -fx-font-weight: bold;");

        solveBtn = new Button("Solve");
        solveBtn.setStyle("-fx-font-weight: bold; -fx-background-color: #ec407a; -fx-text-fill: white; -fx-background-radius: 5px;");
        solveBtn.setPrefWidth(80);
        solveBtn.setDisable(true);

        clearPointsBtn = new Button("Clear Points");
        clearPointsBtn.setStyle("-fx-font-weight: bold; -fx-background-color: #d81b60; -fx-text-fill: white; -fx-background-radius: 5px;");
        clearPointsBtn.setPrefWidth(100);

        panel.getChildren().addAll(
                modeLabel, startModeBtn, endModeBtn, solveBtn, clearPointsBtn
        );

        return panel;
    }

    private HBox createFilePanel() {
        HBox panel = new HBox(10);
        panel.setAlignment(Pos.CENTER);

        loadBtn = new Button("Load");
        loadBtn.setStyle("-fx-background-color: #f06292; -fx-text-fill: white; -fx-background-radius: 5px;");
        loadBtn.setPrefWidth(80);

        saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #f06292; -fx-text-fill: white; -fx-background-radius: 5px;");
        saveBtn.setPrefWidth(80);

        clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #d81b60; -fx-text-fill: white; -fx-background-radius: 5px;");
        clearBtn.setPrefWidth(80);

        panel.getChildren().addAll(loadBtn, saveBtn, clearBtn);

        return panel;
    }

    private void setupEventHandlers() {
        generateBtn.setOnAction(e -> handleGenerate());
        loadBtn.setOnAction(e -> handleLoad());
        saveBtn.setOnAction(e -> handleSave());
        clearBtn.setOnAction(e -> handleClear());
        solveBtn.setOnAction(e -> handleSolve());
        clearPointsBtn.setOnAction(e -> handleClearPoints());

        // выбор режима
        startModeBtn.setOnAction(e -> {
            canvas.setSelectionMode(MazeCanvas.SelectionMode.START);
            setStatus("Select START point by clicking on maze", StatusType.INFO);
        });

        endModeBtn.setOnAction(e -> {
            canvas.setSelectionMode(MazeCanvas.SelectionMode.END);
            setStatus("Select END point by clicking on maze", StatusType.INFO);
        });

        // слушатель для выбора точек
        canvas.setPointSelectionListener(new MazeCanvas.PointSelectionListener() {
            @Override
            public void onStartPointSelected(int row, int col) {
                canvas.clearSolution();
                setStatus("Start point selected at (" + row + 1 + ", " + col + 1 + ")", StatusType.SUCCESS);
                updateSolveButton();
            }

            @Override
            public void onEndPointSelected(int row, int col) {
                canvas.clearSolution();
                setStatus("End point selected at (" + row + 1 + ", " + col + 1 + ")", StatusType.SUCCESS);
                updateSolveButton();
            }

            @Override
            public void onStartPointRemoved() {
                canvas.clearSolution();
                setStatus("Start point removed", StatusType.INFO);
                updateSolveButton();
            }

            @Override
            public void onEndPointRemoved() {
                canvas.clearSolution();
                setStatus("End point removed", StatusType.INFO);
                updateSolveButton();
            }
        });

        rowsField.setOnAction(e -> handleGenerate());
        colsField.setOnAction(e -> handleGenerate());
    }

    private void handleGenerate() {
        try {
            int rows = parseDimension(rowsField.getText());
            int cols = parseDimension(colsField.getText());

            Maze maze = controller.generateMaze(rows, cols);
            canvas.setMaze(maze);
            canvas.clearPoints();
            canvas.clearSolution();
            setStatus("Maze " + rows + "x" + cols + " generated successfully!", StatusType.SUCCESS);
            updateSolveButton();

        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for rows and columns");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Error generating maze: " + e.getMessage());
        }
    }

    private void handleLoad() {
        FileChooser fileChooser = createFileChooser("Load Maze", "*.txt");
        File file = fileChooser.showOpenDialog(stage);

        if (file == null) return;

        try {
            Maze maze = controller.loadMaze(file.getAbsolutePath());
            canvas.setMaze(maze);
            canvas.clearPoints();
            canvas.clearSolution();
            rowsField.setText(String.valueOf(maze.getRows()));
            colsField.setText(String.valueOf(maze.getCols()));

            setStatus("Maze loaded from: " + file.getName(), StatusType.SUCCESS);
            updateSolveButton();

        } catch (IOException e) {
            showError("Error loading maze: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }

    private void handleSave() {
        if (controller.getCurrentMaze() == null) {
            showError("No maze to save. Generate or load a maze first.");
            return;
        }

        FileChooser fileChooser = createFileChooser("Save Maze", "*.txt");
        fileChooser.setInitialFileName("maze.txt");

        File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;

        try {
            controller.saveMaze(file.getAbsolutePath());
            setStatus("Maze saved to: " + file.getName(), StatusType.SUCCESS);

        } catch (IOException e) {
            showError("Error saving maze: " + e.getMessage());
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }

    private void handleSolve() {
        MazeCanvas.Point start = canvas.getStartPoint();
        MazeCanvas.Point end = canvas.getEndPoint();

        if (start == null || end == null) {
            showError("Please select both start and end points");
            return;
        }

        try {
            PointMaze startPoint = new PointMaze(start.row + 1, start.col + 1);
            PointMaze endPoint = new PointMaze(end.row + 1, end.col + 1);

            Maze maze = controller.getCurrentMaze();
            if (maze == null) {
                showError("No maze to solve");
                return;
            }

            SolutionMaze solution = controller.solveMaze(maze, startPoint, endPoint);
            java.util.List<PointMaze> path = solution.getList();

            if (path == null || path.isEmpty()) {
                showError("No path found between selected points!");
                return;
            }

            canvas.setSolutionPath(path);
            setStatus("Solution found! Path length: " + path.size() + " cells", StatusType.SUCCESS);

        } catch (Exception e) {
            showError("Error solving maze: " + e.getMessage());
        }
    }


    private void handleClearPoints() {
        canvas.clearPoints();
        canvas.clearSolution();
        setStatus("Points cleared", StatusType.INFO);
        updateSolveButton();
    }

    private void handleClear() {
        canvas.clear();
        canvas.clearPoints();
        canvas.clearSolution();
        setStatus("Maze cleared", StatusType.INFO);
        updateSolveButton();
    }

    private void updateSolveButton() {
        boolean hasPoints = canvas.getStartPoint() != null && canvas.getEndPoint() != null;
        solveBtn.setDisable(!hasPoints);
    }

    private int parseDimension(String text) {
        text = text.trim();
        if (text.isEmpty()) {
            throw new NumberFormatException("Empty field");
        }

        int value = Integer.parseInt(text);

        if (value < MIN_SIZE || value > MAX_SIZE) {
            throw new IllegalArgumentException(
                    "Dimensions must be between " + MIN_SIZE + " and " + MAX_SIZE +
                            ", got: " + value
            );
        }

        return value;
    }

    private FileChooser createFileChooser(String title, String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", extension)
        );
        fileChooser.setInitialDirectory(new File("."));
        return fileChooser;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().setStyle(
                "-fx-background-color: #fce4ec;" +
                        "-fx-border-color: #ec407a;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 20;"
        );

        alert.showAndWait();
        setStatus(message, StatusType.ERROR);
    }

    private void setStatus(String message, StatusType type) {
        statusLabel.setText(message);

        switch (type) {
            case SUCCESS:
                statusLabel.setStyle("-fx-text-fill: #ad1457; -fx-font-size: 12px; -fx-font-weight: bold;");
                break;
            case ERROR:
                statusLabel.setStyle("-fx-text-fill: #880e4f; -fx-font-size: 12px; -fx-font-weight: bold;");
                break;
            case INFO:
                statusLabel.setStyle("-fx-text-fill: #6a1b9a; -fx-font-size: 12px; -fx-font-weight: bold;");
                break;
            default:
                statusLabel.setStyle("-fx-text-fill: #c2185b; -fx-font-size: 12px;");
        }
    }

    private enum StatusType {
        SUCCESS, ERROR, INFO, DEFAULT
    }
}