package mapElements;

import map.MapObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class MapElement {

    Vector2d position;
    List<MapObserver> observers = new ArrayList<>();

    public Vector2d getPosition(){
        return this.position;
    }

    public void register(MapObserver observer){
        this.observers.add(observer);
    }

    public void unregister(MapObserver observer){
        this.observers.remove(observer);
    }

    void positionChanged(Vector2d oldPosition){
        this.observers.forEach(observer -> observer.positionChanged(oldPosition,this));
    }

    public void remove(){
        this.observers.forEach(observer -> observer.remove(this, this.getPosition()));
    }
}
