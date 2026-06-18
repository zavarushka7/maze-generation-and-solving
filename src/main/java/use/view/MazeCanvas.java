package use.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;
import use.model.Maze;
import use.model.PointMaze;

import java.util.List;

public class MazeCanvas extends Canvas {

    private static final int CANVAS_SIZE = 500;
    private static final double WALL_THICKNESS = 2.0;

    private Maze maze;
    private Color wallColor = Color.web("#d81b60");
    private Color backgroundColor = Color.web("#fce4ec");
    private Color startColor = Color.web("#f06292");
    private Color endColor = Color.web("#d81b60");

    @Getter
    private Point startPoint;
    @Getter
    private Point endPoint;
    private Point mouseHoverPoint;
    @Getter
    @Setter
    private SelectionMode selectionMode = SelectionMode.START;
    private PointSelectionListener listener;
    private List<PointMaze> solutionPath;

    public MazeCanvas() {
        super(CANVAS_SIZE, CANVAS_SIZE);
        setStyle("-fx-background-color: #fce4ec;");
        setFocusTraversable(true);

        widthProperty().addListener((obs, oldVal, newVal) -> draw());
        heightProperty().addListener((obs, oldVal, newVal) -> draw());

        setOnMouseClicked(this::handleMouseClick);
        setOnMouseMoved(this::handleMouseMove);
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        this.startPoint = null;
        this.endPoint = null;
        this.solutionPath = null;
        draw();
    }

    public void clear() {
        this.maze = null;
        this.startPoint = null;
        this.endPoint = null;
        this.solutionPath = null;
        draw();
    }

    public void setSolutionPath(List<PointMaze> path) {
        this.solutionPath = path;
        draw();
    }

