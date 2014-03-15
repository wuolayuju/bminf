
package es.uam.eps.bmi.search.indexing;

/**
 *
 * Representa un índice que sí hace filtrado de stopwords, 
 * pero no hace stemming de términos. 
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class StopwordIndex extends BasicIndex {

    /**
     * Construye este tipo de índice.
     */
    public StopwordIndex() {
        super();
    }
}
