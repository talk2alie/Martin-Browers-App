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
    
    private IntegerProperty _referencePage;
    private StringProperty _originalFileName;
    private StringProperty _originalFilePath;
    private StringProperty _trailerPosition;
    private BooleanProperty _previewButtonDisabled;
    private BooleanProperty _printButtonDisabled;
    private BooleanProperty _exportButtonDisabled;
    
    public ManifestViewModel(Palette palette) {
        if(palette == null) {
            _referencePage = new SimpleIntegerProperty(0);
            _trailerPosition = new SimpleStringProperty("N/A");
            _originalFileName = new SimpleStringProperty("N/A");
            _originalFilePath = new SimpleStringProperty();
            _previewButtonDisabled = new SimpleBooleanProperty(true);
            _printButtonDisabled = new SimpleBooleanProperty(true);
            _exportButtonDisabled = new SimpleBooleanProperty(true);
            return;
        }
        
        _referencePage = 
                new SimpleIntegerProperty(palette.getReferencePage());
        _trailerPosition = 
                new SimpleStringProperty(palette.TRAILER_POSITION);
        _originalFileName = new SimpleStringProperty("N/A");
        _originalFilePath = new SimpleStringProperty();
        _previewButtonDisabled = new SimpleBooleanProperty(true);
        _printButtonDisabled = new SimpleBooleanProperty(true);
        _exportButtonDisabled = new SimpleBooleanProperty(true);
    }
    
    public ManifestViewModel() {
        this(null);
    }

    /**
     * @return the _referencePage
     */
    public IntegerProperty referencePageProperty() {
        return _referencePage;
    }

    /**
     * @param _referencePage the _referencePage to set
     */
    public void setReferencePage(int referencePage) {
        _referencePage.set(referencePage);
    }
    
    public int getReferencePage() {
        return _referencePage.get();
    }

    /**
     * @return the _originalFileName
     */
    public StringProperty originalFileNameProperty() {
        return _originalFileName;
    }

    /**
     * @param _originalFileName the _originalFileName to set
     */
    public void setOriginalFileName(String originalFileName) {
        _originalFileName.set(originalFileName);
    }
    
     public String getOriginalFileName() {
        return _originalFileName.get();
    }

    /**
     * @return the _originalFilePath
     */
    public StringProperty originalFilePathProperty() {
        return _originalFilePath;
    }

    /**
     * @param _originalFilePath the _originalFilePath to set
     */
    public void setOriginalFilePath(String originalFilePath) {
        _originalFilePath.set(originalFilePath);
    }
    
    public String getOriginalFilePath() {
        return _originalFilePath.get();
    }

    /**
     * @return the _trailerPosition
     */
    public StringProperty trailerPositionProperty() {
        return _trailerPosition;
    }

    /**
     * @param _trailerPosition the _trailerPosition to set
     */
    public void setTrailerPosition(String trailerPosition) {
        _trailerPosition.set(trailerPosition);
    }
    
    public String getTrailerPosition() {
        return _trailerPosition.get();
    }

    /**
     * @return the _previewButtonEnabled
     */
    public BooleanProperty previewButtonDisabledProperty() {
        return _previewButtonDisabled;
    }

    /**
     * @param _previewButtonEnabled the _previewButtonEnabled to set
     */
    public void setPreviewButtonDisabled(boolean previewButtonEnabled) {
        _previewButtonDisabled.set(previewButtonEnabled);
    }
    
    public boolean getPreviewButtonDisabled() {
        return _previewButtonDisabled.get();
    }

    /**
     * @return the _printButtonEnabled
     */
    public BooleanProperty printButtonDisabledProperty() {
        return _printButtonDisabled;
    }

    /**
     * @param _printButtonEnabled the _printButtonEnabled to set
     */
    public void setPrintButtonDisabled(boolean printButtonEnabled) {
        _printButtonDisabled.set(printButtonEnabled);
    }
    
    public boolean getPrintButtonDisabled() {
        return _printButtonDisabled.get();
    }

    /**
     * @return the _exportButtonEnabled
     */
    public BooleanProperty exportButtonDisabledProperty() {
        return _exportButtonDisabled;
    }

    /**
     * @param _exportButtonEnabled the _exportButtonEnabled to set
     */
    public void setExportButtonDisabled(boolean exportButtonEnabled) {
        _exportButtonDisabled.set(exportButtonEnabled);
    }
    
    public boolean getExportButtonDisabled() {
        return _exportButtonDisabled.get();
    }
}
