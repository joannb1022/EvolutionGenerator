package mapElements;

import map.WorldMap;


public class Plant extends MapElement {

    public Plant(WorldMap map){
        this.observers.add(map);
    }

    public void setPosition(Vector2d pos){ this.position = pos; }

}
