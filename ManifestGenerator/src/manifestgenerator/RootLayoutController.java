/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestgenerator;

import com.sun.javafx.iio.ImageStorage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import manifestgenerator.models.Cases;
import manifestgenerator.models.PrintView;
import manifestgenerator.models.ManifestViewModel;
import manifestgenerator.models.Palette;
import manifestgenerator.models.PaletteListViewCell;
import manifestgenerator.models.PaletteManager;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

/**
 * FXML Controller class
 *
 * @author talk2
 */
public class RootLayoutController implements Initializable
{
    // <editor-fold defaultstate="collapsed" desc="Fields">

    private Stage mainStage;
    private final ManifestViewModel viewModel;
    private final ObservableList<Palette> palettes;

    @FXML
    private TextField browseTextField;

    @FXML
    private Button previewButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label originalFileLabel;

    @FXML
    private Label foundOnPageLabel;

    @FXML
    private Label trailerPositionLabel;

    @FXML
    private Button printButton;

    @FXML
    private Button exportButton;

    @FXML
    private ListView<Palette> manifestListView;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Action Handlers">
    @FXML
    void onBrowseAction(ActionEvent event) {
        System.out.println("Browsing...");

        // Clear everything
        palettes.clear();
        viewModel.setOriginalFilePath(null);
        viewModel.setOriginalFileName("N/A");
        viewModel.setPreviewButtonDisabled(Boolean.TRUE);
        manifestListView.getSelectionModel().clearSelection();
        viewModel.setTotalPageCountInFile(0);
        viewModel.setPrintButtonDisabled(Boolean.TRUE);
        viewModel.setExportButtonDisabled(Boolean.TRUE);
        viewModel.setReferencePage(0);
        viewModel.setTotalPageCountInFile(0);
        viewModel.setTrailerPosition("N/A");

        // Process new file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Manifest Data File");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters()
                .add(new ExtensionFilter("Comma Separated Values", "*.csv"));
        File file = fileChooser.showOpenDialog(mainStage);
        if (file != null) {
            viewModel.setOriginalFilePath(file.getPath());
            viewModel.setOriginalFileName(file.getName());

            viewModel.setPreviewButtonDisabled(Boolean.FALSE);
        }
    }

