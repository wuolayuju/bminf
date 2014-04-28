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
public interface UserSimilarity {
    
    double userSimilarity(long userID1, long userID2) throws GenericRecommendationException;
    double userSimilarity(long userID1, long userID2, double train) throws GenericRecommendationException;
}
