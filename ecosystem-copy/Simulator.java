import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing deer, grass, and trees.
 * 
 * @author Rebecca McCranie
 *         (adapted from Foxes and Rabbits 
 *         by David J. Barnes and Michael Kölling, 2016.02.29 (2))
 * @version 2020.04.01
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 12;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 12;
    // The probability that a deer will be created in any given grid position.
    private static final double DEER_CREATION_PROBABILITY = 0.05;
    // The probability that a tree will be created in any given grid position.
    private static final double TREE_CREATION_PROBABILITY = 0.10; 
    // The probability that grass will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.15;

    // List of living organisms in the field.
    private ArrayList<Organism> organisms;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        organisms = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setAnimalColor(Deer.class, Color.BLACK);
        view.setPlantColor(Grass.class, Color.GREEN);
        view.setPlantColor(Tree.class, Color.ORANGE);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            // delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;

        // Provide space for new organisms.
        List<Organism> newOrganisms = new ArrayList<>();
        // Let all animals act.
        for(Iterator<Organism> it = organisms.iterator(); it.hasNext(); ) {
            Organism organism = it.next();
            organism.act(newOrganisms);
            if(! organism.isAlive()) {
                it.remove();
            }
        }
        
                        
        // Add the newly born deer and new plants to the main lists.
        organisms.addAll(newOrganisms);
        
        view.showStatus(step, field);
    }
    
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        organisms.clear();
        populate();
                
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with organisms.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Deer deer = new Deer(false, field, location);
                    organisms.add(deer);
                }
                else if(rand.nextDouble() <= TREE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Tree tree = new Tree(false, field, location);
                    organisms.add(tree);
                    location.addPlant(tree);
                }
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Grass grass = new Grass(false, field, location);
                    organisms.add(grass);
                    location.addPlant(grass);
                }
                    // else leave the location empty.
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
