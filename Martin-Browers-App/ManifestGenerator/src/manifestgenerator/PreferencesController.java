/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestgenerator;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import manifestgenerator.models.PaletteManager;
import manifestgenerator.models.PreferencesViewModel;

/**
 * FXML Controller class
 *
 * @author MPussah
 */
public class PreferencesController implements Initializable {

    private PreferencesViewModel viewModel;
    private Stage mainStage;

    @FXML
    private TextField outputDirectoryTextField;

    @FXML
    private TextField inputDirectoryTextField;

    @FXML
    void onInputBrowse(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Default Input Directory");
        File directory = directoryChooser.showDialog(mainStage);
        if (directory != null) {
            viewModel.setDefaultInputDirectory(directory.getPath());
        }
    }

    @FXML
    void onOutputBrowse(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Default Output Directory");
        File directory = directoryChooser.showDialog(mainStage);
        if (directory != null) {
            viewModel.setDefaultOutputDirectory(directory.getPath());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inputDirectoryTextField.textProperty()
                .bind(viewModel.defaultInputDirectoryProperty());
        outputDirectoryTextField.textProperty()
                .bind(viewModel.defaultOutputDirectoryProperty());
    }

    public void setStage(Stage stage) {
        mainStage = stage;
    }

    public void savePreferences(String file) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            XMLEncoder encoder = new XMLEncoder(stream);
            encoder.writeObject(viewModel);
            encoder.close();
        } catch (FileNotFoundException ex) {
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
    }
    
    public void setViewModel(String input, String output) {
        viewModel.setDefaultInputDirectory(input);
        viewModel.setDefaultOutputDirectory(output);
    }
    
    public PreferencesViewModel getViewModel() {
        return viewModel;
    }
    
    public PreferencesController() {
        viewModel = new PreferencesViewModel();
    }
}
