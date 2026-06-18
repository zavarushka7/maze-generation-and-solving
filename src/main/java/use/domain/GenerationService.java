package use.domain;

import use.generator.EllerMazeGenerator;
import use.io.FileManager;
import use.model.Maze;

import java.io.IOException;

public class GenerationService {
    private final FileManager fileManager;
    private final EllerMazeGenerator ellerMazeGenerator;

    public GenerationService(FileManager fileManager, EllerMazeGenerator generator) {
        this.fileManager = fileManager;
        this.ellerMazeGenerator = generator;
    }

    public Maze generateMaze(int rows, int cols) {
        return ellerMazeGenerator.generate(rows, cols);
    }

    public Maze loadMaze(String filePath) throws IOException {
        return fileManager.loadMaze(filePath);
    }

    public void saveMaze(Maze maze, String filePath) throws IOException {
        fileManager.saveMaze(maze, filePath);
    }

}
