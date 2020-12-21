package displaying;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import mapElements.Animal;
import mapElements.MapElement;
import mapElements.MoveDirection;
import mapElements.Vector2d;
import runAndData.GlobalVariables;
import runAndData.SimulationEngine;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static runAndData.FileCarer.writeData;

public class SimulationDrawer implements SimulationDrawerInterface {
    GridPane grid;
    Button pausePlay, getStats;
    BorderPane layout;
    HBox up;
    SimulationEngine simulationEngine;
    Label plants, animals, gene,energyLevel, day, lifeLength, amountOfChildren;

    SimulationDrawer(){
        this.grid = new GridPane();
        this.simulationEngine = new SimulationEngine();
        this.pausePlay = new Button("play");
        this.getStats = new Button("get stats");
        this.layout = new BorderPane();
        this.pausePlay.setOnAction(e ->{
            this.simulationEngine.setSimulationStatus(!this.simulationEngine.getSimulationStatus());
            if(this.simulationEngine.getSimulationStatus())
                pausePlay.setText("pause");
            else
                pausePlay.setText("play");
        });
        this.getStats.setOnAction(e -> {
            try {
                List<String> lines = Arrays.asList("Day", this.day.getText(),
                        "Amount of plants ", this.plants.getText(),
                        "Dominant gene", this.gene.getText(),
                        "Average energy level", this.energyLevel.getText(),
                        "Average life length", this.lifeLength.getText(),
                        "Average amount of children", this.amountOfChildren.getText());
                writeData("statistics.txt",lines);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        HBox down = new HBox(this.pausePlay);
        down.setAlignment(Pos.CENTER);
        VBox statisticBox = new VBox();
        this.plants = new Label("0");
        this.animals = new Label("0");
        this.gene = new Label("x");
        this.energyLevel = new Label("0");
        this.day = new Label("0");
        this.lifeLength = new Label("0");
        this.amountOfChildren = new Label("0");
        statisticBox.getChildren().addAll(new Label("Day"), this.day,
                new Label("Amount of plants "),this.plants
                , new Label("Amount of animals "), this.animals,
                new Label("Dominant gene"), this.gene,
                new Label("Average energy level"), this.energyLevel,
                new Label("Average life length"), this.lifeLength,
                new Label("Average amount of children"), this.amountOfChildren,
                this.getStats);
        statisticBox.setAlignment(Pos.CENTER);
        this.up = new HBox(this.grid);
        this.layout.setCenter(this.up);
        this.layout.setBottom(down);
        this.layout.setRight(statisticBox);
    }

    @Override
    public void drawMap() {
        int w = this.simulationEngine.getMap().getWidth(), h = this.simulationEngine.getMap().getHeight();
        this.grid = new GridPane();
        for (int j = h; j >= -1; j--) {
            for (int i = -1; i <= w; i++) {
                Rectangle r = new Rectangle((GlobalVariables.sceneWidth) * 0.9 / (w + 2), (GlobalVariables.sceneHeight) * 0.9 / (h + 2));
                if ((j == -1 || i == -1) || (j == h || i == w))
                    r.setFill(Color.WHITE);
                else if (this.simulationEngine.getMap().getElements().containsKey(new Vector2d(i, j))) {
                    if (this.simulationEngine.getMap().getElements().get(new Vector2d(i, j)).stream().anyMatch(a -> a instanceof Animal)){
                        List<Animal> animals = new ArrayList<>();
                        Set<MapElement> mapElements = this.simulationEngine.getMap().getElements().get(new Vector2d(i,j));
                        for(MapElement m : mapElements) {
                            if (m instanceof Animal)
                                animals.add((Animal) m);
                        }
                        Animal strongest = this.simulationEngine.getStrongestAnimal(animals);
                        int energyOfStrAnimal = strongest.getEnergyLevel();
                        if(energyOfStrAnimal > GlobalVariables.startEnergy*0.75)
                            r.setFill(Color.MAGENTA);
                        else if(energyOfStrAnimal > GlobalVariables.startEnergy*0.5)
                            r.setFill(Color.LIGHTPINK);
                        else
                            r.setFill(Color.LAVENDER);
                        r.setOnMouseClicked(event ->{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            AtomicInteger n = new AtomicInteger(0);
                            strongest.getNumberOfDesc(n);
                            alert.setTitle("Animal Info");
                            alert.setHeaderText("Animal id: " + strongest.getId());
                            alert.setContentText("Genotype: " + Arrays.toString(strongest.getGenes()) +
                                    "\nNumber of children: " + strongest.getChildren().size() +
                                    "\nNumber of descendants: " + n);

                            alert.showAndWait();
                        });
                    }
                    else
                        r.setFill(Color.GREEN);
                } else {
                    if (this.simulationEngine.getMap().inJungle(new Vector2d(i, j)))
                        r.setFill(Color.LIGHTGREEN);
                    else
                        r.setFill(Color.GOLDENROD);
                }
                this.grid.add(r, i + 1, j + 1);
            }
        }
        this.grid.setHgap((GlobalVariables.sceneHeight*0.02)/(h+3));
        this.grid.setVgap((GlobalVariables.sceneWidth*0.02)/(w+3));
    }

    public void updateSimulation(){
        this.simulationEngine.run();
        this.up.getChildren().remove(this.getGrid());
        this.up.getChildren().add(getActualGrid());
        this.up.setAlignment(Pos.BOTTOM_CENTER);
        setLabels();
    }

    void setLabels(){
        this.plants.setText(String.valueOf(this.simulationEngine.getGlobalVariables().plantCounter));
        this.animals.setText(String.valueOf(this.simulationEngine.getGlobalVariables().livingAnimalCounter));
        this.gene.setText(MoveDirection.values()[this.simulationEngine.getMaxGene()].toString());
        this.energyLevel.setText(String.valueOf(this.simulationEngine.getAverageAnimalEnergy()));
        this.day.setText(String.valueOf(this.simulationEngine.getGlobalVariables().dayCounter));
        if(this.simulationEngine.getGlobalVariables().animalCounter > 0)
            this.amountOfChildren.setText(String.format("%.2f",(double)this.simulationEngine.animalsChildren()/
                (double)this.simulationEngine.getGlobalVariables().animalCounter));
        else
            this.amountOfChildren.setText("0");
        this.lifeLength.setText(String.format("%.2f",this.simulationEngine.getGlobalVariables().averageLifeLength));
    }

    public GridPane getGrid() {
        return grid;
    }

    public GridPane getActualGrid(){
        drawMap();
        return this.grid;
    }

    @Override
    public void pauseSimulation() throws InterruptedException {
        while(!this.simulationEngine.getSimulationStatus()){
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                System.out.println("Thread interrupted");
            }
        }
    }

    public BorderPane getLayout() { return this.layout; }

}
