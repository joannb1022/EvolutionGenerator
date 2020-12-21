package map;

import mapElements.MapElement;
import mapElements.Vector2d;

public interface MapObserver {
    void positionChanged(Vector2d oldPosition, MapElement e);
    void remove(MapElement e, Vector2d position);
}
