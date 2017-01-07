/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manifestgenerator;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author talk2
 */
public class ManifestGenerator extends Application
{

    private RootLayoutController rootController;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/manifestgenerator/views/RootLayout.fxml"));
        Parent root = loader.load();

        rootController = loader.getController();
        rootController.setMainStage(stage);

        stage.setTitle("Manifest Generator");
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setMaximized(Boolean.TRUE);
        Scene scene = new Scene(root);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F1) {
                rootController.onHelpMenuAction();
            }
        });
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.P) {
                try {
                    try {
                        rootController.onPrint();
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                catch (IOException ex) {
                    // TODO: Log error and report it
                }
            }
        });        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.W) {
                rootController.onExportToWord();
            }
        });
        scene.getStylesheets().add("/manifestgenerator/styles/styles.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
