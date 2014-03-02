/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.indexing;

import es.uam.eps.bmi.search.parsing.HTMLAdvancedParser;
import es.uam.eps.bmi.search.parsing.HTMLSimpleParser;
import es.uam.eps.bmi.search.parsing.HTMLStemParser;
import es.uam.eps.bmi.search.parsing.HTMLStopwordsParser;
import java.io.File;

/**
 *
 * @author chus
 */
public class IndexBuilder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String usage = "java es.uam.eps.bmi.indexing.IndexBuilder"
                 + " [-index INDEX_PATH] [-docs DOCS_PATH] [-stopwords STOP_PATH] [-stemmer STEMMER]\n\n"
                 + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                 + "in INDEX_PATH, bringing it then into RAM.";
        
        String indexPath = "index";
        String docsPath = "docs";
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-index")==0) {
                indexPath = args[i+1];
                i++;
            }
            if(args[i].compareTo("-docs")==0) {
                docsPath = args[i+1];
                i++;
            }
        }
        
        String stopPath = indexPath + File.separator + "stopwords.txt";
        String stemmerType = HTMLStemParser.ENGLISH_STEMMER;
        
        String basicPath = indexPath + "basic";
        String stopwordPath = indexPath + "stopword";
        String stemPath = indexPath + "stem";
        String advancedPath = indexPath + "advanced";
        
        BasicIndex basicIndex = new BasicIndex();
        StopwordIndex stopIndex = new StopwordIndex();
        StemIndex stemIndex = new StemIndex();
        AdvancedIndex advIndex = new AdvancedIndex();
        
        basicIndex.build(docsPath, basicPath, new HTMLSimpleParser());
        stopIndex.build(docsPath, stopwordPath, new HTMLStopwordsParser(stopPath));
        stemIndex.build(docsPath, stemPath, new HTMLStemParser(stemmerType));
        advIndex.build(docsPath, advancedPath, new HTMLAdvancedParser(stopPath, stemmerType));
    }
    
}
