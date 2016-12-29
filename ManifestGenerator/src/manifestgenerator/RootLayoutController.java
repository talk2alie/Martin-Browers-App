/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestgenerator;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
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
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import manifestgenerator.models.Cases;
import manifestgenerator.models.PrintView;
import manifestgenerator.models.ManifestViewModel;
import manifestgenerator.models.Palette;
import manifestgenerator.models.PaletteListViewCell;
import manifestgenerator.models.PaletteManager;
import manifestgenerator.models.PreferencesViewModel;
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
public class RootLayoutController implements Initializable {
    // <editor-fold defaultstate="collapsed" desc="Fields">
    private Stage mainStage;
    private final ManifestViewModel viewModel;
    private final ObservableList<Palette> palettes;
    private PreferencesViewModel preferencesViewModel;
    private final String PREFERENCES = "Preferences.xml";
    private PreferencesController preferencesController;

    @FXML
    private TextField browseTextField;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label originalFileLabel;

    @FXML
    private Label foundOnPageLabel;

    @FXML
    private Label manifestPositionLabel;

    @FXML
    private Button printButton;

    @FXML
    private Button exportButton;

    @FXML
    private ListView<Palette> manifestListView;

    @FXML
    private MenuItem browseMenuItem;

    @FXML
    private MenuItem preferencesMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem helpMenuItem;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Action Handlers">
    @FXML
    void onBrowseAction(ActionEvent event) {
        onBrowse();
    }

    @FXML
    void onExportAction(ActionEvent event) {
        onExportToWord();
    }

    @FXML
    void onPrintAction(ActionEvent event) throws IOException {
        onPrint();
    }

    @FXML
    void onHelpMenuAction(ActionEvent event) {
        onHelpMenuAction();
    }

    @FXML
    void onPreferencesAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/manifestgenerator/views/Preferences.fxml"));
            BorderPane page = loader.load();

            preferencesController = loader.getController();
            preferencesController.setStage(mainStage);
            preferencesController.setViewModel(preferencesViewModel.getDefaultInputDirectory(), 
                    preferencesViewModel.getDefaultOutputDirectory());

            Stage preferencesDialog = new Stage();
            preferencesDialog.setResizable(Boolean.FALSE);
            preferencesDialog.setTitle("Modify Preferences");
            preferencesDialog.initModality(Modality.WINDOW_MODAL);
            preferencesDialog.initOwner(mainStage);
            final int WIDTH = 700, HEIGHT = 200;
            Scene preferencesScene = new Scene(page, WIDTH, HEIGHT);
            preferencesDialog.setScene(preferencesScene);
            preferencesDialog.setOnCloseRequest(closeEvent -> {
                System.out.println("Preferences closed");
                preferencesController.savePreferences(PREFERENCES);
                preferencesDialog.close();
                closeEvent.consume();
            });

            preferencesDialog.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void onExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onAboutAction(ActionEvent event) {
        // Create a custom UI for this
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About Manifest Generator");
        alert.setContentText("We are awesome!!");
        alert.showAndWait();
    }

    @FXML
    void onPreviousScroll(ActionEvent event) {
        scrollPrevious();
    }

    @FXML
    void onNextScroll(ActionEvent event) {
        scrollNext();
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
                    } else {
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

    private void onBrowse() {
        // Clear everything
        palettes.clear();
        viewModel.setOriginalFilePath(null);
        viewModel.setOriginalFileName("N/A");
        manifestListView.getSelectionModel().clearSelection();
        viewModel.setTotalPageCountInFile(0);
        viewModel.setPrintButtonDisabled(Boolean.TRUE);
        viewModel.setExportButtonDisabled(Boolean.TRUE);
        viewModel.setReferencePage(0);
        viewModel.setTotalPageCountInFile(0);
        viewModel.setCurrentPageInManifest(0);
        viewModel.setTotalPagesInManifest(0);
        viewModel.setCurrentIndex(0);
        viewModel.setNextButtonDisabled(Boolean.TRUE);
        viewModel.setPreviousButtonDisabled(Boolean.TRUE);
              
        // Process new file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Manifest Data File");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters()
                .add(new ExtensionFilter("Comma Separated Values", "*.csv"));        
        if(preferencesViewModel.getDefaultInputDirectory() != null) {
            File inputDirectory = 
                    new File(preferencesViewModel.getDefaultInputDirectory());
            fileChooser.setInitialDirectory(inputDirectory);
        }        
        File file = fileChooser.showOpenDialog(mainStage);
        if (file != null) {
            viewModel.setOriginalFilePath(file.getPath());
            viewModel.setOriginalFileName(file.getName());

            try {
                PaletteManager paletteManager
                        = new PaletteManager(viewModel.getOriginalFilePath());
                palettes.addAll(paletteManager.PALETTES);
                if (palettes.size() > 0) {
                    manifestListView.getSelectionModel().select(0);
                    viewModel.setTotalPageCountInFile(
                            paletteManager.getTotlaPageCount());
                    viewModel.setPrintButtonDisabled(Boolean.FALSE);
                    viewModel.setExportButtonDisabled(Boolean.FALSE);
                    viewModel.setPrintButtonDisabled(Boolean.FALSE);
                    if (palettes.size() > manifestListView
                            .lookupAll("#manifestVBox").size()) {
                        viewModel.setNextButtonDisabled(Boolean.FALSE);
                    }
                }
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
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
        printButton.disableProperty()
                .bind(viewModel.printButtonDisabledProperty());
        exportButton.disableProperty()
                .bind(viewModel.exportButtonDisabledProperty());

        manifestListView.setItems(palettes);
        manifestListView.setCellFactory(listView -> new PaletteListViewCell());
        manifestListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        viewModel.setCurrentPageInManifest(
                                manifestListView.getSelectionModel()
                                        .getSelectedIndex() + 1
                        );
                        viewModel.setTotalPagesInManifest(palettes.size());
                        viewModel.setReferencePage(newValue.getReferencePage());
                    }
                });

        originalFileLabel.textProperty().bind(Bindings.concat(
                "Original File: ", viewModel.originalFileNameProperty()
        ));

        foundOnPageLabel.textProperty().bind(Bindings.concat(
                "Found on Page: ", viewModel.referencePageProperty(), " of ",
                viewModel.totalPageCountInFileProperty()
        ));

        manifestPositionLabel.textProperty().bind(Bindings.concat(
                "Manifest: Page ",
                viewModel.currentPageInManifestProperty(),
                " of ", viewModel.totalPagesInManifestProperty()
        ));

        helpMenuItem.setAccelerator(KeyCombination.keyCombination("F1"));
        previousButton.disableProperty()
                .bind(viewModel.previousButtonDisabledProperty());
        nextButton.disableProperty()
                .bind(viewModel.nextButtonDisabledProperty());
    }

