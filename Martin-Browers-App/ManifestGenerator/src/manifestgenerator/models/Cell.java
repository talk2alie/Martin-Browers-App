/*
 * Written by WCUPA Computer Science club members
 * November 2016
 */
package manifestgenerator.models;

/**
 * @author Mohamed Alie Pussah (mp754927@wcupa.edu)
 * Encapsulates data for a cell on a page
 */
public class Cell implements Comparable<Cell>
{
    /**
     * The row number of this cell on the page
     */
    public final int ROW;
    /**
     * The column number of this cell on the page
     */
    public final int COLUMN;
    /**
     * The value of this cell on the page
     */
    public final String VALUE;
    
    /**
     * Creates a new cell with the given information
     * @param rowNumber The row number of the cell
     * @param columnNumber The column number of the cell
     * @param value The value of the cell
     */
    public Cell(int rowNumber, int columnNumber, String value) {
        ROW = rowNumber;
        COLUMN = columnNumber;
        VALUE = value;
    }
    
    /**
     * Compares this cell to the given cell
     * @param other The other cell to which to compare this cell
     * @return 0 if the cells are in the same column on the page, 
     * positive number if this cell comes after the other cell, 
     * and a negative number if this cell comes before the other
     * cell
     */
    @Override
    public int compareTo(Cell other)
    {
        return this.COLUMN - other.COLUMN;
    }
}
