package es.uam.eps.bmi.recom.exceptions;

/**
 * Excepción genérica del sistema de recomendación.
 * @author Ari Handler - Adrián Lorenzo
 */
public class GenericRecommendationException extends Exception {

    /**
     * Creates a new instance of <code>GenericRecommendationException</code>
     * without detail message.
     */
    public GenericRecommendationException() {
    }

    /**
     * Constructs an instance of <code>GenericRecommendationException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GenericRecommendationException(String msg) {
        super(msg);
    }
}
