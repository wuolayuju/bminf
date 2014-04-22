/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.similarity;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;

/**
 *
 * @author uam
 */
public interface VectorSimilarity {
    double vectorSimilarity(long userID, long itemID) throws GenericRecommendationException;
}