    public void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public void onExportToWord() {

        if (exportButton.isDisabled()) {
            return;
        }

        Date cureentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy_hhmmss");
        String intialFileName = String.format("Manifest_%s",
                dateFormat.format(cureentDate));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Manifests to MS Word");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Microsoft Word", "*.docx"));
        if(preferencesViewModel.getDefaultInputDirectory() != null) {
            File inputDirectory = 
                    new File(preferencesViewModel.getDefaultInputDirectory());
            fileChooser.setInitialDirectory(inputDirectory);
        }
        fileChooser.setInitialFileName(intialFileName);
        File file = fileChooser.showSaveDialog(mainStage);
        if (file == null) {
            return;
        }

        try {
            createDocument(file.getPath());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void onPrint() throws IOException {

        if (printButton.isDisabled()) {
            return;
        }

        Printer defaultPrinter = Printer.getDefaultPrinter();
        PageLayout layout = defaultPrinter.createPageLayout(Paper.NA_LETTER,
                PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double printableWidth = layout.getPrintableWidth();
        double printableHeight = layout.getPrintableHeight();
        PrinterJob printerJob = PrinterJob.createPrinterJob(defaultPrinter);
        if (printerJob != null && printerJob.showPrintDialog(mainStage)) {
            ArrayList<VBox> pages = new PrintView(palettes).getManifestViews();
            for (VBox vBox : pages) {
                vBox.setPrefSize(printableWidth, printableHeight);
                boolean printIsSuccessful = printerJob.printPage(vBox);
                if (!printIsSuccessful) {
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

    public void onHelpMenuAction() {
        // Create a custom Web view for showing help files
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("You are being helped!");
        alert.setContentText("I am helping you now!!");
        alert.showAndWait();
    }

    public void scrollNext() {
        int pagesInView = manifestListView.lookupAll("#manifestVBox").size();
        viewModel.setCurrentIndex(pagesInView + viewModel.getCurrentIndex());
        if (viewModel.getCurrentIndex() >= palettes.size()) {
            viewModel.setNextButtonDisabled(Boolean.TRUE);
        } else {
            viewModel.setNextButtonDisabled(Boolean.FALSE);
        }
        manifestListView.scrollTo(viewModel.getCurrentIndex());
        viewModel.setPreviousButtonDisabled(Boolean.FALSE);
    }

    public void scrollPrevious() {
        int pagesInView = manifestListView.lookupAll("#manifestVBox").size();
        viewModel.setCurrentIndex(viewModel.getCurrentIndex() - pagesInView);
        if (viewModel.getCurrentIndex() <= 0) {
            viewModel.setPreviousButtonDisabled(Boolean.TRUE);
        } else {
            viewModel.setPreviousButtonDisabled(Boolean.FALSE);
        }
        manifestListView.scrollTo(viewModel.getCurrentIndex());
        viewModel.setNextButtonDisabled(Boolean.FALSE);
    }

    public RootLayoutController() {
        viewModel = new ManifestViewModel();
        palettes = FXCollections.observableArrayList();
        FileInputStream stream = null;
        try {
            File file = new File(PREFERENCES);
            if(!file.exists()){
                file.createNewFile();
            }
            stream = new FileInputStream(PREFERENCES);
            XMLDecoder decoder = new XMLDecoder(stream);
            if(file.length() > 0){
                preferencesViewModel = (PreferencesViewModel)decoder.readObject();
            }else{
                preferencesViewModel = new PreferencesViewModel();
            }            
            decoder.close();
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch(IOException ex){
            ex.printStackTrace();
        } finally {
            try {
                if(stream != null){
                    stream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        if(preferencesViewModel == null){
            preferencesViewModel = new PreferencesViewModel();
        }
    }

    // </editor-fold>
}
