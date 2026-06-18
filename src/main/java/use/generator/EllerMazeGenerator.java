package use.generator;

import java.util.Random;

import use.model.Maze;

public class EllerMazeGenerator {
    private final Random random = new Random();

    public Maze generate(int rows, int cols) {
        byte[][] rightWall = new byte[rows][cols];
        byte[][] downWall = new byte[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rightWall[i][j] = 1;
                downWall[i][j] = 1;
            }
        }
        // пустая строка из множеств (обычные числа)
        int[] sets = new int[cols];
        int nextSetId = 1;
        for (int i = 0; i < cols; i++) {
            sets[i] = nextSetId++;
        }
        // идем по строкам, не доходя до последней
        for (int row = 0; row < rows; row++) {
            boolean isLastRow = (row == rows - 1);
            // присваиваем множества ячейкам без множества (ячейкам, которые были отделены нижней стеной в предыдущей строке)
            for (int col = 0; col < cols; col++) {
                if (sets[col] == 0) {
                    sets[col] = nextSetId++;
                }
            }
            // решаем ставить ли стенку справа
            processHorizontalWalls(row, sets, rightWall, isLastRow);
            // решаем ставить ли стенку снизу
            if (!isLastRow) {
                sets = processVerticalWalls(row, sets, downWall, cols);
            } else {
                processLastRow(row, sets, rightWall, downWall);
            }

        }
        return new Maze(rows, cols, rightWall, downWall);
    }

    private void processHorizontalWalls(int row, int[] sets, byte[][] rightWall, boolean isLastRow) {
        for (int j = 0; j < sets.length - 1; j++) {
            // ставим стенку если текущая ячейка и ячейка справа принадлежат одному множеству
            if (sets[j] == sets[j + 1]) {
                rightWall[row][j] = 1;
            } else {
                boolean removeAll = isLastRow || random.nextBoolean();
                if (removeAll) {
                    rightWall[row][j] = 0;
                    unionSets(sets, j, j + 1);
                } else {
                    rightWall[row][j] = 1;
                }
            }
        }
        // правая стенка всегда стена
        rightWall[row][sets.length - 1] = 1;
    }

    // заменяем все вхождения второго множества на первое
    private void unionSets(int[] sets, int from, int to) {
        int oldSet = sets[to];
        int newSet = sets[from];
        for (int i = 0; i < sets.length; i++) {
            if (sets[i] == oldSet) {
                sets[i] = newSet;
            }
        }
    }

    private int[] processVerticalWalls(int row, int[] sets, byte[][] downWall, int cols) {
        // массив множеств для следующей строки
        int[] nextRow = new int[cols];
        for (int j = 0; j < sets.length; j++) {
            if (random.nextBoolean()) {
                // стену решили не ставить, множество сохраняется
                downWall[row][j] = 0;
                nextRow[j] = sets[j];
            } else {
                // поставили стену (временно)
                downWall[row][j] = 1;
            }
        }
        // проверяем что у каждого множества есть хотя бы одна ячейка без нижней стены
        for (int j = 0; j < sets.length; j++) {
            int set = sets[j];
            boolean hasPassageDown = false;
            for (int i = 0; i < sets.length; i++) {
                // если есть ячейка с таким же множеством и у нее нет нижней стены
                if (sets[i] == set && downWall[row][i] == 0) {
                    hasPassageDown = true;
                    break;
                }
            }
            // если у множества нет ни одной ячейки без нижей стены
            if (!hasPassageDown) {
                // создаем проход вниз для текущей ячейки
                downWall[row][j] = 0;
                nextRow[j] = set;
            }
        }
        return nextRow;
    }

    private void processLastRow(int row, int[] sets, byte[][] rightWall, byte[][] downWall) {
        // нижние стены для всех ячеек
        for (int j = 0; j < sets.length; j++) {
            downWall[row][j] = 1;
        }
        for (int j = 0; j < sets.length - 1; j++) {
            // если разные множества то убираем стенку
            if (sets[j] != sets[j + 1]) {
                rightWall[row][j] = 0;
                unionSets(sets, j, j + 1);
            }
        }
        rightWall[row][sets.length - 1] = 1;
    }
}
