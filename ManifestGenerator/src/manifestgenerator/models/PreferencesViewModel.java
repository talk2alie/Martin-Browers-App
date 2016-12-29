/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestgenerator.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author MPussah
 */
public class PreferencesViewModel {
    private StringProperty defaultInputDirectory;
    private StringProperty defaultOutputDirectory;

    public PreferencesViewModel() {
        defaultInputDirectory = new SimpleStringProperty();
        defaultOutputDirectory = new SimpleStringProperty();
    }
    
    /**
     * @return the defaultInputDirectory
     */
    public StringProperty defaultInputDirectoryProperty() {
        return defaultInputDirectory;
    }
    
    public String getDefaultInputDirectory() {
        return defaultInputDirectory.get();
    }

    /**
     * @param defaultInputDirectory the defaultInputDirectory to set
     */
    public void setDefaultInputDirectory(String defaultInputDirectory) {
        this.defaultInputDirectory.set(defaultInputDirectory);
    }

    /**
     * @return the defaultOutputDirectory
     */
    public StringProperty defaultOutputDirectoryProperty() {
        return defaultOutputDirectory;
    }
    
    public String getDefaultOutputDirectory(){
        return defaultOutputDirectory.get();
    }

    /**
     * @param defaultOutputDirectory the defaultOutputDirectory to set
     */
    public void setDefaultOutputDirectory(String defaultOutputDirectory) {
        this.defaultOutputDirectory.set(defaultOutputDirectory);
    }
    
    
}
