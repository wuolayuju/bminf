/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.indexing;

import java.util.List;

/**
 *
 * @author e185318
 */
public class Posting {
    
    private String documentId;
    private int termFrequency;
    private List<Long> termPositions;

    public Posting(String documentId, int termFrequency, List<Long> termPositions) {
        this.documentId = documentId;
        this.termFrequency = termFrequency;
        this.termPositions = termPositions;
    }

    /**
     * Get the value of termPositions
     *
     * @return the value of termPositions
     */
    public List<Long> getTermPositions() {
        return termPositions;
    }

    /**
     * Get the value of termFrequency
     *
     * @return the value of termFrequency
     */
    public int getTermFrequency() {
        return termFrequency;
    }

    /**
     * Get the value of documentId
     *
     * @return the value of documentId
     */
    public String getDocumentId() {
        return documentId;
    }

}
