/*
 * Written by WCUPA Computer Science club members
 * November 2016
 */
package manifestgenerator.models;

import java.util.ArrayList;

/**
 * @author Mohamed Alie Pussah (mp754927@wcupa.edu)
 * Encapsulates data for a column on a page
 */
public class Column implements Comparable<Column>
{
    /**
     * The header cell of this column
     */
    public final Cell HEADER;
    /**
     * All cells in this column
     */
    public final ArrayList<Cell> CELLS;
    
    /**
     * Creates a new column with the given header
     * @param header The header cell of the column to create
     */
    public Column(Cell header) {
        HEADER = header;
        CELLS = new ArrayList<>();
    }

    
    /**
     * Compares this column to the given column
     * @param other The other column to which to compare this column
     * @return 0 if the columns are in the same column on the page, 
     * positive number if this column comes after the other column, 
     * and a negative number if this column comes before the other
     * column
     */
    @Override
    public int compareTo(Column other)
    {
        return this.HEADER.COLUMN - other.HEADER.COLUMN;
    }
}
