/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Dec 23, 2016
 */

package manifestgenerator.models;

import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

/**
 *
 */
public class ManifestViewModel {
    
    private IntegerProperty referencePage;
    private StringProperty originalFileName;
    private StringProperty originalFilePath;
    private StringProperty trailerPosition;
    private BooleanProperty printButtonDisabled;
    private BooleanProperty exportButtonDisabled;
    private IntegerProperty totalPageCountInFile;
    
    public ManifestViewModel(Palette palette) {
        if(palette == null) {
            referencePage = new SimpleIntegerProperty();
            trailerPosition = new SimpleStringProperty("N/A");
            originalFileName = new SimpleStringProperty("N/A");
            originalFilePath = new SimpleStringProperty();
            printButtonDisabled = new SimpleBooleanProperty(true);
            exportButtonDisabled = new SimpleBooleanProperty(true);
            totalPageCountInFile = new SimpleIntegerProperty();
            return;
        }
        
        referencePage = 
                new SimpleIntegerProperty(palette.getReferencePage());
        trailerPosition = 
                new SimpleStringProperty(palette.TRAILER_POSITION);
        originalFileName = new SimpleStringProperty("N/A");
        originalFilePath = new SimpleStringProperty();
        printButtonDisabled = new SimpleBooleanProperty(true);
        exportButtonDisabled = new SimpleBooleanProperty(true);
        totalPageCountInFile = new SimpleIntegerProperty();
    }
    
    public ManifestViewModel() {
        this(null);
    }

    /**
     * @return the _referencePage
     */
    public IntegerProperty referencePageProperty() {
        return referencePage;
    }

    /**
     * @param _referencePage the _referencePage to set
     */
    public void setReferencePage(int referencePage) {
        this.referencePage.set(referencePage);
    }
    
    public int getReferencePage() {
        return referencePage.get();
    }

    /**
     * @return the _originalFileName
     */
    public StringProperty originalFileNameProperty() {
        return originalFileName;
    }

    /**
     * @param _originalFileName the _originalFileName to set
     */
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName.set(originalFileName);
    }
    
     public String getOriginalFileName() {
        return originalFileName.get();
    }

    /**
     * @return the _originalFilePath
     */
    public StringProperty originalFilePathProperty() {
        return originalFilePath;
    }

    /**
     * @param _originalFilePath the _originalFilePath to set
     */
    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath.set(originalFilePath);
    }
    
    public String getOriginalFilePath() {
        return originalFilePath.get();
    }

    /**
     * @return the _trailerPosition
     */
    public StringProperty trailerPositionProperty() {
        return trailerPosition;
    }

    /**
     * @param _trailerPosition the _trailerPosition to set
     */
    public void setTrailerPosition(String trailerPosition) {
        this.trailerPosition.set(trailerPosition);
    }
    
    public String getTrailerPosition() {
        return trailerPosition.get();
    }

    /**
     * @return the _printButtonEnabled
     */
    public BooleanProperty printButtonDisabledProperty() {
        return printButtonDisabled;
    }

    /**
     * @param _printButtonEnabled the _printButtonEnabled to set
     */
    public void setPrintButtonDisabled(boolean printButtonDisabled) {
        this.printButtonDisabled.set(printButtonDisabled);
    }
    
    public boolean getPrintButtonDisabled() {
        return printButtonDisabled.get();
    }

    /**
     * @return the _exportButtonEnabled
     */
    public BooleanProperty exportButtonDisabledProperty() {
        return exportButtonDisabled;
    }

    /**
     * @param _exportButtonEnabled the _exportButtonEnabled to set
     */
    public void setExportButtonDisabled(boolean exportButtonDisabled) {
        this.exportButtonDisabled.set(exportButtonDisabled);
    }
    
    public boolean getExportButtonDisabled() {
        return exportButtonDisabled.get();
    }
    
    public IntegerProperty totalPageCountInFileProperty() {
        return totalPageCountInFile;
    }
    
    public void setTotalPageCountInFile(int pageCount) {
        this.totalPageCountInFile.set(pageCount);
    }
    
    public int getTotalPageCountInFile() {
        return totalPageCountInFile.get();
    }
}
