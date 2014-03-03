/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.indexing;

/**
 *
 * Representa un índice que sí hace filtrado de stopwords, 
 * pero no hace stemming de términos. 
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class StopwordIndex extends BasicIndex {

    public StopwordIndex() {
        super();
    }
}
