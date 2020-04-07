import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 12;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 12;
    // The default .
    private static final int DEFAULT_DEER = 15;
    // The default .
    private static final int DEFAULT_GRASS = 25;
    // The default .
    private static final int DEFAULT_TREE = 10;
    // The probability that a fox will be created in any given grid position. ????????????????
    private static final double DEER_CREATION_PROBABILITY = .02;
    // The probability that a rabbit will be created in any given grid position. ??????????????
    private static final double GRASS_CREATION_PROBABILITY = .08;    

    // List of animals in the field.
    private List<Entity> entities;
    // The number of deer to spawn.
    private int deerCount;
    // The number of grass to spawn.
    private int grassCount;
    // The number of tree to spawn.
    private int treeCount;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator(int deer, int grass, int tree)
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH, deer, grass, tree);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    private Simulator(int depth, int width, int deer, int grass, int tree)
    {
        deerCount = deer;
        grassCount = grass;
        treeCount = tree;
        
        // Check grid dimensions to be reasonable.
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        // Check population numbers to be reasonable.
        if(grass == 0 && deer == 0 && tree == 0) {
            System.out.println("Please insert a value for each entity.");
            System.out.println("Using default values.");
            deerCount = DEFAULT_DEER;
            grassCount = DEFAULT_GRASS;
            treeCount = DEFAULT_TREE;
        }
        else if(grass < deer && deer < tree) {
            System.out.println("The number of deers must be less than grass and greater than the number of trees.");
            System.out.println("Using default values.");
            deerCount = DEFAULT_DEER;
            grassCount = DEFAULT_GRASS;
            treeCount = DEFAULT_TREE;
        }
        
        entities = new ArrayList<>();
        field = new Field(depth, width);
        
        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Plant.class, Color.GREEN);
        view.setColor(Deer.class, Color.ORANGE);
        
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

        // Provide space for newborn animals.
        List<Entity> newEntities = new ArrayList<>();        
        // Let all rabbits act.
        for(Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();
            entity.act(newEntities);
            if(! entity.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        entities.addAll(newEntities);

        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        entities.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void ogpopulate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Deer deer = new Deer(true, field, location);
                    entities.add(deer);
                }
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Plant plant = new Plant(field, location);
                    entities.add(plant);
                }
                // else leave the location empty.
            }
        }
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void test1populate()
    {
        int deerSpawned = 0;
        int grassSpawned = 0;
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                // Allows only a set number of deer to spawn.
                while(deerSpawned < deerCount){
                    if(rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Deer deer = new Deer(true, field, location);
                        entities.add(deer);
                        deerSpawned++;
                    }
                    // else leave the location empty.
                }
                // Allows only a set number of grass to spawn.
                while(grassSpawned < grassCount){
                    if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Plant plant = new Plant(field, location);
                        entities.add(plant);
                        grassSpawned++;
                    }
                    // else leave the location empty.
                }
            }
        }
        
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void test2populate()
    {
        int deerSpawned = 0;
        int grassSpawned = 0;
        boolean deerBirthed = false;
        boolean grassBirthed = false;
        Random rand = Randomizer.getRandom();
        field.clear();
        while(deerSpawned < deerCount && grassSpawned < grassCount){
            for(int row = 0; row < field.getDepth(); row++) {
                for(int col = 0; col < field.getWidth(); col++) {
                // Allows only a set number of deer to spawn.
                
                
                
                    if(rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Deer deer = new Deer(true, field, location);
                        entities.add(deer);
                        deerSpawned++;
                    }
                    // else leave the location empty.
                
                // Allows only a set number of grass to spawn.
                    if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Plant plant = new Plant(field, location);
                        entities.add(plant);
                        grassSpawned++;
                    }
                    
                    // else leave the location empty.
                
                }
            }
             
        
        }
    }

    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        int deerSpawned = 0;
        int grassSpawned = 0;
        boolean deerBirthed = false;
        boolean grassBirthed = false;
        Random rand = Randomizer.getRandom();
        field.clear();
        while(deerSpawned < deerCount){
            for(int row = 0; row < field.getDepth(); row++) {
                for(int col = 0; col < field.getWidth(); col++) {
                // Allows only a set number of deer to spawn.
                
                
                
                    if(rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Deer deer = new Deer(true, field, location);
                        entities.add(deer);
                        deerSpawned++;
                    }
                    // else leave the location empty.
                
                }
            }
        }
             
        while(grassSpawned < grassCount){
            for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Plant plant = new Plant(field, location);
                        entities.add(plant);
                        grassSpawned++;
                    }
                }
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
