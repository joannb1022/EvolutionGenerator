package map;

import mapElements.MapElement;
import mapElements.Vector2d;

public interface IMap extends MapObserver{
    void place(MapElement e);
    boolean occupied(Vector2d v);
    Vector2d pickPlaceOutsideJungle();
    Vector2d findStartingPos();
}
