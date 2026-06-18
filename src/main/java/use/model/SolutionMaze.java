package use.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
//Arraylist для решения лабиринта, решение будет записываться в виде координат
// в коллекции
public class SolutionMaze {
    private List<PointMaze> list;
}
