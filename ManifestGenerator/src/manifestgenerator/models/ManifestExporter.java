/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Jan 7, 2017
 */
package manifestgenerator.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

/**
 *
 */
public class ManifestExporter extends Task<Void>
{

    private final List<Palette> palettes;
    private String initialFileName;
    private final Stage mainStage;
    private final String defaultOutputDirectory;
    private int currentPage;
    private final ReadOnlyBooleanWrapper working;
    private File file;

    public ManifestExporter(List<Palette> palettes,
            Stage stage,
            String outputDirectory) {
        this.palettes = palettes;
        mainStage = stage;
        defaultOutputDirectory = outputDirectory;
        currentPage = 0;
        working = new ReadOnlyBooleanWrapper(true);
        file = null;
    }

    private void createFooter(XWPFDocument manifestDocument, Palette palette) {
        final int FONT_SIZE = 20;

        XWPFParagraph footer = manifestDocument.createParagraph();
        footer.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun footerRun = footer.createRun();
        footerRun.setBold(true);
        footerRun.setFontFamily("Segoe UI");
        footerRun.setFontSize(FONT_SIZE);
        footerRun.setText(String.format("CART TOTAL: %s", palette.getCaseCount()));
        footerRun.addBreak();
        footerRun.addBreak();
        footerRun.addBreak();
        footerRun.addBreak();
        footerRun.addBreak();
        footerRun.addBreak();

        createHeader(manifestDocument, palette);
        createsubHeader(manifestDocument, palette);
    }

    private void createsubHeader(XWPFDocument manifestDocument, Palette palette) {
        final int FONT_SIZE = 28;

        XWPFParagraph subheader = manifestDocument.createParagraph();
        subheader.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun subheaderRun = subheader.createRun();
        subheaderRun.setFontFamily("Segoe UI Light");
        subheaderRun.setFontSize(FONT_SIZE);
        subheaderRun.setText(String.format("Trailer Position: %s", palette.TRAILER_POSITION));
    }

    private void createHeader(XWPFDocument manifestDocument, Palette palette) {
        final int FONT_SIZE = 35;

        XWPFParagraph header = manifestDocument.createParagraph();
        header.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun headerRun = header.createRun();
        headerRun.setFontFamily("Segoe UI Black");
        headerRun.setFontSize(FONT_SIZE);
        headerRun.setText(String.format("Route: %s", palette.getRouteInfo()));
        headerRun.addTab();
        headerRun.addTab();
        headerRun.setText(String.format("Stop: %s", palette.getStopInfo()));
    }

