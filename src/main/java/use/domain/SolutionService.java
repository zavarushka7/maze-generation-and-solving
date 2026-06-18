package use.domain;

import use.model.Maze;
import use.model.PointMaze;
import use.model.SolutionMaze;

//Решение лабиринта
public interface SolutionService {

    // Нахождение пути от начальной до конечной точки,
    // на вход принимает лабиринт и координаты начала и конца.
    // Отдает List координат пути
    SolutionMaze findWay(Maze maze, PointMaze start, PointMaze finish);

}
