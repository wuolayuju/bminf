/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.searching;

import es.uam.eps.bmi.search.ScoredTextDocument;
import es.uam.eps.bmi.search.indexing.Index;
import java.util.List;

/**
 *
 * @author e185318
 */
public interface Searcher {
    
    public void build(Index index);
    
    public List<ScoredTextDocument> search(String query);

}
