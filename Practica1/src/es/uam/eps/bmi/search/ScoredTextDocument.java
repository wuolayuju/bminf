/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search;

/**
 *
 * @author e185318
 */
public class ScoredTextDocument implements Comparable{

    private TextDocument document;
    private double score;

    public ScoredTextDocument(TextDocument document, double score) {
        this.document = document;
        this.score = score;
    }

    /**
     * Get the value of score
     *
     * @return the value of score
     */
    public double getScore() {
        return score;
    }
    
    public String getDocumentId() {
        return document.getId();
    }
    
    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
