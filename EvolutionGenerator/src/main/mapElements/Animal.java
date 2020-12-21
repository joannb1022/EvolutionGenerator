package mapElements;

import runAndData.GlobalVariables;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Animal extends MapElement{
    Genotype geno;
    int energyLevel, id, spawnDay, deathDay;
    List<Animal> children = new LinkedList<>();
    Animal[] parents;
    MoveDirection direction;


    public Animal(Vector2d pos, int id){
        this.geno = new Genotype();
        this.id = id;
        this.parents = new Animal[1];
        this.parents[0] = null;
        this.position = pos;
        this.geno.randomGeno();
        this.direction = MoveDirection.random();
        this.energyLevel = GlobalVariables.startEnergy;
    }

    public Animal(Animal dad, Animal mom, Vector2d childPos, int id){
        this.geno = new Genotype();
        this.geno.pickGeno(dad,mom);
        this.parents = new Animal[2];
        this.parents[0] = dad;
        this.parents[1] = mom;
        this.id = id;
        this.position = childPos;
        this.energyLevel = dad.getEnergyLevel()/4 + mom.getEnergyLevel()/4;
    }

    public int getEnergyLevel(){
        return this.energyLevel;
    }

    public void increaseEnergyLevel(int energy){ this.energyLevel += energy; }

    Genotype getGeno(){ return this.geno; }

    public int[] getGenes(){ return getGeno().getGenes();}

    public void move(){
        this.energyLevel -= GlobalVariables.moveEnergy;
        this.direction = turn();
        Vector2d prev = this.position;
        this.position = this.position.add(this.direction.toUnitVector());
        super.positionChanged(prev);

    }

    public List<Animal> getChildren() { return this.children; }

    public void getNumberOfDesc(AtomicInteger n) {
        n.addAndGet(this.getChildren().size());
        this.getChildren().forEach(a -> a.getNumberOfDesc(n));
    }

    MoveDirection turn(){
        Random rand = new Random();
        int x = rand.nextInt(32), i = 0;//[1,1,1,1,7,7,7,7]
        while(x > 0 && i < 7){
            x -= this.geno.genes[i++];
        }
        return MoveDirection.values()[i];
    }
    public void die(){
        if(this.energyLevel <= 0) {
            this.energyLevel = 0;
            super.remove();
        }
    }

    public int getId() { return this.id; }

    public void addChild(Animal child) {
        this.children.add(child);
    }

    public boolean canCopulate(){ return this.energyLevel> 0.5*GlobalVariables.startEnergy; }

    public void setDeathDay(int deathDay) { this.deathDay = deathDay; }

    public void setSpawnDay(int spawnDay){ this.spawnDay = spawnDay; }

    public int getSpawnDay(){ return this.spawnDay; }

    public int getDeathDay(){ return this.deathDay; }
}
