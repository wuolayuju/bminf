/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.indexing;

import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.parsing.TextParser;
import java.util.List;

/**
 *
 * @author e185318
 */
public interface Index {
    
    public void build(String inputCollectionPath, String outputIndexPath, TextParser textParser);
    
    public void load(String indexPath);
    
    public List<String> getDocumentIds();

    public TextDocument getDocument(String documentId);
    
    public List<String> getTerms();
    
    public List<Posting> getTermPostings (String term);
    
    public List<Posting> getDocumentPostings (String documentId);
}
