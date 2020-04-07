
/**
 * Write a description of class Grass here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Grass extends Plant
{
    // How many turns must pass before grass spreads to an adjacent square.
    private static final int NEW_GRASS = 2;  
    // The likelihood of a grass dying in a fire.
    private static final double FIRE_DEATH_PROBABILITY = 0.75;
    
    /**
     * Constructor for objects of class Grass
     */
    public Grass(Field field, Location location)
    {
        super(field, location);
    }
    
    /**
     * Grass can reproduce if its age is a multiple of NEW_GRASS.
     */
    private boolean canBreed()
    {
        return getTurnCount() % NEW_GRASS == 0;
    }
    
}
