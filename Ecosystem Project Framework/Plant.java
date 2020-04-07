import java.util.List;
import java.util.Random;

/**
 * A simple model of a plant.
 * Plants age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Plant extends Entity
{
    // Characteristics shared by all plants (class variables).
    // How many turns must pass before grass spreads to an adjacent square.
    private static final int NEW_GRASS = 2;
    
    // Individual characteristics (instance fields).
    /**
     * Create a new plant. A plant may be created with age
     * zero or with a random age.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location)
    {
        super(field, location);
    }
    
    /**
     * This is what the plant does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newPlants A list to return newly born plants.
     */
    public void act(List<Entity> newPlants)
    {
        if(isAlive()) {
            giveBirth(newPlants);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Check whether or not this plant is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPlants A list to return newly born plants.
     */
    private void giveBirth(List<Entity> newPlants)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        if (canBreed()){
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Plant newPlant = new Plant(field, loc);
                newPlants.add(newPlant);
            }
        }
    }
  
    /**
     * Grass can reproduce if its age is a multiple of NEW_GRASS.
     */
    private boolean canBreed()
    {
        return getTurnCount() % NEW_GRASS == 0;
    }
}
