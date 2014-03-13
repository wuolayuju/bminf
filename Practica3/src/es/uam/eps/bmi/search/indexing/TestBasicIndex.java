/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.indexing;

import es.uam.eps.bmi.search.TextDocument;
import es.uam.eps.bmi.search.parsing.HTMLSimpleParser;
import java.io.IOException;
import java.util.List;

/**
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class TestBasicIndex {
     public static void  main(String[] args) throws IOException {
        String usage = "java es.uam.eps.bmi.search.TestIndex"
                 + " [-index INDEX_PATH] [-docs DOCS_PATH] \n\n"
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
        
        BasicIndex index = new BasicIndex();
        
        index.build(docsPath, indexPath, new HTMLSimpleParser());
        
        index.load(indexPath);
        
        List<String> terms = index.getTerms();
        
        List<Posting> termPostings = index.getTermPostings(terms.get(terms.indexOf("family")));
        
        Posting post1 = termPostings.get(0);
        
        TextDocument doc = index.getDocument(post1.getDocumentId());
        
        System.out.println("Family => docId = "+doc.getId()+"("+doc.getName()+") , freq = "+post1.getTermFrequency());
     }
}
