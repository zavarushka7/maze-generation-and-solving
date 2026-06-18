package use;

import javafx.application.Application;
import javafx.stage.Stage;
import use.controller.MazeController;
import use.domain.GenerationService;
import use.domain.SolutionService;

import use.domain.SolutionServiceImpl;
import use.generator.EllerMazeGenerator;
import use.io.FileManager;
import use.view.MainLayout;

public class Main extends Application {

    private MazeController controller;

    @Override
    public void start(Stage primaryStage) {
        FileManager fileManager = new FileManager();
        EllerMazeGenerator generator = new EllerMazeGenerator();
        GenerationService generationService = new GenerationService(fileManager, generator);
        SolutionService solutionService = new SolutionServiceImpl();
        controller = new MazeController(generationService, solutionService);

        MainLayout mainLayout = new MainLayout(controller, primaryStage);

        primaryStage.setTitle("Maze");
        primaryStage.setScene(mainLayout.getScene());
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}