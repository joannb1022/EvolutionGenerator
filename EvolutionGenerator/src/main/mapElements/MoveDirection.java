package mapElements;

import java.util.Random;

public enum MoveDirection {
    NORTH,NORTHEAST,EAST,SOUTHEAST,SOUTH, SOUTHWEST,WEST,NORTHWEST;

    public static MoveDirection random(){ return MoveDirection.values()[ new Random().nextInt(8)];}

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            case WEST -> new Vector2d(-1, 0);
            case EAST -> new Vector2d(1, 0);
            case NORTHEAST -> new Vector2d(1, 1);
            case NORTHWEST -> new Vector2d(-1, 1);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case NORTH ->  "\u21d1";
            case NORTHEAST ->  "\u21d7";
            case EAST -> "\u21d2";
            case SOUTHEAST ->  "\u21d8";
            case SOUTH ->  "\u21d3";
            case SOUTHWEST ->  "\u21d9";
            case WEST ->  "\u21d0";
            case NORTHWEST ->  "\u21d6";
        };
    }
}
