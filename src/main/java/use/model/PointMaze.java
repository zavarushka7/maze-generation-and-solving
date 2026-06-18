package use.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Координаты для лабиринта
public class PointMaze {
    private int rows;
    private int cols;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointMaze pointMaze = (PointMaze) o;
        return rows == pointMaze.rows && cols == pointMaze.cols;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, cols);
    }
}
