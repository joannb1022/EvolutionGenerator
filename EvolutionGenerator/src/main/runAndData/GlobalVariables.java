package runAndData;

import mapElements.Vector2d;

public class GlobalVariables {

    public static int width, height, startEnergy,
            moveEnergy, plantEnergy, startAnimals, sceneHeight = 600, sceneWidth = 800;
    public static double jungleRatio;
    public static Vector2d d,u;
    public int animalCounter = 0, livingAnimalCounter = 0, plantCounter = 0,dayCounter = 1;
    public double averageLifeLength = 0;
    public int[] genoType = new int[8];
}
