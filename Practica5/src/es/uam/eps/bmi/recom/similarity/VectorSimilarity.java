/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.similarity;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;

/**
 * Define la interfaz que debe tener una función de similitud entre un usuario
 * y un ítem.
 * @author Ari Handler - Adrián Lorenzo
 */
public interface VectorSimilarity {
	/**
	 * Calcula la similitud entre un determinado usuario y un ítem.
	 * @param userID identificador del usuario
	 * @param itemID identificador del item.
	 * @return valor de similitud entre el usuario y el ítem.
	 * @throws GenericRecommendationException si no existe el usuario.
	 */
    double vectorSimilarity(long userID, long itemID) throws GenericRecommendationException;
}
