/*
 * Author: Mohamed Alie Pussah, II
 *   Date: Dec 18, 2016
 */
package manifestgenerator.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a palette that can hold up to 21 cases of buns
 */
public class Palette
{

    /**
     * Gets a list of all the different kinds of cases in this palette
     */
    public final ArrayList<Cases> CASES;
    private int _caseCount; // Number of cases in palette
    private final int MAX_CASE_COUNT = 21;
    private int _referencePage;

    /**
     * Gets the position within the trailer where this palette will be placed
     */
    public final String TRAILER_POSITION;

    /**
     * Gets a unique identifier for this palette
     */
    public final String ID;

    /**
     * Create a new palette in the given trailer position
     *
     * @param trailerPosition - The position within the trailer where
     *                        this palette will be positioned
     */
    public Palette(String trailerPosition) {
        TRAILER_POSITION = trailerPosition;
        ID = trailerPosition + UUID.randomUUID().toString();
        CASES = new ArrayList<>(21);
    }

    /**
     * Adds the given cases to this palette
     *
     * @param cases - The cases to add to this palette
     * @return null if all the cases were added; otherwise, returns
     *         the cases that could not be added because the palette is full
     */
    public Cases addCases(Cases cases) {
        if (_caseCount >= MAX_CASE_COUNT) {
            return cases;
        }

        AtomicReference<Integer> index = new AtomicReference<>(-1);
        Cases entry = findCases(cases.getId(), index);
        if (entry == null) { // This palette has no entry for these cases            
            if ((_caseCount + cases.getQuantity()) <= MAX_CASE_COUNT) {
                cases.moveToPalette(ID);
                CASES.add(cases);
                _caseCount += cases.getQuantity();
                return null;
            }
            int acceptableCasesCount = MAX_CASE_COUNT - _caseCount;
            Cases acceptableCases = new Cases(cases.getRoute(),
                    cases.getStop(), acceptableCasesCount, cases.getContent(),
                    cases.getContentId(), ID);
            CASES.add(acceptableCases);
            _caseCount += acceptableCasesCount;
            cases.decreaseQuantityBy(acceptableCasesCount);
            return cases;
        }

        // These cases already exist in this palette
        int acceptableCasesCount = MAX_CASE_COUNT - _caseCount;
        if (acceptableCasesCount >= cases.getQuantity()) {
            entry.increaseQuantityBy(cases.getQuantity());
            CASES.set(index.get(), entry); // update the cases in the palette  
            _caseCount += cases.getQuantity();
            return null;
        }

        entry.increaseQuantityBy(acceptableCasesCount);
        CASES.set(index.get(), entry); // update the cases in the palette  
        _caseCount += acceptableCasesCount;
        cases.decreaseQuantityBy(acceptableCasesCount);
        return cases;
    }

    /**
     * Removes count of the given cases from this palette
     *
     * @param casesId - The unique identifier of the cases to remove
     * @param count   - The quantity of the cases to remove
     * @return null if the given cases were not found; otherwise, the
     *         removed case
     */
    public Cases removeCases(String casesId, int count) {
        if (_caseCount == 0) {
            return null;
        }

        AtomicReference<Integer> index = new AtomicReference<>(-1);
        Cases entry = findCases(casesId, index);
        if (entry == null) {
            return entry;
        }

        entry.decreaseQuantityBy(count);
        _caseCount -= count;
        if (entry.getQuantity() > 0) {
            CASES.set(index.get().intValue(), entry);
        }
        else {
            CASES.remove(index.get().intValue());
        }
        return new Cases(entry.getRoute(), entry.getStop(), count,
                entry.getContent(), entry.getContentId(), "");
    }

    /**
     * Searches this palette for cases with the given casesId
     *
     * @param casesId - The unique identifier of the cases to find
     * @param index   - The starting position within the palette (counting from
     *                0, top to bottom), where these cases can be found
     * @return
     */
    public Cases findCases(String casesId, AtomicReference<Integer> index) {
        for (int i = 0; i < CASES.size(); ++i) {
            if (CASES.get(i).getId().equals(casesId)) {
                index.set(i);
                return CASES.get(i);
            }
        }
        return null;
    }

    /**
     * Gets the page from the CSV file where data for this palette
     * was collected
     *
     * @return the page, in the CSV file, from which data for this
     *         palette was collected
     */
    public int getReferencePage() {
        return _referencePage;
    }

    /**
     * Sets the page in the CSV file from which data for this palette
     * was collected
     *
     * @param referencePage - The page in the CSV file where data for this
     *                      palette was collected
     */
    public void setReferencePage(int referencePage) {
        _referencePage = referencePage;
    }

    /**
     * Gets the total number of cases in this palette
     *
     * @return the number of cases in this palette
     */
    public int getCaseCount() {
        return _caseCount;
    }

    /**
     * Gets the ID of the last cases in this palette
     *
     * @return
     */
    public String getLastCaseId() {
        if (CASES.isEmpty()) {
            return null;
        }

        return CASES.get(CASES.size() - 1).getId();
    }

    /**
     * Gets a sorted list of all cases in this palette
     *
     * @return the sorted list of cases
     */
    public ArrayList<Cases> getSortedCaseList() {
        Collections.sort(CASES);
        return CASES;
    }

    /**
     * Gets this palette's manifest in the form of a CSV content
     *
     * @return the CSV of this palette's manifest
     */
    public String getManifestAsCsv() {
        StringBuilder builder = new StringBuilder();

        builder.append(",STOP,CASES,DESCRIPTION,WRIN,TRAILER,ROUTE\n");
        getSortedCaseList().forEach((cases) -> {
            builder.append(",")
                    .append(cases.getStop())
                    .append(",")
                    .append(cases.getQuantity())
                    .append(",")
                    .append(cases.getContent())
                    .append(",")
                    .append(cases.getContentId())
                    .append(",")
                    .append(TRAILER_POSITION)
                    .append(",")
                    .append(cases.getRoute())
                    .append("\n");
        });
        builder.append("CART TOTAL:,,").append(_caseCount).append("\n");

        return builder.toString();
    }
}
