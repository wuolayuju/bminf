package es.uam.eps.bmi.social.graph.exceptions;

/**
 * Excepción genérica de redes sociales.
 * @author Ari Handler - Adrián Lorenzo
 */
public class SocialException extends Exception {

    /**
     * Creates a new instance of <code>SocialException</code> without detail
     * message.
     */
    public SocialException() {
    }

    /**
     * Constructs an instance of <code>SocialException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public SocialException(String msg) {
        super(msg);
    }
}
