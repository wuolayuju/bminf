/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.indexing;

import java.util.List;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author chus
 */
public class StemIndex extends BasicIndex {
    
    public StemIndex() {
        super();
    }
    
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
