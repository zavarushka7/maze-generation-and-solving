package use.controller;

import lombok.Getter;
import use.domain.GenerationService;
import use.domain.SolutionService;
import use.model.Maze;
import use.model.PointMaze;
import use.model.SolutionMaze;

import java.io.IOException;

public class MazeController {
    private final GenerationService generationService;
    private final SolutionService solutionService;
    @Getter
    private Maze currentMaze;

    public MazeController(GenerationService generationService, SolutionService solutionService) {
        this.generationService = generationService;
        this.solutionService = solutionService;
    }

    public Maze generateMaze(int rows, int cols) {
        currentMaze = generationService.generateMaze(rows, cols);
        return currentMaze;
    }

    public void saveMaze(String filePath) throws IOException {
        generationService.saveMaze(currentMaze, filePath);
    }

    public Maze loadMaze(String filePath) throws IOException {
        currentMaze = generationService.loadMaze(filePath);
        return currentMaze;
    }

    public SolutionMaze solveMaze(Maze maze, PointMaze start, PointMaze finish) {
        return solutionService.findWay(maze, start, finish);
    }
}
