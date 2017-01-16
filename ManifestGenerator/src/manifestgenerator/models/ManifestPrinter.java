/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Jan 7, 2017
 */
package manifestgenerator.models;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Task;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 *
 */
public class ManifestPrinter extends Task<Void>
{
    private final List<Palette> palettes;
    private final Stage mainStage;
    private int currentPage;
    private final int totalPageCount;
    private final ReadOnlyBooleanWrapper working;

    public ManifestPrinter(List<Palette> palettes, Stage stage) {
        if (palettes == null || palettes.isEmpty()) {
            throw new NullPointerException("The palettes to print cannot be null or empty");
        }

        if (stage == null) {
            throw new NullPointerException("This class must have a stage");
        }

        this.palettes = palettes;
        totalPageCount = palettes.size();
        currentPage = 0;
        mainStage = stage;
        working = new ReadOnlyBooleanWrapper(true);
    }

    @Override
    protected Void call() throws Exception {
        updateMessage("Getting Things Ready for Printing...");
        ArrayList<VBox> pages = new PrintView(palettes).getManifestViews();
        updateProgress(-1, totalPageCount);
        Platform.runLater(() -> {
            Printer defaultPrinter = Printer.getDefaultPrinter();
            PageLayout layout = defaultPrinter.createPageLayout(Paper.NA_LETTER,
                    PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
            
            double printableWidth = layout.getPrintableWidth();
            double printableHeight = layout.getPrintableHeight();            
            PrinterJob printerJob = PrinterJob.createPrinterJob(defaultPrinter);            
            if (printerJob == null) {
                try {
                    throw new Exception("There was a problem.");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    updateMessage("PrinterJob was null.");
                    return;
                }
            }
                        
            printerJob.getJobSettings().setPageRanges(new PageRange(1, totalPageCount));
            if(printerJob.showPrintDialog(mainStage)) {
                int endPage = printerJob.getJobSettings().getPageRanges()[0].getEndPage();                
                while (currentPage < endPage) {
                    if(pages.size() <= 0) {
                        updateMessage("We could not generate pages for printing.");
                        printerJob.cancelJob();
                        return;
                    }
                    VBox vBox = pages.get(currentPage);
                    printerJob.getJobSettings().setPageRanges(new PageRange(1, totalPageCount));
                    currentPage++;
                    updateMessage(String.format("Printing Page %s of %s...", currentPage, endPage));
                    updateProgress(currentPage, endPage);
                    vBox.setMinSize(printableWidth, printableHeight);
                    final int SCALE_X = 2, SCALE_Y = 2;                    
                    Scale scale = new Scale(SCALE_X, SCALE_Y);
                    vBox.getTransforms().add(scale);
                    //vBox.setMaxSize(printableWidth, printableHeight);
                    boolean printIsSuccessful = printerJob.printPage(vBox);
                    if (!printIsSuccessful) {
                        updateMessage("Something Went Wrong; Please Check Printer...");
                        printerJob.cancelJob();
                        return;
                    }         
                }
                printerJob.endJob();
                updateMessage("All Manifests Sent to Printer...");
                working.set(false);
            } else {
                updateMessage("Print was Cancelled...");
                updateProgress(0, 0);
                working.set(false);
            }
        });
        return null;
    }
        
    public final boolean getWorking() {
        return working.get();
    }
    
    public final ReadOnlyBooleanProperty workingProperty(){
        return working.getReadOnlyProperty();
    }
}
