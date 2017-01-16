/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Dec 23, 2016
 */
package manifestgenerator.models;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
public class PaletteListViewCell extends ListCell<Palette>
{

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
    private TableColumn<Cases, String> stopColumn;

    @FXML
    private Label cartTotalLabel;

    @FXML
    private Label routeLabelFooterPreview;

    @FXML
    private Label stopLabelFooterPreview;

    @FXML
    private Label caseTrailerPositionLabelFooterPreview;

    private FXMLLoader loader;

    private void setCasesTableHeight(int rowCount) {
        final int FIRST_ROW_HEIGHT = 70;
        final int INCREMENT_PER_ROW = 25;
        casesTable.setPrefHeight(
                FIRST_ROW_HEIGHT + ((rowCount - 1) * INCREMENT_PER_ROW));
    }

    @Override
    protected void updateItem(Palette palette, boolean empty) {
        super.updateItem(palette, empty);

        if (empty || palette == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        if (loader == null) {
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
        routeLabel.setText(String.format("Route: %s", palette.getRouteInfo()));
        stopLabel.setText(String.format("Stop: %s", palette.getStopInfo()));
        caseTrailerPositionLabel.setText(String.format("Trailer Position: %s",
                palette.TRAILER_POSITION));

        routeLabelFooterPreview.setText(String.format("Route: %s", palette.getRouteInfo()));
        stopLabelFooterPreview.setText(String.format("Stop: %s", palette.getStopInfo()));
        caseTrailerPositionLabelFooterPreview.setText(String.format("Trailer Position: %s",
                palette.TRAILER_POSITION));

        setCasesTableHeight(cases.size());
        casesTable.setItems(cases);
        casesTable.setEditable(false);
        casesTable.setFocusTraversable(false);
        casesTable.setId("casesTable");

        // Prevent user from reordering columns in the table
        casesTable.getColumns().addListener(new ListChangeListener()
        {
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced()) {
                    casesTable.getColumns().clear();
                    casesTable.getColumns().addAll(itemIdColumn,
                             itemDescriptionColumn,
                             casesColumn,
                             stopColumn);
                }
            }
        });
        
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("contentId"));
        itemIdColumn.setEditable(false);
        itemIdColumn.setResizable(false);
        itemIdColumn.setSortable(false);

        itemDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        itemDescriptionColumn.setEditable(false);
        itemDescriptionColumn.setResizable(false);
        itemDescriptionColumn.setSortable(false);

        casesColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        casesColumn.setEditable(false);
        casesColumn.setResizable(false);
        casesColumn.setSortable(false);

        stopColumn.setCellValueFactory(new PropertyValueFactory<>("stop"));
        stopColumn.setEditable(false);
        stopColumn.setResizable(false);
        stopColumn.setSortable(false);

        cartTotalLabel.setText("CART TOTAL: " + palette.getCaseCount());

        setText(null);
        setGraphic(manifestVBox);
    }
}
