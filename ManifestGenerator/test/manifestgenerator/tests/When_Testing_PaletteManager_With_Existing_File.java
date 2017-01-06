/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestgenerator.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import manifestgenerator.models.Column;
import manifestgenerator.models.Page;
import manifestgenerator.models.PaletteManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author talk2
 */
public class When_Testing_PaletteManager_With_Existing_File
{    
//    private final String _fileName;
//    public When_Testing_PaletteManager_With_Existing_File() {
//        _fileName = "C:\\Users\\talk2\\Desktop\\Files\\112816a.csv";
////        _fileName = "C:\\Users\\talk2\\Desktop\\Files\\BunManifest2.csv";
//        _expectedPageCount = 90;
////        _fileName = "C:\\Users\\talk2\\Desktop\\Files\\Report1.csv";
////        _expectedPageCount = 295;
//        _expectCartTotal = true;
//    }
//    
//    // This value should reflect the number of pages in the CSV file
//    // We also have to make this value dynamic based on the CSV file    
//    private final int _expectedPageCount;
//    private final boolean _expectCartTotal;    
//
//    @Test
//    public void instance_should_be_created() {
//        try {
//            PaletteManager csvReader = new PaletteManager(_fileName);
//            assertTrue(true); // Instance created; therefore, test passed!
//        }
//        catch (FileNotFoundException ex) {
//            // TODO: Log the exception
//            assertTrue(ex.getMessage(), false); // Test failed! :-(
//        }
//        catch (IOException ex) {
//            assertTrue(ex.getMessage(), false); // Test failed! :-(
//        }
//    }
//
//    @Test
//    public void instance_page_count_should_equal_page_count_in_file() {
//        try {
//
//            PaletteManager reader = new PaletteManager(_fileName);
//            assertEquals(_expectedPageCount, reader.PAGES.size());
//        }
//        catch (IOException ex) {
//            //TODO: Log the exception
//            assertTrue(ex.getMessage(), false); // Test failed! :-(
//        }
//    }
//
//    @Test
//    public void instance_pages_should_each_have_at_least_one_cell_in_their_columns() {
//        try {
//
//            PaletteManager reader = new PaletteManager(_fileName);
//            boolean eachColumnHasAtLeastOnceCell = false;
//            for (Page page : reader.PAGES) {
//                for (Column column : page.COLUMNS) {
//                    eachColumnHasAtLeastOnceCell = column.CELLS.size() > 0;
//                }
//            }
//            assertTrue(eachColumnHasAtLeastOnceCell);
//        }
//        catch (IOException ex) {
//            //TODO: Log the exception
//            assertTrue(ex.getMessage(), false); // Test failed! :-(
//        }
//    }    
//
//    @Test
//    public void instance_should_output_parsed_data_to_given_file() {
//        try {
//            String fileName = "C:\\Users\\talk2\\Desktop\\Files\\TestOutput.csv";
//            PaletteManager reader = new PaletteManager(_fileName);
//            reader.saveAsCsv(fileName);
//            File file = new File(fileName);
//            assertTrue(file.exists());
//        }
//        catch (IOException ex) {
//            //TODO: Log the exception           
//            assertTrue(ex.getMessage(), false); // Test failed! :-(
//        }
//    }
//    
//    @Test
//    public void instance_should_output_palettes_to_given_file() {
//        try {
//            String fileName = "C:\\Users\\talk2\\Desktop\\Files\\palettes.csv";
//            PaletteManager reader = new PaletteManager(_fileName);
//            reader.saveAllPalettesAsCsv(fileName);
//            File file = new File(fileName);
//            assertTrue(file.exists());
//        }
//        catch (IOException ex) {
//            //TODO: Log the exception           
//            assertTrue(ex.getMessage(), false); // Test failed! :-(
//        }
//    }
}
