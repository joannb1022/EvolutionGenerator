package map;

import mapElements.*;
import runAndData.GlobalVariables;

import java.util.*;


public class WorldMap extends Jungle implements IMap, MapObserver {

    Map<Vector2d, Set<MapElement>> mapElements = new HashMap<>();
    final int width, height;


    public WorldMap() {
        this.width = GlobalVariables.width;
        this.height = GlobalVariables.height;
        this.lL = new Vector2d( (int)(this.width * (0.5 - GlobalVariables.jungleRatio* 0.5)),
                (int)(this.height * (0.5 - GlobalVariables.jungleRatio* 0.5)));
        this.uR = new Vector2d( (int)(this.width * (0.5 + GlobalVariables.jungleRatio * 0.5)),
                (int)(this.height * ( 0.5 + GlobalVariables.jungleRatio* 0.5)));
        GlobalVariables.u = this.uR;
        GlobalVariables.d = this.lL;
    }

    public void remove(MapElement e, Vector2d position) {
        if(this.mapElements.containsKey(position)) {
            this.mapElements.get(position).remove(e);
            if(this.mapElements.get(position).isEmpty())
                this.mapElements.remove(position);
        }
    }


    public void place(MapElement e) {
        if(!this.mapElements.containsKey(e.getPosition())) {
            this.mapElements.put(e.getPosition(), new HashSet<>());
        }
        this.mapElements.get(e.getPosition()).add(e);
    }

    public void positionChanged(Vector2d oldPosition, MapElement e) {
        remove(e,oldPosition);
        place(e);
    }

    public boolean occupied(Vector2d v) {
        return this.mapElements.containsKey(v);
    }

    public Vector2d findPosForChild(Vector2d v){
        Random rand = new Random();
        int x = rand.nextInt(8);
        if(occupied(v.add(MoveDirection.values()[x].toUnitVector()))){
            for(int i = 0; i < 8; i++){
                if(!occupied(v.add(MoveDirection.values()[i].toUnitVector()))) {
                    return v.add(MoveDirection.values()[i].toUnitVector());
                }
            }
        }
        return v.add(MoveDirection.values()[x].toUnitVector());
    }


    public Vector2d findStartingPos(){
        Vector2d pos = new Vector2d(new Random().nextInt(GlobalVariables.width),new Random().nextInt(GlobalVariables.height));
        if(occupied(pos)){
            for(int i = 0; i < GlobalVariables.width; i++)
                for (int j = 0; j < GlobalVariables.height; j++)
                    if(!occupied(new Vector2d(i,j)))
                        return new Vector2d(i,j);
        }
        return pos;
    }

    public Map<Vector2d,Set<MapElement>> getElements(){
        return this.mapElements;
    }

    public int getHeight() { return this.height; }

    public int getWidth() { return this.width; }

    public boolean inJungle(Vector2d v){
        return lL.precedes(v) && uR.follows(v);
    }

    public Vector2d pickPlaceInJungle(){
        Random rand = new Random();
        for(int i = 0; i < 2; i++) {
            Vector2d pos = new Vector2d(rand.nextInt(
                    GlobalVariables.u.getX() - GlobalVariables.d.getX() + 1
            ) + GlobalVariables.d.getX(), rand.nextInt(
                    GlobalVariables.u.getY() - GlobalVariables.d.getY() + 1
            ) + GlobalVariables.d.getY());
            if (!occupied(pos)) {
                return pos;
            }
        }
        for(int i = GlobalVariables.d.getX(); i <= GlobalVariables.u.getX(); i++) {
            for (int j = GlobalVariables.d.getY(); j <= GlobalVariables.u.getY(); j++) {
                if (!occupied(new Vector2d(i, j))) {
                    return new Vector2d(i, j);
                }
            }
        }
        return null;
    }

    public Vector2d pickPlaceOutsideJungle(){
        Random rand = new Random();
        for(int i = 0; i < 5; i++) {
            int opt = rand.nextInt(4), x = 0, y = 0;
            switch (opt) {
                case 0 -> {
                    x = rand.nextInt(GlobalVariables.d.getX());
                    y = rand.nextInt(GlobalVariables.height);
                }
                case 1 -> {
                    x = rand.nextInt(
                            GlobalVariables.u.getX() - GlobalVariables.d.getX() + 1)
                            + GlobalVariables.d.getX();
                    y = rand.nextInt(
                            GlobalVariables.height - GlobalVariables.u.getY() - 1)
                            + GlobalVariables.u.getY() + 1;
                }
                case 2 -> {
                    x = rand.nextInt(
                            GlobalVariables.u.getX() - GlobalVariables.d.getX() + 1)
                            + GlobalVariables.d.getX();
                    y = rand.nextInt(GlobalVariables.d.getY());
                }
                case 3 -> {
                    x = rand.nextInt(GlobalVariables.width - GlobalVariables.u.getX() - 1)
                            + GlobalVariables.u.getX() + 1;
                    y = rand.nextInt(GlobalVariables.height);
                }
            }
            if (!occupied(new Vector2d(x, y)))
                return new Vector2d(x, y);
        }
        for(int i = 0; i < GlobalVariables.width; i++){
            for(int j = 0; j < GlobalVariables.height; j++){
                if(i >= GlobalVariables.d.getX() && i < GlobalVariables.u.getX() - GlobalVariables.d.getX() + 1
                    && j >= GlobalVariables.d.getY() && j < GlobalVariables.u.getY() - GlobalVariables.d.getY() + 1
                )
                    continue;
                if(!occupied(new Vector2d(i,j)))
                    return new Vector2d(i,j);
            }
        }
        return null;
    }

}