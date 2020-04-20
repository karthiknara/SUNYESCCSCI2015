import java.util.List;

/**
 * A class representing shared characteristics of things in the field.
 * 
 * @author Rebecca McCranie
 *         (adapted from Foxes and Rabbits 
 *         by David J. Barnes and Michael Kölling, 2016.02.29 (2))
 * @version 2020.04.01
 */
public abstract class Thing
{
    // Whether the thing is viable or not.
    private boolean viable;
    // The thing's field.
    private Field field;
    // The thing's position in the field.
    private Location location;
    
    /**
     * Create a new thing at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Thing(Field field, Location location)
    {
        viable = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this thing act - that is: make it do
     * whatever it wants/needs to do.
     * @param newThings A list to receive newly born things.
     */
    abstract void act(List<Thing> newThings);

    /**
     * Check whether the thing is viable or not.
     * @return true if the thing is still viable.
     */
    protected boolean isViable()
    {
        return viable;
    }

    /**
     * Indicate that the thing is no longer viable.
     * It is removed from the field.
     */
    abstract void setDead();
    
    /**
     * Return the thing's location.
     * @return The thing's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the organism at the new location in the given field.
     * @param newLocation The organism's new location.
     */
    protected void setLocation(Location newLocation) {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
         
    /**
     * Return the thing's field.
     * @return The thing's field.
     */
    protected Field getField()
    {
        return field;
    }
}
