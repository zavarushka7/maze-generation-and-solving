package use.io;

import use.model.Maze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class FileManager {
    public Maze loadMaze(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!validateFile(path)) {
            throw new IOException("Invalid file: " + filePath);
        }

        List<String> lines = Files.readAllLines(path);
        int separatorIndex = validateFormat(lines);
        String[] sizes = lines.get(0).trim().split(" ");
        int rows = Integer.parseInt(sizes[0]);
        int cols = Integer.parseInt(sizes[1]);
        byte[][] rightWall = parseMatrix(lines, 1, rows, cols, "Right walls");
        byte[][] downWall = parseMatrix(lines, separatorIndex + 1, rows, cols, "Down walls");
        return new Maze(rows, cols, rightWall, downWall);

    }

    public void saveMaze(Maze maze, String filePath) throws IOException {
        if (maze == null) {
            throw new IllegalArgumentException("Maze cannot be null");
        }
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        Path path = Paths.get(filePath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        List<String> lines = new ArrayList<>();
        lines.add(maze.getRows() + " " + maze.getCols());
        for (int i = 0; i < maze.getRows(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < maze.getCols(); j++) {
                if (j > 0) {
                    sb.append(" ");
                }
                sb.append(maze.hasRightWall(i, j) ? "1" : "0");
            }
            lines.add(sb.toString());
        }
        lines.add("");
        for (int i = 0; i < maze.getRows(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < maze.getCols(); j++) {
                if (j > 0) {
                    sb.append(" ");
                }
                sb.append(maze.hasDownWall(i, j) ? "1" : "0");
            }
            lines.add(sb.toString());
        }
        Files.write(path, lines);
    }

    public boolean validateFile(Path path) {
        if (path == null) {
            return false;
        }
        return Files.exists(path) && Files.isReadable(path) && Files.isRegularFile(path);
    }

    private int validateFormat(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String[] sizes = lines.get(0).trim().split(" ");
        if (sizes.length != 2) {
            throw new IllegalArgumentException("First line must contain rows and cols");
        }
        int rows = Integer.parseInt(sizes[0]);
        int cols = Integer.parseInt(sizes[1]);
        if (rows < 1 || rows > 50 || cols < 1 || cols > 50) {
            throw new IllegalArgumentException("Rows and cols must be between 1 and 50");
        }

        int separatorIndex = -1;
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).trim().isEmpty()) {
                separatorIndex = i;
                break;
            }
        }
        if (separatorIndex == -1 || separatorIndex != rows + 1 || lines.size() - separatorIndex - 1 != rows) {
            throw new IllegalArgumentException("Error in file formatting");
        }
        return separatorIndex;
    }

    private byte[][] parseMatrix(List<String> lines, int startIndex, int rows, int cols, String matrixName) {
        byte[][] matrix = new byte[rows][cols];
        for (int i = 0; i < rows; i++) {
            String[] parts = lines.get(startIndex + i).trim().split(" ");
            if (parts.length != cols) {
                throw new IllegalArgumentException(matrixName + " line " + (i + 1) + ": expected " + cols + " numbers, got " + parts.length);
            }

            for (int j = 0; j < cols; j++) {
                try {
                    int value = Integer.parseInt(parts[j]);
                    if (value != 0 && value != 1) {
                        throw new IllegalArgumentException(matrixName + "[" + i + "][" + j + "]: value must be 0 or 1, got " + value);
                    }
                    matrix[i][j] = (byte) value;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(matrixName + "[" + i + "][" + j + "]: not a number, got " + parts[j]);
                }
            }
        }
        return matrix;
    }

}

