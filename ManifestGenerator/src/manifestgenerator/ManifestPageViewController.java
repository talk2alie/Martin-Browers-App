/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestgenerator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import manifestgenerator.models.Cases;
import manifestgenerator.models.Palette;

/**
 * FXML Controller class
 *
 * @author talk2
 */
public class ManifestPageViewController implements Initializable
{
    private Palette palette;

    @FXML
    private VBox manifestPage;

    @FXML
    private GridPane headerGrid;

    @FXML
    private Label routeLabel;

    @FXML
    private Label stopLabel;

    @FXML
    private Label caseTrailerPositionLabel;

    @FXML
    private TableView<Cases> casesTable;

    @FXML
    private TableColumn<Cases, String> itemIdColumn;

    @FXML
    private TableColumn<Cases, String> itemDescriptionColumn;

    @FXML
    private TableColumn<Cases, Integer> casesColumn;

    @FXML
    private TableColumn<Cases, String> stopColumn;

    @FXML
    private Label cartTotalLabel;
    
    @FXML
    private Label routeLabelFooter;

    @FXML
    private Label stopLabelFooter;

    @FXML
    private Label caseTrailerPositionLabelFooter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Cases> cases = FXCollections
                .observableArrayList(palette.getSortedCaseList());
        routeLabel.setText(String.format("Route: %s", palette.getRouteInfo()));           
        stopLabel.setText(String.format("Stop: %s", palette.getStopInfo()));
        caseTrailerPositionLabel.setText(String.format("Trailer Position: %s", 
                palette.TRAILER_POSITION));
        
        casesTable.setItems(cases);
        itemIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("contentId"));
        itemDescriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("content"));
        casesColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity")); 
        stopColumn.setCellValueFactory(
                new PropertyValueFactory<>("stop"));
        
        cartTotalLabel.setText("CART TOTAL: " + palette.getCaseCount()); 
        
        routeLabelFooter.setText(String.format("Route: %s", palette.getRouteInfo()));           
        stopLabelFooter.setText(String.format("Stop: %s", palette.getStopInfo()));
        caseTrailerPositionLabelFooter.setText(String.format("Trailer Position: %s", 
                palette.TRAILER_POSITION));
    }
    
    public ManifestPageViewController(Palette palette) {
        this.palette = palette;
    }

}
