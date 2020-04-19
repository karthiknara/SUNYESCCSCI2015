import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

/**
 * A simple model of fire.
 * Fire starts, moves, kills grass and trees, and burns out.
 * 
 * @author Rebecca McCranie
 *         (adapted from Foxes and Rabbits 
 *         by David J. Barnes and Michael Kölling, 2016.02.29 (2))
 * @version 2020.04.01
 */
public class Fire extends Thing
{
    
    private static final Random rand = Randomizer.getRandom();
    
    // The probability that a fire will start
    private static final double FIRE_PROBABILITY = 1;
    
    
    
    /**
     * Create fire. 
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fire(Field field, Location location)
    {
        super(field, location);
    }
        
    /**
     * This is what fire does most of the time: it looks for
     * grass or trees to burn.
     * 
     * @param field The field currently occupied.
     * @param newDeers A list to return newly born deer.
     */
    public void act(List<Thing> newFire)
    {
        Location newLocation = findFuel();
        if(newLocation == null) {
            // No food found - try to move to a free location.
            newLocation = getField().randomAdjacentLocation(getLocation());
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
    
        
    /**
     * If no fire is already burning, start a fire in the field.
     */
    private void startFire()
    {
        Random rand = Randomizer.getRandom();
        Field field = getField();
        int row = rand.nextInt(field.getWidth());
        int col = rand.nextInt(field.getDepth());
        Location location = new Location(row, col);
        Fire newFire = new Fire(field, location);        
    }
    
  
    /**
     * Look for plants adjacent to the current location.
     * Plants have a chance of dying by fire based on type.
     * @return Where fuel was found, or null if it wasn't.
     */
    private Location findFuel()
    {
        Random rand = Randomizer.getRandom();
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            ArrayList<Plant> plants = where.getPlants();
            if(plants.size() > 0) {
                for(Plant plant : plants) {
                    double death = 0;
                    if(plant instanceof Grass) {
                        death = 0.75;
                    }
                    else if(plant instanceof Tree) {
                        death = 0.60;
                    }
                
                    if(rand.nextDouble() <= death) {
                        plant.setDead();
                    }
                    return where;
                }         
            }
        }
        return null;
    }
    
    
       
    /**
     * If fire runs out of fuel, it dies.
     */
    protected void setDead()
    {
        boolean viable = isViable();
        viable = false;
    }
    
    
}
