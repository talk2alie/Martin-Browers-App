/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestgenerator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;

public class HelpDialogController implements Initializable {

    @FXML // fx:id="treeView"
    private TreeView<String> treeView; // Value injected by FXMLLoader

    @FXML // fx:id="titleLabel"
    private Label titleLabel; // Value injected by FXMLLoader

    @FXML // fx:id="webView"
    private WebView webView; // Value injected by FXMLLoader

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //reeview root
        TreeItem<String> root = new TreeItem<>("Pallett Generator - Help Menu");
        //root nodes
        TreeItem<String> systemRequirements = new TreeItem<>("System Requirements");
        TreeItem<String> instructions = new TreeItem<>("Instructions");
        TreeItem<String> node3 = new TreeItem<>("Node 3");
        root.getChildren().addAll(systemRequirements, instructions, node3);
        
        
        treeView.setRoot(root);
        
    }

}