    @FXML
    void onExportAction(ActionEvent event) {
        System.out.println("Exporting Manifests...");
        Date cureentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy_hhmmss");
        String intialFileName = String.format("Manifest_%s",
                dateFormat.format(cureentDate));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Manifests");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Microsoft Word", "*.docx"));
        fileChooser.setInitialFileName(intialFileName);
        File fileName = fileChooser.showSaveDialog(mainStage);

        try {
            createDocument(fileName.getPath());
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

    @FXML
    void onPreviewManifestsAction(ActionEvent event) {
        System.out.println("Previewing Manifests...");

        try {
            PaletteManager paletteManager
                    = new PaletteManager(viewModel.getOriginalFilePath());
            palettes.addAll(paletteManager.PALETTES);
            if (paletteManager.PALETTES.size() > 0) {
                manifestListView.getSelectionModel().select(0);
                viewModel.setTotalPageCountInFile(
                        paletteManager.getTotlaPageCount());
                viewModel.setPrintButtonDisabled(Boolean.FALSE);
                viewModel.setExportButtonDisabled(Boolean.FALSE);
            }
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    void onPrintAction(ActionEvent event) throws IOException {
        
        Printer defaultPrinter = Printer.getDefaultPrinter();
        PageLayout layout = defaultPrinter.createPageLayout(Paper.NA_LETTER, 
                PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double printableWidth = layout.getPrintableWidth();
        double printableHeight = layout.getPrintableHeight();
        
        PrinterJob printerJob = PrinterJob.createPrinterJob(defaultPrinter);
        if(printerJob != null && printerJob.showPrintDialog(mainStage)){
            ArrayList<VBox> pages = new PrintView(palettes).getManifestViews();
            for(VBox vBox : pages) {
                vBox.setPrefSize(printableWidth, printableHeight);
                boolean printIsSuccessful = printerJob.printPage(vBox);
                if(!printIsSuccessful) {
                    // Bind the jobStatusProperty to some UI control
                    // Notify user of a possible error
                    printerJob.cancelJob();
                    return;
                }                          
            }
            printerJob.endJob();
            // Notify user that this page has been sent to the printer      
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Helpers">
    private void createDocument(String fileName) throws FileNotFoundException,
            IOException {

        // Styles
        final String CART_STYLE = "MartinBrowersCartTotal";
        final String HEADER_STYLE = "MartinBrowersPageHeader";
        final String SUB_HEADER_STYLE = "MartinBrowersPageSubHeader";
        final String TABLE_STYLE = "MartinBrowersTable";

        // Table Settings
        final int HEADER_ROW = 0;
        final int WRIN_COLUMN = 0;
        final int DESCRIPTION_COLUMN = 1;
        final int CASES_COLUMN = 2;
        final int STOP_COLUMN = 3;
        final int COLUMN_COUNT = 4;

        InputStream baseFileStream = new FileInputStream("manifest_base.docx");
        XWPFDocument manifestDocument = new XWPFDocument(baseFileStream);

        for (Palette palette : palettes) {
            // Add 1 for header row
            int rowCount = palette.CASES.size() + 1;
            List<Cases> allCases = palette.getSortedCaseList();

            // header
            XWPFParagraph headerParagraph = manifestDocument.createParagraph();
            headerParagraph.setStyle(HEADER_STYLE);
            XWPFRun headerRun = headerParagraph.createRun();

            headerRun.setText(String.format("Route: %s\t\t\t\tStop: %s",
                    palette.getRouteInfo(), palette.getStopInfo()));

            // Sub header
            XWPFParagraph subHeaderParagraph = manifestDocument
                    .createParagraph();
            subHeaderParagraph.setStyle(SUB_HEADER_STYLE);
            XWPFRun subHeaderRun = subHeaderParagraph.createRun();
            subHeaderRun.setText(String.format("Trailer Position: %s",
                    palette.TRAILER_POSITION));

            XWPFTable casesTable = manifestDocument
                    .createTable(rowCount, COLUMN_COUNT);
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
                    // Get a table cell properties element (tcPr)
                    CTTcPr cellProperties = cell.getCTTc().addNewTcPr();
                    // Set vertical alignment to "center"
                    CTVerticalJc verticalAlignment = cellProperties.addNewVAlign();
                    verticalAlignment.setVal(STVerticalJc.CENTER);

                    // Get 1st paragraph in cell's paragraph list
                    XWPFParagraph cellParagraph = cell.getParagraphs().get(0);
                    // Create a run to contain the content
                    XWPFRun cellRun = cellParagraph.createRun();
                    // Set values
                    if (rowIndex == HEADER_ROW) {
                        cellRun.setBold(true);
                        cellParagraph.setAlignment(ParagraphAlignment.CENTER);

                        if (columnIndex == WRIN_COLUMN) {
                            cellRun.setText("WRIN");
                        }

                        if (columnIndex == DESCRIPTION_COLUMN) {
                            cellRun.setText("DESCRIPTION");
                        }

                        if (columnIndex == CASES_COLUMN) {
                            cellRun.setText("CASES");
                        }

                        if (columnIndex == STOP_COLUMN) {
                            cellRun.setText("STOP");
                        }
                    }
                    else {
                        Cases cases = palette.getSortedCaseList()
                                .get(rowIndex - 1);
                        cellParagraph.setAlignment(ParagraphAlignment.LEFT);

                        if (columnIndex == WRIN_COLUMN) {
                            cellRun.setText(cases.getContentId());
                        }

                        if (columnIndex == DESCRIPTION_COLUMN) {
                            cellRun.setText(cases.getContent());
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

            // Cart paragraph
            XWPFParagraph cartParagraph = manifestDocument.createParagraph();
            cartParagraph.setStyle(CART_STYLE);
            XWPFRun cartRun = cartParagraph.createRun();
            cartRun.setText(
                    String.format("CART TOTAL: %s", palette.getCaseCount()));

            // Closing paragraph
            XWPFParagraph closingParagraph = manifestDocument.createParagraph();
            closingParagraph.setPageBreak(true);
        } // for palette

        FileOutputStream manifestFileStream = new FileOutputStream(fileName);
        manifestDocument.write(manifestFileStream);
        baseFileStream.close();
        manifestFileStream.close();
        manifestDocument.close();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        browseTextField.textProperty()
                .bind(viewModel.originalFilePathProperty());
        previewButton.disableProperty()
                .bind(viewModel.previewButtonDisabledProperty());
        printButton.disableProperty()
                .bind(viewModel.printButtonDisabledProperty());
        exportButton.disableProperty()
                .bind(viewModel.exportButtonDisabledProperty());

        manifestListView.setItems(palettes);
        manifestListView.setCellFactory(listView -> new PaletteListViewCell());
        manifestListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    viewModel.setTrailerPosition(newValue.TRAILER_POSITION);
                    viewModel.setReferencePage(newValue.getReferencePage());
                });

        originalFileLabel.textProperty().bind(Bindings.concat(
                "Original File: ", viewModel.originalFileNameProperty()
        ));

        foundOnPageLabel.textProperty().bind(Bindings.concat(
                "Found on Page: ", viewModel.referencePageProperty(), " of ",
                viewModel.totalPageCountInFileProperty()
        ));

        trailerPositionLabel.textProperty().bind(Bindings.concat(
                "Trailer Position: ", viewModel.trailerPositionProperty()
        ));
    }

    public void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public RootLayoutController() {
        viewModel = new ManifestViewModel();
        palettes = FXCollections.observableArrayList();
    }

    // </editor-fold>
}
