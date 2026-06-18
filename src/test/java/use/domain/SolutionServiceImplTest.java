package use.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import use.model.Maze;
import use.model.PointMaze;

public class SolutionServiceImplTest {


    @Test
    void testWay1() {
        byte[][] rightWall = {
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 1},
                {0, 1, 1, 1, 0, 0, 0, 1, 1, 1},
                {1, 0, 1, 0, 0, 1, 1, 1, 1, 1},
                {1, 0, 0, 1, 0, 0, 1, 0, 1, 1},
                {0, 0, 1, 0, 1, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 0, 1, 1},
                {0, 0, 0, 1, 1, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 1, 0, 1, 1, 0, 1},
                {1, 0, 0, 0, 1, 1, 1, 0, 0, 1},
                {0, 1, 0, 1, 0, 1, 0, 0, 0, 1}
        };

        byte[][] downWall = {
                {0, 1, 0, 0, 0, 1, 1, 1, 0, 0},
                {1, 1, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 0, 0, 1, 0, 0, 1, 0},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 0},
                {0, 1, 1, 1, 0, 1, 0, 0, 0, 0},
                {1, 1, 0, 1, 0, 0, 1, 0, 1, 1},
                {0, 1, 1, 1, 1, 0, 0, 0, 1, 0},
                {1, 0, 1, 0, 0, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        Maze maze = new Maze(10, 10, rightWall, downWall);
        SolutionServiceImpl service = new SolutionServiceImpl();
        var way = service.findWay(maze, new PointMaze(1, 10), new PointMaze(10, 10));
        //если хочешь, то можешь раскомментировать и проследить как он идет
//        for (PointMaze p :way.getList()){
//            System.out.println(p.getRows()+"\t"+p.getCols());
//        }

        Assertions.assertEquals(20, way.getList().size());
    }

    @Test
    void testWay2() {
        byte[][] rightWall = {
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 1},
                {0, 1, 1, 1, 0, 0, 0, 1, 1, 1},
                {1, 0, 1, 0, 0, 1, 1, 1, 1, 1},
                {1, 0, 0, 1, 0, 0, 1, 0, 1, 1},
                {0, 0, 1, 0, 1, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 0, 1, 1},
                {0, 0, 0, 1, 1, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 1, 0, 1, 1, 0, 1},
                {1, 0, 0, 0, 1, 1, 1, 0, 0, 1},
                {0, 1, 0, 1, 0, 1, 0, 0, 0, 1}
        };

        byte[][] downWall = {
                {0, 1, 0, 0, 0, 1, 1, 1, 0, 0},
                {1, 1, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 0, 0, 1, 0, 0, 1, 0},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 0},
                {0, 1, 1, 1, 0, 1, 0, 0, 0, 0},
                {1, 1, 0, 1, 0, 0, 1, 0, 1, 1},
                {0, 1, 1, 1, 1, 0, 0, 0, 1, 0},
                {1, 0, 1, 0, 0, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        Maze maze = new Maze(10, 10, rightWall, downWall);
        SolutionServiceImpl service = new SolutionServiceImpl();
        var way = service.findWay(maze, new PointMaze(1, 1), new PointMaze(10, 10));
        //если хочешь, то можешь раскомментировать и проследить как он идет
//        for (PointMaze p : way.getList()) {
//            System.out.println(p.getRows() + "\t" + p.getCols());
//        }

        Assertions.assertEquals(49, way.getList().size());
    }

    @Test
    void testWay3() {
        byte[][] rightWall = {
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 1},
                {0, 1, 1, 1, 0, 0, 0, 1, 1, 1},
                {1, 0, 1, 0, 0, 1, 1, 1, 1, 1},
                {1, 0, 0, 1, 0, 0, 1, 0, 1, 1},
                {0, 0, 1, 0, 1, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 0, 1, 1},
                {0, 0, 0, 1, 1, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 1, 0, 1, 1, 0, 1},
                {1, 0, 0, 0, 1, 1, 1, 0, 0, 1},
                {0, 1, 0, 1, 0, 1, 0, 0, 0, 1}
        };

        byte[][] downWall = {
                {0, 1, 0, 0, 0, 1, 1, 1, 0, 0},
                {1, 1, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 0, 0, 1, 0, 0, 1, 0},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 0},
                {0, 1, 1, 1, 0, 1, 0, 0, 0, 0},
                {1, 1, 0, 1, 0, 0, 1, 0, 1, 1},
                {0, 1, 1, 1, 1, 0, 0, 0, 1, 0},
                {1, 0, 1, 0, 0, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        Maze maze = new Maze(10, 10, rightWall, downWall);
        SolutionServiceImpl service = new SolutionServiceImpl();
        var way = service.findWay(maze, new PointMaze(10, 1), new PointMaze(10, 10));
        //если хочешь, то можешь раскомментировать и проследить как он идет
//        for (PointMaze p : way.getList()) {
//            System.out.println(p.getRows() + "\t" + p.getCols());
//        }

        Assertions.assertEquals(16, way.getList().size());
    }


}
