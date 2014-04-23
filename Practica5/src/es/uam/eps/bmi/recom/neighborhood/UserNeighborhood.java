/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.neighborhood;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import java.util.List;

/**
 *
 * @author uam
 */
public interface UserNeighborhood {
    
    List<Long> getUserNeighborhood(long userID) throws GenericRecommendationException;
}
