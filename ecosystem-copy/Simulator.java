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
    // The number of turns before a fire starts.
    private static final int FIRE_STARTS = 10;
    

    // List of living organisms in the field.
    private ArrayList<Thing> things;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    // List of fires in the field.
    
    
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
        
       things = new ArrayList<>();
       field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Deer.class, Color.BLACK);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(Tree.class, Color.ORANGE);
        view.setColor(Fire.class, Color.RED);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (40 steps).
     */
    public void runLongSimulation()
    {
        simulate(40);
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
        List<Thing> fire = new ArrayList<>();
        if(step > 0 && step % FIRE_STARTS == 0) {
            startFire(fire);
        }
        // Provide space for new organisms.
        List<Thing> newThings = new ArrayList<>();
        // Let all entities act.
        for(Iterator<Thing> it = things.iterator(); it.hasNext(); ) {
            Thing thing = it.next();
            thing.act(newThings);
            if(! thing.isViable()) {
                it.remove();
            }
            
        }
                                
        // Add the newly born deer and new plants to the main lists.
        things.addAll(newThings);
        things.addAll(fire);
        
        view.showStatus(step, field);
    }
    
        /**
     * If no fire is already burning, start a fire in the field.
     */
    private void startFire(List<Thing> fire)
    {
        Random rand = Randomizer.getRandom();
        int row = rand.nextInt(field.getWidth());
        int col = rand.nextInt(field.getDepth());
        Location location = new Location(row, col);
        Fire newFire = new Fire(field, location);
        fire.add(newFire);
    }
    
    
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        things.clear();
        populate();
                
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    public int getStep()
    {
        return step;
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
                    things.add(deer);
                }
                else if(rand.nextDouble() <= TREE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Tree tree = new Tree(false, field, location);
                    things.add(tree);
                    location.addPlant(tree);
                }
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Grass grass = new Grass(false, field, location);
                    things.add(grass);
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
