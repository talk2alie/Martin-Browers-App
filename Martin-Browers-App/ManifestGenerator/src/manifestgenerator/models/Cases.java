/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Dec 18, 2016
 */
package manifestgenerator.models;

/**
 * Represents the cases of a particular type of bun within a palette
 */
public class Cases implements Comparable<Cases>
{

    private String _route;
    private String _stop;
    private int _quantity;
    private String _content; // The description
    private String _contentId; // The WRIN
    private String _paletteId;

    /**
     * Creates a new instance using the given information
     *
     * @param route     - The route through which these cases will be delivered
     * @param stop      - The stop where theses cases will be delivered
     * @param quantity  - The quantity of these cases that will be delivered
     * @param content   - The description of the bun contained in theses cases
     * @param contentId - The WRIN of the bun contained in the theses cases
     * @param paletteId - The unique identifier of the palette that contains
     *                  theses cases
     */
    public Cases(String route, String stop, int quantity,
            String content, String contentId, String paletteId) {
        _route = route;
        _stop = stop;
        _quantity = quantity;
        _content = content;
        _contentId = contentId;
        _paletteId = paletteId;
    }

    /**
     * Creates a new instance that will be populated with data later
     */
    public Cases() {
        this("", "", 0, "", "", "");
    }

    @Override
    public int compareTo(Cases other) {
        return getId().compareTo(other.getId());
    }

    /**
     * @return the _route
     */
    public String getRoute() {
        return _route;
    }

    /**
     * @param route the _route to set
     */
    public void setRoute(String route) {
        _route = route;
    }

    /**
     * @return the _stop
     */
    public String getStop() {
        return _stop;
    }

    /**
     * @param stop the _stop to set
     */
    public void setStop(String stop) {
        _stop = stop;
    }

    /**
     * @return the _quantity
     */
    public int getQuantity() {
        return _quantity;
    }

    /**
     * @param amount the amount by which to increase the quantity
     */
    public void increaseQuantityBy(int amount) {
        _quantity += Math.abs(amount);
    }

    /**
     * @param amount the amount by which to decrease the quantity
     */
    public void decreaseQuantityBy(int amount) {
        _quantity -= Math.abs(amount);
    }

    /**
     * @return the _content
     */
    public String getContent() {
        return _content;
    }

    /**
     * @param content the _content to set
     */
    public void setContent(String content) {
        _content = content;
    }

    /**
     * @return the _contentId
     */
    public String getContentId() {
        return _contentId;
    }

    /**
     * @param contentId the _contentId to set
     */
    public void setContentId(String contentId) {
        _contentId = contentId;
    }

    /**
     * @return the _id
     */
    public String getId() {
        return _stop + _contentId;
    }

    /**
     * @return the paletteId
     */
    public String getPaletteId() {
        return _paletteId;
    }

    /**
     * @param paletteId the _paletteId to set
     */
    public void moveToPalette(String paletteId) {
        _paletteId = paletteId;
    }
}