    public void clearSolution() {
        this.solutionPath = null;
        draw();
    }


    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, getWidth(), getHeight());

        if (maze == null) {
            drawEmptyState(gc);
            return;
        }

        int rows = maze.getRows();
        int cols = maze.getCols();

        if (rows == 0 || cols == 0) {
            drawEmptyState(gc);
            return;
        }

        drawMaze(gc, rows, cols);
        drawSolution(gc, rows, cols);
        drawPoints(gc, rows, cols);
    }

    private void drawSolution(GraphicsContext gc, int rows, int cols) {
        if (solutionPath == null || solutionPath.size() < 2) return;

        double cellSize = Math.min(getWidth() / cols, getHeight() / rows);
        double offsetX = (getWidth() - cellSize * cols) / 2;
        double offsetY = (getHeight() - cellSize * rows) / 2;

        gc.setStroke(Color.web("#42A5F5"));
        gc.setLineWidth(3.0);

        for (int i = 0; i < solutionPath.size() - 1; i++) {
            PointMaze p1 = solutionPath.get(i);
            PointMaze p2 = solutionPath.get(i + 1);

            double x1 = offsetX + (p1.getCols() - 1) * cellSize + cellSize / 2;
            double y1 = offsetY + (p1.getRows() - 1) * cellSize + cellSize / 2;
            double x2 = offsetX + (p2.getCols() - 1) * cellSize + cellSize / 2;
            double y2 = offsetY + (p2.getRows() - 1) * cellSize + cellSize / 2;

            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    private void drawEmptyState(GraphicsContext gc) {
        gc.setFill(Color.web("#d81b60"));
        gc.setFont(Font.font(16));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("No maze loaded.\nGenerate or load a maze.", getWidth() / 2, getHeight() / 2);
    }

    private void drawMaze(GraphicsContext gc, int rows, int cols) {
        // вычисляем размер ячейки так, чтобы лабиринт занимал всё поле 500 на 500
        double cellSize = Math.min(getWidth() / cols, getHeight() / rows);
        double offsetX = (getWidth() - cellSize * cols) / 2;
        double offsetY = (getHeight() - cellSize * rows) / 2;

        gc.setStroke(wallColor);
        gc.setLineWidth(WALL_THICKNESS);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = offsetX + col * cellSize;
                double y = offsetY + row * cellSize;

                if (row == 0) {
                    gc.strokeLine(x, y, x + cellSize, y);
                } else if (maze.hasDownWall(row - 1, col)) {
                    gc.strokeLine(x, y, x + cellSize, y);
                }

                if (col == 0) {
                    gc.strokeLine(x, y, x, y + cellSize);
                } else if (maze.hasRightWall(row, col - 1)) {
                    gc.strokeLine(x, y, x, y + cellSize);
                }

                if (col == cols - 1) {
                    gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize);
                } else if (maze.hasRightWall(row, col)) {
                    gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize);
                }

                if (row == rows - 1) {
                    gc.strokeLine(x, y + cellSize, x + cellSize, y + cellSize);
                } else if (maze.hasDownWall(row, col)) {
                    gc.strokeLine(x, y + cellSize, x + cellSize, y + cellSize);
                }
            }
        }
    }

    private void drawPoints(GraphicsContext gc, int rows, int cols) {
        double cellSize = Math.min(getWidth() / cols, getHeight() / rows);
        double offsetX = (getWidth() - cellSize * cols) / 2;
        double offsetY = (getHeight() - cellSize * rows) / 2;

        if (mouseHoverPoint != null && maze != null) {
            double x = offsetX + mouseHoverPoint.col * cellSize;
            double y = offsetY + mouseHoverPoint.row * cellSize;
            gc.setFill(Color.web("#f8bbd0", 0.3));
            gc.fillRect(x, y, cellSize, cellSize);
        }

        if (startPoint != null) {
            double x = offsetX + startPoint.col * cellSize + cellSize / 2;
            double y = offsetY + startPoint.row * cellSize + cellSize / 2;
            double radius = Math.min(cellSize / 4, 15);

            gc.setFill(startColor);
            gc.setGlobalAlpha(0.9);
            gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            gc.setGlobalAlpha(1.0);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(Math.min(cellSize / 3, 16)));
            gc.setTextAlign(TextAlignment.CENTER);
            double fontSize = Math.min(cellSize / 3, 16);
            double yOffset = fontSize * 0.35;
            gc.fillText("S", x, y + yOffset);
            gc.setGlobalAlpha(1.0);
        }

        if (endPoint != null) {
            double x = offsetX + endPoint.col * cellSize + cellSize / 2;
            double y = offsetY + endPoint.row * cellSize + cellSize / 2;
            double radius = Math.min(cellSize / 4, 15);

            gc.setFill(endColor);
            gc.setGlobalAlpha(0.9);
            gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            gc.setGlobalAlpha(1.0);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(Math.min(cellSize / 3, 16)));
            gc.setTextAlign(TextAlignment.CENTER);
            double fontSize = Math.min(cellSize / 3, 16);
            double yOffset = fontSize * 0.35;
            gc.fillText("E", x, y + yOffset);
            gc.setGlobalAlpha(1.0);
        }
    }

    private void handleMouseClick(MouseEvent event) {
        if (maze == null) return;

        double cellSize = Math.min(getWidth() / maze.getCols(), getHeight() / maze.getRows());
        double offsetX = (getWidth() - cellSize * maze.getCols()) / 2;
        double offsetY = (getHeight() - cellSize * maze.getRows()) / 2;

        int col = (int) ((event.getX() - offsetX) / cellSize);
        int row = (int) ((event.getY() - offsetY) / cellSize);

        if (col < 0 || col >= maze.getCols() || row < 0 || row >= maze.getRows()) {
            return;
        }

        Point clickedPoint = new Point(row, col);

        if (startPoint != null && startPoint.equals(clickedPoint)) {
            startPoint = null;
            if (listener != null) listener.onStartPointRemoved();
            draw();
            return;
        }

        if (endPoint != null && endPoint.equals(clickedPoint)) {
            endPoint = null;
            if (listener != null) listener.onEndPointRemoved();
            draw();
            return;
        }

        if (selectionMode == SelectionMode.START) {
            startPoint = clickedPoint;
            if (listener != null) listener.onStartPointSelected(row, col);
        } else {
            endPoint = clickedPoint;
            if (listener != null) listener.onEndPointSelected(row, col);
        }

        draw();
    }

    private void handleMouseMove(MouseEvent event) {
        if (maze == null) {
            mouseHoverPoint = null;
            draw();
            return;
        }

        double cellSize = Math.min(getWidth() / maze.getCols(), getHeight() / maze.getRows());
        double offsetX = (getWidth() - cellSize * maze.getCols()) / 2;
        double offsetY = (getHeight() - cellSize * maze.getRows()) / 2;

        int col = (int) ((event.getX() - offsetX) / cellSize);
        int row = (int) ((event.getY() - offsetY) / cellSize);

        if (col < 0 || col >= maze.getCols() || row < 0 || row >= maze.getRows()) {
            mouseHoverPoint = null;
        } else {
            mouseHoverPoint = new Point(row, col);
        }

        draw();
    }

    public void clearPoints() {
        this.startPoint = null;
        this.endPoint = null;
        draw();
    }

    public void setPointSelectionListener(PointSelectionListener listener) {
        this.listener = listener;
    }

    public static class Point {
        public int row;
        public int col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    public enum SelectionMode {
        START, END
    }

    public interface PointSelectionListener {
        void onStartPointSelected(int row, int col);

        void onEndPointSelected(int row, int col);

        void onStartPointRemoved();

        void onEndPointRemoved();
    }
}