
package es.uam.eps.bmi.search.parsing;

import java.io.IOException;

/**
 * Interfaz para procesar y limpiar un texto
 * @author Ari Handler - Adrián Lorenzo
 */
public interface TextParser {
    
    /**
     * 
     * Procesa un texto dado, devolviendo un {@link String} con el texto
     * limpiado.
     * 
     * @param text Texto a procesar.
     * @return Texto procesado. 
     * @throws IOException En caso de algún error en el parseo.
     */
    public String parse(String text) throws IOException;
}
