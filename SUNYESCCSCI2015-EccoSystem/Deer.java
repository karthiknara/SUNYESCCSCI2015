import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

/**
 * A simple model of a deer.
 * Deer, move, eat grass and trees, reproduce, and die.
 * 
 * 
 */
public class Deer extends Animal
{
    // Characteristics shared by all deer (class variables).
    
    // Number of turns between reproduction cycles. 
    private static final int REPRODUCTION_CYCLE = 4;
    // The minimum health required for a deer to live.
    private static final int MIN_HEALTH = 0;
    // The maximum health a deer can have.
    private static final int MAX_HEALTH = 8;
    // The food value of a single grass. In effect, this is the
    // number of health points a deer gains from eating a square of grass.
    private static final int GRASS_FOOD_VALUE = 1;
    // A shared random number generator.
    private static final Random rand = Randomizer.getRandom();
    
    
    // The deer's food level, which is increased by eating grass.
    private int healthLevel;

    /**
     * Create a deer. Deer have 8 health points  
     * 
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Deer(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
           
            healthLevel = rand.nextInt(MAX_HEALTH);
        }
        else {
            healthLevel = 8;
            
        }
    }
        
    /**
     * This is what the deer does most of the time: it looks for
     * grass. In the process, it might breed or die of hunger.
     * 
     * @param field The field currently occupied.
     * @param newDeers A list to return newly born deer.
     */
    public void act(List<Thing> newDeers)
    {
        
        if(isViable()) {
            giveBirth(newDeers);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
           
            lowerHealth();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * Decrease health points. This could result in the deer's death.
     */
    private void lowerHealth()
    {
        healthLevel--;
        if(healthLevel <= MIN_HEALTH) {
            setDead();
        }
    }
    
    /**
     * Look for plants adjacent to the current location.
     * Only the first live grass is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location mostFood = getLocation();
        ArrayList<Plant> currantPlantList = mostFood.getPlants();
        while(it.hasNext()) {
            Location where = it.next();
            ArrayList<Plant> plants = where.getPlants();
            if(plants.size() > currantPlantList.size()) {
                mostFood = where;
            }
        }
        currantPlantList = mostFood.getPlants();
        if(currantPlantList.size() > 0) {
            Plant food = currantPlantList.get(0);
            food.setDead();
            currantPlantList.remove(0);
            if(healthLevel < MAX_HEALTH) {
                healthLevel = healthLevel + GRASS_FOOD_VALUE;
            }        
            return mostFood;
        }
        return null;
    }
    
    /**
     * Check whether or not this deer is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newDeers A list to return newly born deer.
     */
    private void giveBirth(List<Thing> newDeers)
    {
        // New deer are born into adjacent locations.
        // Get a list of adjacent free locations.
        if(canBreed()) {
            Field field = getField();
            Location free = field.freeAdjacentLocation(getLocation());
            Deer young = new Deer(false, field, free);
            newDeers.add(young);
        }
    }
    
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed()) {
            births = 1;
        }
        return births;
    }

    /**
     * A deer can breed every x turns, where x = REPRODUCTION_CYCLE.
     */
    private boolean canBreed()
    {
        boolean breed = false;
        if( REPRODUCTION_CYCLE == 0 && healthLevel == MAX_HEALTH){
            breed = true;
        } 
        return breed;
    }
}
