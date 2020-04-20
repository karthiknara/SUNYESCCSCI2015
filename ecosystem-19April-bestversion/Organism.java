import java.util.List;

/**
 * A class representing shared characteristics of organisms.
 * 
 * @author Rebecca McCranie
 *         (adapted from Foxes and Rabbits 
 *         by David J. Barnes and Michael Kölling, 2016.02.29 (2))
 * @version 2020.04.01
 */
public abstract class Organism extends Thing
{
    // Whether the organism is alive or not.
    //private boolean viable;
    // The organism's field.
    //private Field field;
    // The organism's position in the field.
    //private Location location;
    
    /**
     * Create a new thing at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Organism(Field field, Location location)
    {
        super(field, location);
    }
    
    /**
     * Make this organism act - that is: make it do
     * whatever it wants/needs to do.
     * @param newOrganisms A list to receive newly born organisms.
     */
    abstract void act(List<Thing> newThings);

    
    /**
     * Indicate that the organism is no longer alive.
     * It is removed from the field.
     */
    abstract void setDead();
}
