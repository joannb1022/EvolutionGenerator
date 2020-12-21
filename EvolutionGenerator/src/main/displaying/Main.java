package displaying;

import javafx.application.Application;
import javafx.stage.Stage;

import static runAndData.FileCarer.readFile;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Evolution generator");
        readFile("parameters.json");
        UserInterface userInterface = new UserInterface();
        primaryStage.setScene(userInterface.getPrimScene());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}