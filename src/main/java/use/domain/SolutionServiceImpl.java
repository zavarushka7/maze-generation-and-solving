package use.domain;

import use.model.Maze;
import use.model.PointBFS;
import use.model.PointMaze;
import use.model.SolutionMaze;

import java.util.*;


public class SolutionServiceImpl implements SolutionService {

    //Реализация BFS
    @Override
    public SolutionMaze findWay(Maze maze, PointMaze start, PointMaze finish) {

        //Очередь для клеток, которые нужно посетить
        Queue<PointBFS> queue = new ArrayDeque<>();
        //Добавляем стартовую клетку, родитель нулл
        queue.offer(new PointBFS(start, null));

        //Массив посещенных клеток
        boolean[][] visit = new boolean[maze.getRows()][maze.getCols()];
        //Карта посещенных клеток, (клетка/родитель)
        Map<PointMaze, PointMaze> perent = new HashMap<>();

        perent.put(start, null);

        //Флаг - нашли ли мы финал
        boolean flag = false;

        while (!queue.isEmpty() && !flag) {
            //Достали элемент из очереди
            var currentBFS = queue.poll();
            //Отметили, что мы его посетили
            visit[currentBFS.getCurrent().getRows() - 1][currentBFS.getCurrent().getCols() - 1] = true;

            if (currentBFS.getCurrent().equals(finish)) {

                flag = true;
            } else {
                updateQueue(queue, visit, perent, currentBFS.getCurrent(), maze);
            }
        }

        List<PointMaze> way = new ArrayList<>();
        //собираем путь в лист
        if (flag) {
            var current = finish;
            while (current != null) {
                way.add(current);
                current = perent.get(current);
            }
        }
        Collections.reverse(way);

        return new SolutionMaze(way);
    }


    //Обновление доступных клеток
    private void updateQueue(Queue<PointBFS> queue, boolean[][] visit, Map<PointMaze, PointMaze> perent, PointMaze current, Maze maze) {
        //получаем список доступных клеток
        List<PointMaze> list = avaiblePoint(current, maze);
        //Перебираем список
        for (PointMaze p : list) {
            //Проверка посещали ли мы ее до этого
            if (!visit[p.getRows() - 1][p.getCols() - 1]) {
                //Добавляем в очередь
                queue.offer(new PointBFS(p, current));
                //Создаем узел с родителем
                perent.put(p, current);
            }
        }
    }


    //нахождение доступных клеток куда можно встать
    private List<PointMaze> avaiblePoint(PointMaze current, Maze maze) {
        List<PointMaze> list = new ArrayList<>();
        // если есть точка куда можно сходить до добавляем ее в лист
        maze.getLeftPoint(current.getRows(), current.getCols()).ifPresent(list::add);
        maze.getRightPoint(current.getRows(), current.getCols()).ifPresent(list::add);
        maze.getUpPoint(current.getRows(), current.getCols()).ifPresent(list::add);
        maze.getDownPoint(current.getRows(), current.getCols()).ifPresent(list::add);
        return list;
    }


}
