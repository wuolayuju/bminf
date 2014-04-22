/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.exceptions;

/**
 *
 * @author chus
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
