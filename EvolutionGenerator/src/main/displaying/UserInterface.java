package displaying;


import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import runAndData.GlobalVariables;

public class UserInterface {

    Scene primScene;

    UserInterface(){
        Button s1= new Button("Simulation 1");
        Button s2 = new Button("Simulation 2");
        TextField text= new TextField();
        buildButton(s1,text,1);
        buildButton(s2,text,2);
        text.setMaxWidth(100);
        VBox layout= new VBox(5);
        Label label= new Label("Enter number of animals: ");
        layout.getChildren().addAll(label, text, s1, s2);
        layout.setAlignment(Pos.CENTER);
        this.primScene = new Scene(layout, 300, 250);;
    }

    void buildButton(Button b, TextField text, int x) {
        b.setOnAction(action ->
                {
                    GlobalVariables.startAnimals = Integer.parseInt(text.getText());
                    Stage simulationX = new Stage();
                    simulationX.setTitle("Simulation " + x);
                    SimulationDrawer simX = new SimulationDrawer();
                    Scene layout = new Scene(simX.getLayout(), GlobalVariables.sceneWidth + 100, GlobalVariables.sceneHeight);
                    simulationX.setScene(layout);
                    Thread thread = new Thread(() -> {
                        Runnable updater = simX::updateSimulation;
                        while (true) {
                            try {
                                simX.pauseSimulation();
                                Thread.sleep(simX.simulationEngine.getInterval());
                            } catch (InterruptedException e) {
                                System.out.println("Thread interrupted!");
                            }
                            Platform.runLater(updater);
                        }
                    });
                    thread.setDaemon(true);
                    thread.start();
                    simulationX.show();
                }
        );
    }

    Scene getPrimScene(){return this.primScene;}
}
