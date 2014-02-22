
package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.Index;
import java.util.List;

/**
 * Interfaz para la búsqueda en un índice creado por previamente por alguna
 * implementación de {@link Index}.
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public interface Searcher {
    
    /**
     * Crea el buscador a partir del índice pasado como argumento de entrada.<p>
     * 
     * Es necesario haber creado el índice previamente de la llamada a este método mediante {@link Index#load}.
     * 
     * @param index Indice sobre el que realizar búsquedas.
     */
    public void build(Index index);
    
    /**
     * Devuelve un ranking (ordenado por score decreciente) de documentos,
     * resultantes de ejecutar una consulta dada sobre el índice del buscador.
     * 
     * @param query Consulta que se desea realizar.
     * @return Lista de documentos puntuados en orden decreciente de puntuación.
     * 
     * @see ScoredTextDocument
     */
    public List<ScoredTextDocument> search(String query);

}
