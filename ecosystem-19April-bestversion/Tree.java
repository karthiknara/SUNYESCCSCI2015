import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

/**
 * A simple model of a tree.
 * Trees age, reproduce, and have a 60% chance of dying if they
 * encounter fire.
 * 
 * @author Rebecca McCranie
 *         (adapted from Foxes and Rabbits 
 *         by David J. Barnes and Michael Kölling, 2016.02.29 (2))
 * @version 2020.04.01
 */
public class Tree extends Plant
{
    // Characteristics shared by all trees (class variables).
    
    // The number of turns before a tree can reproduce.
    private static final int NEW_TREE = 5;
    // The likelihood of a tree dying if a fire comes.
    private static final double TREE_FIRE_DEATH_PROBABILITY = 0.6;
    // A shared random number generator.
    private static final Random rand = Randomizer.getRandom();
    //Maximum number of plants on a square. Should come from Plant??
    private static final int MAX_PLANTS = 10;
    // Individual characteristics (instance fields).
    // The tree's age.
    private int age;
    
    /**
     * Create a tree. 
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Tree(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt();
        }
        else {
            age = 0;
        }
    }
    
      
    /**
     * Trees spread to an adjacent square every 5 turns,
     * as long as it is alive. Squares can contain up to 10 plants.
     * Trees can replace grass plants.
     * (based on the findFood method for the Fox class)
     * @param field The field currently occupied.
     * @param newPlants A list to return newly born plants.
     */
    public void act(List<Thing> newTrees)
    {
        incrementAge();
        if(isViable()) {
            spread(newTrees);
        }
    }
        
    /**
     * A tree can reproduce if its age is a multiple of NEW_TREE.
     */
    private boolean canSpread()
    {
        return (age > 0 && age % NEW_TREE == 0);
    }
        
    /**
     * Increase the age. This is only used to determine when a tree
     * can reproduce. Trees do not die of old age.
     */
    private void incrementAge()
    {
        age++;        
    }
          
    /**
     * New trees will grow in adjacent locations with enough space.
     * @param newTrees A list to return new trees.
     */
    public void spread(List<Thing> newPlants)
    {
        // New trees spread into adjacent locations.
        // Get a list of adjacent locations with space for trees.
        if(canSpread()) {
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            boolean spread = false;
            while(!spread){
                for(Location location : adjacent) {
                    ArrayList<Plant> currantPlants = location.getPlants();
                    if(currantPlants.size() < 10) {
                        Tree young = new Tree(false, field, location);
                        newPlants.add(young);
                        currantPlants.add(young);
                        spread = true;
                    }
                    if(currantPlants.size() == 10) {
                        for(Plant plant : currantPlants) {
                            if(plant instanceof Grass) {
                                plant.setDead();
                                Tree young = new Tree(false, field, location);
                                currantPlants.remove(plant);
                                currantPlants.add(young);
                                newPlants.add(young);
                                spread = true;
                            }
                        }
                    }
                }       
            }
        }
    }
}
