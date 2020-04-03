import java.util.ArrayList;

/**
 * Represent a location in a rectangular grid.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class Location
{
    // Row and column positions.
    private int row;
    private int col;
    private ArrayList<Plant> plantsInSquare;
    

    /**
     * Represent a row and column.
     * @param row The row.
     * @param col The column.
     */
    public Location(int row, int col)
    {
        this.row = row;
        this.col = col;
        plantsInSquare = new ArrayList<>();
    }
    
    /**
     * Implement content equality.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        }
        else {
            return false;
        }
    }
    
    /**
     * Return a string of the form row,column
     * @return A string representation of the location.
     */
    public String toString()
    {
        return row + "," + col;
    }
    
    /**
     * Use the top 16 bits for the row value and the bottom for
     * the column. Except for very big grids, this should give a
     * unique hash code for each (row, col) pair.
     * @return A hashcode for the location.
     */
    public int hashCode()
    {
        return (row << 16) + col;
    }
    
    /**
     * @return The row.
     */
    public int getRow()
    {
        return row;
    }
    
    /**
     * @return The column.
     */
    public int getCol()
    {
        return col;
    }
    
    /**
     * @return The ArrayList of all the plants in the location.
     */
    public ArrayList<Plant> getPlants()
    {
        return plantsInSquare;
    }
    
    /**
     * Remove plant from ArrayList at location.
     * 
     * @return Updated ArrayList
     */
    public ArrayList<Plant> removePlant()
    {
        for(Plant plant : plantsInSquare) {
            if(!plant.isAlive()) {
                plantsInSquare.remove(plant);
            }
        }
        return plantsInSquare;
    }
    
    /**
     * Add plant to ArrayList at location.
     * 
     * @return Updated ArrayList
     */
    public void addPlant(Plant plant)
    {
        plantsInSquare.add(plant);
    }
}
