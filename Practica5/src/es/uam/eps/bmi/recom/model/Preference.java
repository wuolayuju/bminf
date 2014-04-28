package es.uam.eps.bmi.recom.model;

/**
 * Interfaz que modeliza lo que debe ser una preferencia de un usuario hacia
 * un ítem en un determinado grado.
 * @author Ari Handler - Adrián Lorenzo
 */
public interface Preference {
    
	/**
	 * Obtiene el identificador del usuario de la preferencia.
	 * @return identificador de usuario.
	 */
    long getUserID();
    
	/**
	 * Obtiene el identificador del ítem de la preferencia.
	 * @return identificador del ítem.
	 */
    long getItemID();
    
	/**
	 * Obtiene el valor de la preferencia.
	 * @return valor decimal.
	 */
    float getValue();
    
	/**
	 * Determina el valor de la preferencia.
	 * @param value nuevo valor de preferencia.
	 */
    void setValue(float value);
}
