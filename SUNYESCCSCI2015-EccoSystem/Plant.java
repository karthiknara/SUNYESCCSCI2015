import java.util.List;
import java.util.ArrayList;

/**
 * A class representing shared characteristics of plants.
 * 
 * 
 */
public abstract class Plant extends Organism
{
    // Whether the plant is alive or not.
    //private boolean viable;
    // The plant's field.
    //private Field field;
    // The plant's position in the field.
    //private Location location;
    //The max number of plants that can be on any particular square
    private static final int MAX_PLANTS = 10;
    
    
   
    /**
     * Create a new plant at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location)
    {
        super(field, location);
    }
    
           
    /**
     * Make this plant act - that is: make it do
     * whatever it wants/needs to do.
     * @param newPlants A list to receive newly born plants.
     */
    abstract public void act(List<Thing> newPlants);

    /**
     * Indicate that the plant is no longer alive.
     * It is removed from the field and from the list of plants on that 
     * square.
     */
    protected void setDead()
    {
        boolean viable = isViable();
        viable = false;
        Location loc = getLocation();
        ArrayList<Plant> plants = loc.getPlants();
        for(Plant plant : plants) {
            if(!isViable()) {
                plants.remove(plant);
            }
        }
    }
        
    /**
     * The plant will spread after a certain number of turns.
     * It will spread to the first adjacent square with available space.
     */
    abstract public void spread(List<Thing> newPlants);  
    
}
