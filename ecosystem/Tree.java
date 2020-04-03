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
    private static final double FIRE_DEATH_PROBABILITY = 0.6;
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
    public Tree(boolean alive, Field field, Location location)
    {
        super(alive, field, location);
        age = 0;
    }
    
    /**
     * Trees spread to an adjacent square every 5 turns,
     * as long as it is alive. Squares can contain up to 10 plants.
     * Trees can replace grass plants.
     * (based on the findFood method for the Fox class)
     * @param field The field currently occupied.
     * @param newPlants A list to return newly born plants.
     */
    public void act(List<Plant> newTrees)
    {
        Location newLocation = getField().locationsWithSpaceForTrees(getLocation());
        
        if(isAlive() && canSpread() && newLocation != null) {
            spread(newTrees);   
            Tree newTree = new Tree(true, getField(),newLocation);
            setLocation(newTree, newLocation);
        }
        else {
            // Overcrowding.
            setDead();
        }
    }
    
    /**
     * A tree can reproduce if its age is a multiple of NEW_TREE.
     */
    private boolean canSpread()
    {
        return age % NEW_TREE == 0;
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
    public void spread(List<Plant> newTrees)
    {
        // New trees spread into adjacent locations.
        // Get a list of adjacent locations with space for trees.
        Field field = getField();
        Location loc = field.locationsWithSpaceForTrees(getLocation());
        Tree seedling = new Tree(true, field, loc);
        List<Plant> currantPlantList = field.getPlantsAt(loc);
        if(currantPlantList.size() < MAX_PLANTS) {
            field.placePlant(seedling, loc);
            newTrees.add(seedling);
        } 
        else {
            int index = 0;
            boolean searching = true;
            while(searching && index < MAX_PLANTS) {
                for(Plant plant : currantPlantList) {
                    if(plant instanceof Grass) {
                        field.replaceGrass(loc);
                        newTrees.add(seedling);
                        searching = false;
                    }
                }
            }
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int reproduce()
    {
        int seedlings = 0;
        if(canBreed()) {
            seedlings++;
        }
        return seedlings;
    }

    /**
     * A tree can reproduce if its age is a multiple of 5.
     */
    private boolean canBreed()
    {
        return age % NEW_TREE == 0;
    }
}
