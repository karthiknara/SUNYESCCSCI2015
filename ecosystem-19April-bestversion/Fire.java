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
    // The probability that a fire will kill a tree.
    private static final double TREE_DEATH_PROBABILITY= 0.60;
    // The probability that a fire will kill grass.
    private static final double GRASS_DEATH_PROBABILITY = 0.75;
    
    
    
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
        if(isViable()) {
            spread(newFire);
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
        Location currantLocation = getLocation();
        ArrayList<Plant> plantsInSquare = currantLocation.getPlants();
        while(it.hasNext()) {
            Location where = it.next();
            ArrayList<Plant> plants = where.getPlants();
            if(plants.size() > 0) {
                for(Plant plant : plants) {
                    double death = 0;
                    if(plant instanceof Grass) {
                        death = GRASS_DEATH_PROBABILITY;
                    }
                    else if(plant instanceof Tree) {
                        death = TREE_DEATH_PROBABILITY;
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
     * Fire will spread to any square with plants on it. 
     * Each plant has a probability of dying by fire.
     */
    private void spread(List<Thing> newFire)
    {
      
      Field field = getField();
      List<Location> adjacent = field.adjacentLocations(getLocation());
      for(Location location : adjacent) {
          ArrayList<Plant> adjacentPlantList = location.getPlants();
          if(adjacentPlantList.size() > 0) {
              Fire fire = new Fire(field, location);
              newFire.add(fire);
            }
      }
      
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
