/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Dec 23, 2016
 */

package manifestgenerator.models;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 *
 */
public class PaletteListViewCell extends ListCell<Palette> {
    
    @FXML
    private VBox manifestVBox;
    
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
    private TableColumn<Cases, Integer> casesColumn;

    @FXML
    private TableColumn<Cases, String> itemDescriptionColumn;

    @FXML
    private Label cartTotalLabel;
    
    private FXMLLoader loader;
    
    @Override 
    protected void updateItem(Palette palette, boolean empty) {
        super.updateItem(palette, empty);
        
        if(empty || palette == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        
        if(loader == null) {
            loader = new FXMLLoader(getClass()
                    .getResource("/manifestgenerator/views/PaletteListCell.fxml"));
            loader.setController(this);
            
            try {
                loader.load();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        ObservableList<Cases> cases = FXCollections
                .observableArrayList(palette.getSortedCaseList());
        routeLabel.setText(String.format("Route: %s", cases.get(0).getRoute()));
        stopLabel.setText(String.format("Stop: %s", cases.get(0).getStop()));
        caseTrailerPositionLabel.setText(String.format("Trailer Position: %s", 
                palette.TRAILER_POSITION));
        
        casesTable.setItems(cases);
        itemIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("contentId"));
        itemDescriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("content"));
        casesColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));        
        
        cartTotalLabel.setText("CART TOTAL: " + palette.getCaseCount()); 
        
        setText(null);
        setGraphic(manifestVBox);
    }
}
