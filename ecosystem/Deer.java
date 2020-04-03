import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a deer.
 * Deer age, move, eat grass, reproduce, and die.
 * 
 * @author Rebecca McCranie
 *         (adapted from Foxes and Rabbits 
 *         by David J. Barnes and Michael Kölling, 2016.02.29 (2))
 * @version 2020.04.01
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
    
    // Individual characteristics (instance fields).
    // The deer's age.
    private int age;
    // The deer's food level, which is increased by eating grass.
    private int healthLevel;

    /**
     * Create a deer. Deer have 8 health points and are 0 years old at 
     * creation.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Deer(boolean alive, Field field, Location location)
    {
        super(alive, field, location);
        healthLevel = 8;
        age = 0;
    }
        
    /**
     * This is what the deer does most of the time: it looks for
     * grass. In the process, it might breed or die of hunger.
     * 
     * @param field The field currently occupied.
     * @param newDeers A list to return newly born deer.
     */
    public void act(List<Animal> newDeers)
    {
        incrementAge();
        lowerHealth();
        if(isAlive()) {
            giveBirth(newDeers);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
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
     * Increase the age. This could result in the deer's death.
     */
    private void incrementAge()
    {
        age++;
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
     * Look for grass adjacent to the current location.
     * Only the first live grass is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object plant = field.getObjectAt(where);
            if(plant instanceof Grass) {
                Grass grass = (Grass) plant;
                if(grass.isAlive() && healthLevel <= MAX_HEALTH) { 
                    healthLevel = healthLevel + GRASS_FOOD_VALUE;
                }
                grass.setDead();
                return where;
                }
            }
            return null;
    }
    
    /**
     * Check whether or not this deer is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newDeers A list to return newly born deer.
     */
    private void giveBirth(List<Animal> newDeers)
    {
        // New deer are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Deer young = new Deer(true, field, loc);
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
     * A deer can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        boolean breed = false;
        if(age % REPRODUCTION_CYCLE == 0){
            breed = true;
        } 
        return breed;
    }
}
