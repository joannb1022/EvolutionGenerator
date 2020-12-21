package runAndData;

import map.WorldMap;
import mapElements.*;
import java.util.*;
import java.util.stream.Collectors;

public class SimulationEngine implements IEngine {

    WorldMap map;
    boolean simulationStatus = false;
    long interval = 250;
    GlobalVariables globalVariables = new GlobalVariables();

    public SimulationEngine() {
        this.map = new WorldMap();
        startAnimals();
        generatePlants();
    }

    public void run() {
        removeDeadAnimals();
        moveAnimals();
        feedAnimals();
        copulation();
        generatePlants();
        this.globalVariables.dayCounter++;
    }

    public void startAnimals() {
        for (int i = 0; i < GlobalVariables.startAnimals; i++) {
            Animal startingAnimal = new Animal(this.map.findStartingPos(), ++this.globalVariables.animalCounter);
            startingAnimal.setSpawnDay(globalVariables.dayCounter);
            startingAnimal.register(this.map);
            this.map.place(startingAnimal);
            this.globalVariables.livingAnimalCounter++;
            for(int j = 0; j < 8; j++)
                this.globalVariables.genoType[j] += startingAnimal.getGenes()[j];
        }
    }


    public Set<Animal> getAnimals() {
        Set<Animal> animals = new HashSet<>();
        for (Map.Entry<Vector2d, Set<MapElement>> entry : this.map.getElements().entrySet()) {
            for (MapElement x : entry.getValue()) {
                if (x instanceof Animal)
                    animals.add((Animal) x);
            }
        }
        return animals;
    }

    public int getAverageAnimalEnergy(){
        List<Animal> a = List.copyOf(getAnimals());
        return a.size() != 0 ? a.stream().mapToInt(Animal::getEnergyLevel).sum()/ a.size() : 0;
    }

    public Set<Plant> getPlants() {
        Set<Plant> plants = new HashSet<>();
        for (Map.Entry<Vector2d, Set<MapElement>> entry : this.map.getElements().entrySet()) {
            for (MapElement x : entry.getValue()) {
                if (x instanceof Plant)
                    plants.add((Plant) x);
            }
        }
        return plants;
    }

    public void generatePlants() {
        Vector2d in = getMap().pickPlaceInJungle(), out = getMap().pickPlaceOutsideJungle();
        if(in != null){
            Plant p = new Plant(this.map);
            p.setPosition(in);
            p.register(this.map);
            this.map.place(p);
            this.globalVariables.plantCounter++;
        }
        if(out != null){
            Plant p = new Plant(this.map);
            p.setPosition(out);
            p.register(this.map);
            this.map.place(p);
            this.globalVariables.plantCounter++;
        }
    }


    public void copulation() {
        List<Animal> kids =  new ArrayList<>();
        for (Map.Entry<Vector2d, Set<MapElement>> entry : this.map.getElements().entrySet()) {
            List<Animal> animals = new ArrayList<>();
            for (MapElement x : entry.getValue()) {
                if (x instanceof Animal)
                    animals.add((Animal) x);
            }
            if (animals.size() >= 2) {
                Animal dad = getStrongestAnimal(animals);
                animals.remove(dad);
                Animal mom = getStrongestAnimal(animals);
                if (mom.canCopulate() && dad.canCopulate()) {
                    Vector2d childPos = this.map.findPosForChild(dad.getPosition());
                    Animal kiddo = new Animal(dad, mom, childPos, ++this.globalVariables.animalCounter);
                    kiddo.setSpawnDay(globalVariables.dayCounter);
                    kiddo.register(this.map);
                    kids.add(kiddo);
                    mom.increaseEnergyLevel(-mom.getEnergyLevel() / 4);
                    dad.increaseEnergyLevel(-dad.getEnergyLevel() / 4);
                    mom.addChild(kiddo);
                    dad.addChild(kiddo);
                    this.globalVariables.livingAnimalCounter++;
                    for(int i = 0; i < 8; i++)
                        this.globalVariables.genoType[i] += kiddo.getGenes()[i];
                }
            }
        }
        kids.forEach(kid -> this.map.place(kid));
    }

    public void feedAnimals() {
        Set<Plant> plants = getPlants();
        Set<Animal> animals = getAnimals();
        for (Plant p : plants) {
            Set<Animal> animalsOnPos = animals.stream().
                    filter(a -> a.getPosition().equals(p.getPosition())).collect(Collectors.toSet());
            if (!animalsOnPos.isEmpty()) {
                List<Animal> luckyAnimals = getStrongestAnimals(animalsOnPos);
                luckyAnimals.forEach(a -> a.increaseEnergyLevel((GlobalVariables.plantEnergy / animalsOnPos.size())));
                p.remove();
                p.unregister(this.map);
                this.globalVariables.plantCounter--;
            }
        }
    }

    public List<Animal> getStrongestAnimals(Set<Animal> animals) {
        int max = animals.stream().map(Animal::getEnergyLevel).
                max(Integer::compareTo).orElse(0);
        return animals.stream().filter(a -> a.getEnergyLevel() == max).collect(Collectors.toList());
    }

    public Animal getStrongestAnimal(List<Animal> animals) {
        return animals.stream().max(Comparator.comparing(Animal::getEnergyLevel)).stream()
                .findFirst().orElseThrow(() -> new IllegalStateException("There is no such animal"));
    }

    public void moveAnimals() {
        getAnimals().forEach(Animal::move);
    }

    public void removeDeadAnimals() {
        getAnimals().forEach(a -> {
            a.die();
            if (a.getEnergyLevel() == 0) {
                a.setDeathDay(globalVariables.dayCounter);
                globalVariables.averageLifeLength +=
                ((double)(a.getDeathDay() - a.getSpawnDay()) - globalVariables.averageLifeLength)/
                (double)(globalVariables.animalCounter - globalVariables.livingAnimalCounter + 1);
                this.globalVariables.livingAnimalCounter--;
                a.unregister(this.map);
            }
        });
    }

    public long getInterval() { return this.interval; }

    public boolean getSimulationStatus() {return this.simulationStatus;}

    public WorldMap getMap() { return this.map; }

    public void setSimulationStatus(boolean status){this.simulationStatus = status;}

    public GlobalVariables getGlobalVariables() { return globalVariables; }

    public int animalsChildren(){ return getAnimals().stream().mapToInt(o -> o.getChildren().size()).sum();}

    public int getMaxGene(){
        int max = 0;
        for(int i = 0; i < 8; i++) {
            if (this.globalVariables.genoType[i] > this.globalVariables.genoType[max])
                max = i;
        }
        return max;
    }

}