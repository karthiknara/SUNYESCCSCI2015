
/**
 * Write a description of class Grass here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Grass extends Plant
{
      

    /**
     * Constructor for objects of class Grass
     */
    public Grass(Field field, Location location)
    {
        super(field, location);
        setBreedingProbability(0.12);
    }

    
}
