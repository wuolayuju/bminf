
package es.uam.eps.bmi.search.indexing;

import java.util.List;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * Representa un índice que sí hace filtrado de stopwords 
 * y stemming de términos.
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class AdvancedIndex extends BasicIndex {

    public AdvancedIndex() {
        super();
    }
    /**
     * Retorna la lista de postings correspondiente al término
     * (internamente el término se stemmiza para buscarlo en el índice)
     * 
     * @param term
     * @return 
     */
    @Override
    public List<Posting> getTermPostings(String term) {
        if (indexMap == null)
            return null;
        
        SnowballStemmer stemmer = new englishStemmer();
        
        stemmer.setCurrent(term);
        stemmer.stem();
        String stemmedTerm = stemmer.getCurrent();
        
        return indexMap.get(stemmedTerm);
    }
}
