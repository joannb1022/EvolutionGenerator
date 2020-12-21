package mapElements;
import runAndData.GlobalVariables;

import java.util.*;

    public class Vector2d {
        public final int x;
        public final int y;

        public Vector2d(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean precedes(Vector2d other) {
            return this.x <= other.x && this.y <= other.y;
        }

        public boolean follows(Vector2d other) {
            return this.x >= other.x && this.y >= other.y;
        }

        public Vector2d upperRight(Vector2d other) {
            int x, y;
            x = Math.max(this.x, other.x);
            y = Math.max(this.y, other.y);
            return new Vector2d(x, y);
        }

        public Vector2d lowerLeft(Vector2d other) {
            int x, y;
            x = Math.min(this.x, other.x);
            y = Math.min(this.y, other.y);
            return new Vector2d(x, y);
        }

        public Vector2d add(Vector2d other) {
            int a = (this.x + other.x)%GlobalVariables.width, b = (this.y + other.y)%GlobalVariables.height;
            return new Vector2d((a>=0?a:a+GlobalVariables.width)%GlobalVariables.width,(b>=0?b:b+GlobalVariables.height)%GlobalVariables.height);
        }

        public Vector2d subtract(Vector2d other) {
            return new Vector2d(this.x - other.x, this.y - other.y);
        }

        public int getX(){
            return this.x;
        }

        public int getY(){
            return this.y;
        }

        public Vector2d opposite() {
            return new Vector2d(-this.x, -this.y);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if (!(other instanceof Vector2d))
                return false;
            Vector2d that = (Vector2d) other;
            return that.x == this.x && that.y == this.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }

        @Override
        public String toString() {
            return "(" + this.x + "," + this.y + ")";
        }

    }
