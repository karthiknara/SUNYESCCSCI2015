import java.util.List;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author Rebecca McCranie
 *         (adapted from Foxes and Rabbits 
 *         by David J. Barnes and Michael Kölling, 2016.02.29 (2))
 * @version 2020.04.01
 */
public abstract class Animal extends Organism
{
    // Whether the animal is alive or not.
    //private boolean alive;
    // The animal's field.
    //private Field field;
    // The animal's position in the field.
    //private Location location;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        super(field, location);
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Thing> newAnimals);

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        boolean viable = isViable();
        viable = false;
        Field field = getField();
        Location location = getLocation();
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
}
