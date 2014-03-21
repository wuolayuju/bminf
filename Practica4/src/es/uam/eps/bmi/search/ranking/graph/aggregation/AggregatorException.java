/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph.aggregation;

/**
 *
 * @author uam
 */
public class AggregatorException extends Exception {

    /**
     * Creates a new instance of <code>AggregatorException</code> without detail
     * message.
     */
    public AggregatorException() {
    }

    /**
     * Constructs an instance of <code>AggregatorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AggregatorException(String msg) {
        super(msg);
    }
}
