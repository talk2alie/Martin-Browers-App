/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Dec 29, 2016
 */

package manifestgenerator.models;

import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 *
 */
public class PaletteManagerTask extends Task<Void> {
    
    private final ObservableList<Palette> palettes;
    private final File file;
    
    public PaletteManagerTask(File csvFile) {
        file = csvFile;
        palettes = FXCollections.observableArrayList();
    }

    @Override
    protected Void call() throws Exception {
        return null;
    }

}
