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
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.layout.VBox;
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
            if (printerJob != null && printerJob.showPrintDialog(mainStage)) {
                for (VBox vBox : pages) {
                    currentPage++;
                    updateMessage(String.format("Printing Page %s of %s...",
                            currentPage, totalPageCount));
                    vBox.setPrefSize(printableWidth, printableHeight);
                    boolean printIsSuccessful = printerJob.printPage(vBox);
                    if (!printIsSuccessful) {
                        updateMessage("Something Went Wrong; Please Check Printer...");
                        printerJob.cancelJob();
                        return;
                    }
                    updateProgress(currentPage, totalPageCount);
                    updateMessage(String.format("Sent Page %s of %s to Printer...",
                            currentPage, totalPageCount));
                }
                printerJob.endJob();
                updateMessage("All Manifests Sent to Printer...");
                working.set(false);
            } else {
                updateMessage("Print was Cancelled or Something Went Wrong...");
                updateProgress(currentPage, totalPageCount);
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
