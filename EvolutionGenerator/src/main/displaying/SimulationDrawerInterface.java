package displaying;

public interface SimulationDrawerInterface {
    void drawMap();
    void updateSimulation();
    void pauseSimulation() throws InterruptedException;
}
