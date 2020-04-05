import java.util.Random;
import java.util.List;

/**
 * A class representing shared characteristics of living beings,
 * called Entities.
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Entity
{
    // The maximum number of offpring an entity can reproduce.
    private static final int MAX_OFFSPRING = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The likelihood of an entity breeding.
    private static double BREEDING_PROBABILITY = 0;
    
    // Whether the entity is alive or not.
    private boolean alive;
    // The entity's field.
    private Field field;
    // The entity's position in the field.
    private Location location;
    
    /**
     * Create a new entity at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Entity(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this entity act - that is: make it do
     * whatever it wants/needs to do.
     * @param newEntities A list to receive newly generated Entities.
     */
    abstract public void act(List<Entity> newEntities);

    /**
     * Check whether the entity is alive or not.
     * @return true if the entity is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the entity is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Place the entity at the new location in the given field.
     * @param newLocation The entity's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxOffspring()) + 1;
        }
        return births;
    }
    
    /**
     * Return a random value for entity breeding.
     * @return A random value for breeding.
     */
    protected Random getRandom()
    {
        return rand;
    }
    
    /**
     * Return the entity's location.
     * @return The entity's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Return the entity's field.
     * @return The entity's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Return the entity's breeding probability.
     * @return The entity's breeding probability.
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Return the entity's max number of offspring.
     * @return The entity's max number of offspring.
     */
    protected int getMaxOffspring()
    {
        return MAX_OFFSPRING;
    }
    
    /**
     * Return the entity's max number of offspring.
     * @return The entity's max number of offspring.
     */
    protected double setBreedingProbability(double prob)
    {
        return BREEDING_PROBABILITY = prob;
    }
}
