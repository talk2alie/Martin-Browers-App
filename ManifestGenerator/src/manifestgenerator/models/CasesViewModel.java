/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Dec 23, 2016
 */

package manifestgenerator.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 */
public class CasesViewModel {
    private final StringProperty route;
    private final StringProperty stop;
    private final IntegerProperty quantity;
    private final StringProperty content; // The description
    private final StringProperty contentId; // The WRIN
    
    public CasesViewModel(Cases cases) {
        route = new SimpleStringProperty(cases.getRoute());
        stop = new SimpleStringProperty(cases.getStop());
        quantity = new SimpleIntegerProperty(cases.getQuantity());
        content = new SimpleStringProperty(cases.getContent());
        contentId = new SimpleStringProperty(cases.getContentId());
    }

    /**
     * @return the _route
     */
    public StringProperty routeProperty() {
        return route;
    }
    
    public String getRoute() {
        return route.get();
    }

    /**
     * @param _route the _route to set
     */
    public void setRoute(String route) {
        this.route.set(route);
    }

    /**
     * @return the _stop
     */
    public StringProperty stopProperty() {
        return stop;
    }
    
    public String getStop() {
        return stop.get();
    }

    /**
     * @param _stop the _stop to set
     */
    public void setStop(String stop) {
        this.stop.set(stop);
    }

    /**
     * @return the _quantity
     */
    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public int getQuantity() {
        return quantity.get();
    }
    
    /**
     * @param _quantity the _quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    /**
     * @return the _content
     */
    public StringProperty contentProperty() {
        return content;
    }
    
    public String getContent() {
        return content.get();
    }

    /**
     * @param _content the _content to set
     */
    public void setContent(String content) {
        this.content.set(content);
    }

    /**
     * @return the _contentId
     */
    public StringProperty contentIdProperty() {
        return contentId;
    }

    public String getContentId() {
        return contentId.get();
    }
    
    /**
     * @param _contentId the _contentId to set
     */
    public void setContentId(String contentId) {
        this.contentId.set(contentId);
    }   
    
}
