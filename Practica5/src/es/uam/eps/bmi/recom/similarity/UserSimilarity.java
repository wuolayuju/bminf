/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.recom.similarity;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;

/**
 * Interfaz que modela cualquier función de similitud entre usuarios para
 * la construcción de sistemas de recomendación.<br>
 * Se define una sola función común que es la que calcula la similitud entre dos
 * usuarios, además de una copia de esta en la que se define el porcentaje de
 * ratings que se deben utilizar para calcularla con el fin de poder implementar
 * algoritmos de métricas.
 * @author Ari Handler - Adrián Lorenzo
 */
public interface UserSimilarity {
    
	/**
	 * Calcula la similitud entre dos usuarios, representado como un número decimal.
	 * @param userID1 identificador del primer usuario.
	 * @param userID2 identificador del segundo usuario.
	 * @return valor de similitud entre los usuarios
	 * @throws GenericRecommendationException si alguno de los dos usuarios no existe.
	 */
    double userSimilarity(long userID1, long userID2) throws GenericRecommendationException;
	
	/**
	 * Calcula la similitud entre dos usuarios, representado como un número decimal,
	 * haciendo uso sólo de un cierto porcentaje de datos de entrenamiento.
	 * @param userID1 identificador del primer usuario.
	 * @param userID2 identificador del segundo usuario.
	 * @param train porcentaje de datos de entrenamiento para calcular la similitud.
	 * @return valor de similitud entre los usuarios.
	 * @throws GenericRecommendationException si alguno de los dos usuarios no existe.
	 */
    double userSimilarity(long userID1, long userID2, double train) throws GenericRecommendationException;
}
