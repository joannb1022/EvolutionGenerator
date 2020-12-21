package map;

import mapElements.Vector2d;

public abstract class Jungle {
    Vector2d lL;
    Vector2d uR;

    public abstract boolean inJungle(Vector2d v);
    public abstract Vector2d pickPlaceInJungle();
}
