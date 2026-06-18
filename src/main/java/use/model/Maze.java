package use.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
//Лабиринт
public class Maze {
    //кол-во строк
    private int rows;
    //кол-во колонок
    private int cols;
    //матрица для правых стен
    byte[][] rightWall;
    //матрица для нижних стен
    byte[][] downWall;


    // У нас 1 и о, поэтому все в byte, максимальный размер лабиринта 50х50
    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        rightWall = new byte[50][50];
        downWall = new byte[50][50];
    }

    //Проверяем можно ли подвинуться влево
    // x и y координаты не совпадают с индексами поэтому уменьшаем все на 1
    public Optional<PointMaze> getLeftPoint(int r, int c) {
        if (c == 1) return Optional.empty();
        else {
            if (rightWall[r - 1][c - 1 - 1] == 0) return Optional.of(new PointMaze(r, c - 1));
        }
        return Optional.empty();
    }

    //Проверяем можно ли подвинуться вправо
    public Optional<PointMaze> getRightPoint(int r, int c) {
        if (rightWall[r - 1][c - 1] == 0) return Optional.of(new PointMaze(r, c + 1));
        return Optional.empty();
    }

    //Проверяем можно ли подвинуться вниз
    public Optional<PointMaze> getDownPoint(int r, int c) {
        if (downWall[r - 1][c - 1] == 0) return Optional.of(new PointMaze(r + 1, c));
        return Optional.empty();
    }

    //Проверяем можно ли подвинуться вверх
    public Optional<PointMaze> getUpPoint(int r, int c) {
        if (r == 1) return Optional.empty();
        else {
            if (downWall[r - 1 - 1][c - 1] == 0) return Optional.of(new PointMaze(r - 1, c));
        }
        return Optional.empty();
    }

    public boolean hasRightWall(int i, int j) {
        return rightWall[i][j] == 1;
    }

    public boolean hasDownWall(int i, int j) {
        return downWall[i][j] == 1;
    }
}
