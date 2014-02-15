
package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.Index;
import java.util.List;

/**
 * Interfaz para la búsqueda en un índice
 * @author Ari Handler - Adrián Lorenzo
 */
public interface Searcher {
    
    /**
     * 
     * Crea el buscador a partir del índice pasado como argumento de entrada
     * 
     * @param index 
     */
    public void build(Index index);
    
    /**
     * 
     * Devuelve un ranking (ordenado por score decreciente) de documentos,
     * resultates de ejecutar una consta dada sobre el índice del buscador
     * 
     * @param query
     * @return 
     */
    public List<ScoredTextDocument> search(String query);

}
