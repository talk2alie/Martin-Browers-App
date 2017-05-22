/*
 * Written by WCUPA Computer Science club members 
 * November 2016
 */
package manifestgenerator.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohamed Alie Pussah (mp754927@wcupa.edu)
 * Encapsulates data for a single page; either 
 * from a parsed CSV file, or from a generated 
 * report
 */
public class Page
{   
    /**
     * Gets the wanted columns on the page
     */
    public final ArrayList<Column> COLUMNS;
    
    /**
     * Gets the summary rows on the page
     */
    public final Map<String, String> SUMMARIES;
    
    /**
     * Gets the cardinal position of this 
     * page in the CSV file or report
     */
    public final int NUMBER;
    
    /**
     * Creates a new page in a report or in a 
     * parsed CSV file, given the page number
     * @param number The page number
     */
    public Page(int number) {
        NUMBER = number;
        SUMMARIES = new HashMap<>();
        COLUMNS = new ArrayList<>();
    }
}
