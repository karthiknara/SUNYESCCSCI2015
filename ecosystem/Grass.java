import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

/**
 * A simple model of grass.
 * Grass spreads, never ages, is eaten by deer, and has a 75% chance
 * of dying if a fire comes.
 * 
 * @author Rebecca McCranie
 *         (adapted from Foxes and Rabbits 
 *         by David J. Barnes and Michael Kölling, 2016.02.29 (2))
 * @version 2020.04.01
 */
public class Grass extends Plant
{
    // Characteristics shared by all grasses (class variables).
    
    // How many turns must pass before grass spreads to an adjacent square.
    private static final int NEW_GRASS = 2;
    // The likelihood of a grass dying in a fire.
    private static final double FIRE_DEATH_PROBABILITY = 0.75;
    // A shared random number generator to control direction of spread.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    private int age;
    
    /**
     * Create grass in one square. 
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Grass(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt();
        }
    }
    
    /**
     * Return the grass plant's age. Plants don't die of old age, but age 
     * determines when plants can reproduce.
     * @return The age of the grass.
     */
    private int incrementAge()
    {
        age++;
        return age;
    }
    
    /**
     * Grass spreads to an adjacent square every 2 turns, as 
     * long as it is alive. Squares can contain up to 10 plants.
     * Trees can replace grass plants.
     * (based on the findFood method for the Fox class)
     * @param field The field currently occupied.
     * @param newPlants A list to return newly born plants.
     */
    public void act(List<Plant> newPlants)
    {
        incrementAge();
        Location newLocation = getField().locationsWithSpaceForGrass(getLocation());
        if(isAlive() && canSpread() && newLocation != null) {
            spread(newPlants); 
            Grass newGrass = new Grass(false, getField(),newLocation);
        }
        
    }
    
    /**
     * Add a grass plant into the List for plants at the location.
     * 
     * @param location The location within the field.
     */
    public ArrayList<Plant> setLocation(Location location)
    {
        ArrayList<Plant> plantsCurrantLocation = location.getPlants();
        Plant plant = new Grass(false, getField(), location);
        if(plantsCurrantLocation.size() < 10) {
            plantsCurrantLocation.add(plant);
        } else {
            setDead();
        }
        return plantsCurrantLocation;
    }
    
    /**
     * Grass can reproduce if its age is a multiple of NEW_GRASS.
     */
    private boolean canSpread()
    {
        return age % NEW_GRASS == 0;
    }
    
    /**
     * New grass will be made into adjacent locations with enough space.
     * @param newGrasses A list to return new grass.
     */
    public void spread(List<Plant> newPlants)
    {
        // New grasses spread into adjacent locations.
        // Get a list of adjacent locations with space for grass.
        Field field = getField();
        Location loc = field.locationsWithSpaceForGrass(getLocation());
        Grass young = new Grass(true, field, loc);
        newPlants.add(young);
    }
    
   
}
