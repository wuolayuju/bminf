
package es.uam.eps.bmi.search.parsing;

import java.io.IOException;

/**
 * Interfaz para procesar un texto
 * @author Ari Handler - Adrián Lorenzo
 */
public interface TextParser {
    
    /**
     * 
     * Procesa un texto dado
     * 
     * @param text
     * @return texto procesado
     * @throws IOException 
     */
    public String parse(String text) throws IOException;
}
