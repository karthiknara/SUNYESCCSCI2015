import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing deer, grass, and trees.
 * 
 * 
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
    private static final double TREE_CREATION_PROBABILITY = 0.25; 
    // The probability that grass will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.100;
    // The number of turns before a fire starts.
    private static final int FIRE_STARTS = 10;
    // The default number of deer to start the simulation.
    private static final int DEFAULT_DEER = 10;
    // The default number of grass plants to start the simulation.
    private static final int DEFAULT_GRASS = 144;
    // The default number of trees to start the simulation.
    private static final int DEFAULT_TREE = 25;

    // List of living organisms in the field.
    private ArrayList<Thing> things;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    // The number of deer at beginning of simulation.
    private int deerCount;
    // The number of trees at beginning of simulation.
    private int treeCount;
    // The number of grass plants at beginning of simulation.
    private int grassCount;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator(int deer, int grass, int trees)
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH, deer, grass, trees);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    private Simulator(int depth, int width, int deer, int grass, int trees)
    {
  
        // Check grid dimensions to be reasonable.
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        if (grass > 0 && deer > 0 && trees > 0 && grass >= deer && deer >= trees) {
        deerCount = deer;
        grassCount = grass;
        treeCount = trees;
        }
        
        // Check population numbers to be reasonable.
       else if(grass == 0 || deer == 0 || trees == 0) {
            System.out.println("Please insert a value for each entity.");
            System.out.println("Using default values.");
            deerCount = DEFAULT_DEER;
            grassCount = DEFAULT_GRASS;
            treeCount = DEFAULT_TREE;
        }
        else if(grass < deer || deer < trees) {
            System.out.println("The number of deer must be less than the number of grass plants and greater than the number of trees.");
            System.out.println("Using default values.");
            deerCount = DEFAULT_DEER;
            grassCount = DEFAULT_GRASS;
            treeCount = DEFAULT_TREE;
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
     * (25 steps).
     */
    public void runLongSimulation()
    {
        simulate(25);
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
            delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * 
     */
    public void simulateOneStep()
    {
        step++;
        List<Thing> newFires = new ArrayList<>();
        if(step > 0 && step % FIRE_STARTS == 0) {
            startFire(newFires);
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
        things.addAll(newFires);
        
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
        int deerCreated = 0;
        int grassCreated = 0;
        int treesCreated = 0;
        while(deerCreated < deerCount) {
             int row = rand.nextInt(field.getDepth());
             int col = rand.nextInt(field.getWidth());
             Location location = new Location(row, col);
             Deer deer = new Deer(false, field, location);
             things.add(deer);
             deerCreated++;
        }
        while(treesCreated < treeCount) {
            int row = rand.nextInt(field.getDepth());
            int col = rand.nextInt(field.getWidth()); 
            Location location = new Location(row, col);
            Tree tree = new Tree(false, field, location);
            ArrayList<Plant> plants = location.getPlants();
            if(plants.size() < 10) {
                things.add(tree);
                location.addPlant(tree);
                treesCreated++;
            } 
            else {
                tree.setDead();
            }
        }
        while(grassCreated < grassCount) {
            int row = rand.nextInt(field.getDepth());
            int col = rand.nextInt(field.getWidth()); 
            Location location = new Location(row, col);
            Grass grass = new Grass(false, field, location);
            ArrayList<Plant> plants = location.getPlants();
            if(plants.size() < 10) {
                things.add(grass);
                location.addPlant(grass); 
                grassCreated++;
            } 
            else {
                grass.setDead();
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
