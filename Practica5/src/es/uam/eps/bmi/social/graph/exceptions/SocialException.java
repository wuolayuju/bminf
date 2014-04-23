/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.social.graph.exceptions;

/**
 *
 * @author chus
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
