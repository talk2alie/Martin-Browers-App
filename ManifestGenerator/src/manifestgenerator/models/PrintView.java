/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Dec 26, 2016
 */
package manifestgenerator.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import manifestgenerator.ManifestPageViewController;

/**
 *
 */
public class PrintView
{
    private final ArrayList<Palette> palettes;
    public PrintView(List<Palette> palettes) {
        this.palettes = new ArrayList<>(palettes);
    }

    public ArrayList<VBox> getManifestViews() throws IOException {
        ArrayList<VBox> views = new ArrayList<>();

        palettes.forEach(palette -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/manifestgenerator/views/ManifestPageView.fxml"));
                loader.setController(new ManifestPageViewController(palette));

                loader.load();
                VBox vBox = loader.getRoot();
                views.add(vBox);
            }
            catch (IOException ex) {
                // Log error                
            }

        });
        return views;
    }
}
