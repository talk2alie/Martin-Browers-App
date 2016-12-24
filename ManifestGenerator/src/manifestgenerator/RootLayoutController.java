/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestgenerator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import manifestgenerator.models.ManifestViewModel;
import manifestgenerator.models.Palette;
import manifestgenerator.models.PaletteListViewCell;
import manifestgenerator.models.PaletteManager;

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
    private GridPane headerGrid;

    @FXML
    private Label titleLabel;

    @FXML
    private Label subTitleLabel;

    @FXML
    private TextField browseTextField;

    @FXML
    private Button browseButton;

    @FXML
    private GridPane mainGrid;

    @FXML
    private GridPane previewGrid;

    @FXML
    private Button previewButton;

    @FXML
    private GridPane contentGrid;

    @FXML
    private GridPane sliderGrid;

    @FXML
    private Button previousButton;

    @FXML
    private ScrollPane sliderScrollPane;

    @FXML
    private HBox manifestHBox;

    @FXML
    private Button nextButton;

    @FXML
    private HBox manifestInfoHBox;

    @FXML
    private Label originalFileLabel;

    @FXML
    private Label foundOnPageLabel;

    @FXML
    private Label trailerPositionLabel;

    @FXML
    private GridPane footerGrid;

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
    }

    @FXML
    void onPreviewManifestsAction(ActionEvent event) {
        System.out.println("Previewing Manifests...");
        
        try {
            PaletteManager paletteManager = 
                    new PaletteManager(viewModel.getOriginalFilePath());
            palettes.addAll(paletteManager.PALETTES);
            if(paletteManager.PALETTES.size() > 0) {
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
    void onPrintAction(ActionEvent event) {
        System.out.println("Printing Manifests...");
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Helpers">
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