    private void createDocument(File file) throws FileNotFoundException, IOException {
        updateMessage("Getting Things Ready...");
        updateProgress(-1, palettes.size());
        
        InputStream baseFileStream = new FileInputStream("manifest_base.docx");
        XWPFDocument manifestDocument = new XWPFDocument(baseFileStream);

        final int HEADER_ROW = 0;
        final int WRIN_COLUMN = 0;
        final int DESCRIPTION_COLUMN = 1;
        final int CASES_COLUMN = 2;
        final int STOP_COLUMN = 3;
        final int COLUMN_COUNT = 4;
        final String COLUMN_WIDTH = "3333";
        final String LARGE_COLUMN_WIDTH = "9999";
        final int FONT_SIZE = 20;

        final String TABLE_STYLE = "MartinBrowersTable";

        int processedPalettesCount = 0;
        // Note that each palette can produce a single manifest page
        for (Palette palette : palettes) {
            updateMessage(String.format("Exporting Page %s of %s...", ++currentPage, palettes.size()));
            updateProgress(currentPage, palettes.size());

            createHeader(manifestDocument, palette);
            createsubHeader(manifestDocument, palette);
            // Add 1 for header row            
            int rowCount = palette.CASES.size() + 1;
            // Create cases table
            XWPFTable casesTable = manifestDocument.createTable(rowCount, COLUMN_COUNT);
            CTTblPr tableProperties = casesTable.getCTTbl().getTblPr();
            CTString styleStr = tableProperties.addNewTblStyle();
            styleStr.setVal(TABLE_STYLE);
            List<XWPFTableRow> rows = casesTable.getRows();
            int rowIndex = 0;
            int columnIndex = 0;
            for (XWPFTableRow row : rows) {
                // Get the cells in this row
                List<XWPFTableCell> cells = row.getTableCells();
                // Add content to each cell
                for (XWPFTableCell cell : cells) {
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                    // Get a table cell properties element (tcPr)
                    CTTcPr cellProperties = cell.getCTTc().addNewTcPr();
                    CTTblWidth cellWidth = cellProperties.addNewTcW();
                    cellWidth.setW(new BigInteger(COLUMN_WIDTH));  // sets width

                    // Get 1st paragraph in cell's paragraph list
                    XWPFParagraph cellParagraph = cell.getParagraphs().get(0);
                    // Create a run to contain the content
                    XWPFRun cellRun = cellParagraph.createRun();
                    cellRun.setFontSize(FONT_SIZE);
                    cellParagraph.setAlignment(ParagraphAlignment.CENTER);
                    // Set values
                    if (rowIndex == HEADER_ROW) {
                        cellRun.setBold(true);
                        if (columnIndex == WRIN_COLUMN) {
                            cellRun.setText("WRIN");
                        }

                        if (columnIndex == DESCRIPTION_COLUMN) {
                            cellRun.setText("DESCRIPTION");
                            cellWidth.setW(new BigInteger(LARGE_COLUMN_WIDTH));
                        }

                        if (columnIndex == CASES_COLUMN) {
                            cellRun.setText("CASES");
                        }

                        if (columnIndex == STOP_COLUMN) {
                            cellRun.setText("STOP");
                        }
                    }
                    else {
                        Cases cases = palette.getSortedCaseList().get(rowIndex - 1);
                        if (columnIndex == WRIN_COLUMN) {
                            cellRun.setText(cases.getContentId());
                        }

                        if (columnIndex == DESCRIPTION_COLUMN) {
                            cellRun.setText(cases.getContent());
                            cellWidth.setW(new BigInteger(LARGE_COLUMN_WIDTH));
                        }

                        if (columnIndex == CASES_COLUMN) {
                            cellRun.setText(
                                    String.format("%s", cases.getQuantity()));
                        }

                        if (columnIndex == STOP_COLUMN) {
                            cellRun.setText(
                                    String.format("%s", cases.getStop()));
                        }
                    }
                    columnIndex++;
                } // for cell
                columnIndex = 0;
                rowIndex++;
            } // for row

            // Create Cart Total
            createFooter(manifestDocument, palette);

            // Create page break
            processedPalettesCount++;
            if (processedPalettesCount < palettes.size()) {
                XWPFParagraph pageBreakParagraph = manifestDocument.createParagraph();
                pageBreakParagraph.setPageBreak(Boolean.TRUE);
            }
        }

        FileOutputStream manifestFileStream = new FileOutputStream(file);
        manifestDocument.write(manifestFileStream);
        baseFileStream.close();
        manifestFileStream.close();
        manifestDocument.close();
    }

    @Override
    protected void failed() {
        super.failed();
        updateMessage("Something Went Wrong...");
        updateProgress(0, palettes.size());
    }

    @Override
    protected Void call() throws Exception {
        updateMessage("Generating Initial File Name...");
        updateProgress(0, palettes.size());

        Platform.runLater(() -> {
            Date cureentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy_hhmmss");
            initialFileName = String.format("Manifest_%s", dateFormat.format(cureentDate));
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Manifests to MS Word");
            fileChooser.getExtensionFilters().clear();
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Microsoft Word", "*.docx"));
            if (defaultOutputDirectory != null && defaultOutputDirectory.length() > 0) {
                File outputDirectory = new File(defaultOutputDirectory);
                fileChooser.setInitialDirectory(outputDirectory);
            }
            fileChooser.setInitialFileName(initialFileName);
            updateMessage("Getting File...");
            file = fileChooser.showSaveDialog(mainStage);
            if (file == null) {
                updateMessage("Export Cancelled...");
                updateProgress(0, palettes.size());
                cancel();
                working.set(false);
            }
            else {
                try {
                    createDocument(file);
                    updateMessage("Export Completed...");
                    working.set(false);
                }
                catch (IOException ex) {
                    // TODO: Log Error
                    ex.printStackTrace();
                }
            }

        });

        return null;
    }

    public final boolean getWorking() {
        return working.get();
    }

    public final ReadOnlyBooleanProperty workingProperty() {
        return working.getReadOnlyProperty();
    }
}
