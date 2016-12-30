/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Dec 23, 2016
 */

package manifestgenerator.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 */
public class ManifestViewModel {
    
    private IntegerProperty referencePage;
    private StringProperty originalFileName;
    private StringProperty originalFilePath;
    private IntegerProperty currentPageInManifest;
    private IntegerProperty totalPagesInManifest;
    private BooleanProperty printButtonDisabled;
    private BooleanProperty exportButtonDisabled;
    private IntegerProperty totalPageCountInFile;
    private BooleanProperty previousButtonDisabled;
    private BooleanProperty nextButtonDisabled;
    private IntegerProperty currentIndex;
    private DoubleProperty progress;
    private StringProperty progressText;
    
    public ManifestViewModel(Palette palette) {
        if(palette == null) {
            referencePage = new SimpleIntegerProperty();
        }
        else {
            referencePage = new SimpleIntegerProperty(palette.getReferencePage());
        }
        
        
        currentPageInManifest = new SimpleIntegerProperty();
        totalPagesInManifest = new SimpleIntegerProperty();
        originalFileName = new SimpleStringProperty("N/A");
        originalFilePath = new SimpleStringProperty();
        printButtonDisabled = new SimpleBooleanProperty(true);
        exportButtonDisabled = new SimpleBooleanProperty(true);
        totalPageCountInFile = new SimpleIntegerProperty();
        previousButtonDisabled = new SimpleBooleanProperty(Boolean.TRUE);
        nextButtonDisabled = new SimpleBooleanProperty(Boolean.TRUE);
        currentIndex = new SimpleIntegerProperty();
        progress = new SimpleDoubleProperty();
        progressText = new SimpleStringProperty();
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
    public IntegerProperty currentPageInManifestProperty() {
        return currentPageInManifest;
    }

    /**
     * @param _trailerPosition the _trailerPosition to set
     */
    public void setCurrentPageInManifest(int currentPageInManifest) {
        this.currentPageInManifest.set(currentPageInManifest);
    }
    
    public int getCurrentPageInManifest() {
        return currentPageInManifest.get();
    }
    
    /**
     * @return the _trailerPosition
     */
    public IntegerProperty totalPagesInManifestProperty() {
        return totalPagesInManifest;
    }

    /**
     * @param _trailerPosition the _trailerPosition to set
     */
    public void setTotalPagesInManifest(int totalPagesInManifest) {
        this.totalPagesInManifest.set(totalPagesInManifest);
    }
    
    public int getTotalPagesInManifest() {
        return totalPagesInManifest.get();
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

    /**
     * @return the previousButtonDisabled
     */
    public BooleanProperty previousButtonDisabledProperty() {
        return previousButtonDisabled;
    }
    
    public boolean getPreviousButtonDisabled() {
        return previousButtonDisabled.get();
    }

    /**
     * @param previousButtonDisabled the previousButtonDisabled to set
     */
    public void setPreviousButtonDisabled(boolean previousButtonDisabled) {
        this.previousButtonDisabled.set(previousButtonDisabled);
    }

    /**
     * @return the nextButtonDisabled
     */
    public BooleanProperty nextButtonDisabledProperty() {
        return nextButtonDisabled;
    }
    
    public boolean getNextButtonDisabled() {
        return nextButtonDisabled.get();
    }

    /**
     * @param nextButtonDisabled the nextButtonDisabled to set
     */
    public void setNextButtonDisabled(boolean nextButtonDisabled) {
        this.nextButtonDisabled.set(nextButtonDisabled);
    }

    /**
     * @return the currentIndex
     */
    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }
    
    public int getCurrentIndex() {
        return currentIndex.get();
    }

    /**
     * @param currentIndex the currentIndex to set
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex.set(currentIndex);
    }

    /**
     * @return the progress
     */
    public DoubleProperty progressProperty() {
        return progress;
    }
    
    public double getProgress() {
        return progress.get();
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    /**
     * @return the progressText
     */
    public StringProperty progressTextProperty() {
        return progressText;
    }
    
    public String getProgressText() {
        return progressText.get();
    }

    /**
     * @param progressText the progressText to set
     */
    public void setProgressText(String progressText) {
        this.progressText.set(progressText);
    }
    
       
}
