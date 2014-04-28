package es.uam.eps.bmi.recom.neighborhood;

import es.uam.eps.bmi.recom.exceptions.GenericRecommendationException;
import java.util.List;

/**
 * Interfaz que define las operaciones que debe implementar un calculador
 * de vecindario para usuarios.
 * @author Ari Handler - Adrián Lorenzo
 */
public interface UserNeighborhood {
    
	/**
	 * Obtiene los usuarios más próximos a un determinado usuario.
	 * @param userID identificador de usuario.
	 * @return lista de identificadores de los usuarios más próximos a al usuario.
	 * @throws GenericRecommendationException si el usuario no existe.
	 */
    List<Long> getUserNeighborhood(long userID) throws GenericRecommendationException;
}
