/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph.aggregation;

import es.uam.eps.bmi.search.indexing.AdvancedIndex;
import es.uam.eps.bmi.search.ranking.graph.PageRank;
import es.uam.eps.bmi.search.searching.LiteralMatchingSearcher;
import es.uam.eps.bmi.search.searching.ProximalSearcher;
import es.uam.eps.bmi.search.searching.TFIDFSearcher;

/**
 *
 * @author uam
 */
public class AggregatorTest {
    
    public static void main(String[] args) {
        String indexPath = "";
        String linksFile = "";
        
        String usage = "USAGE: java es.uam.eps.bmi.search.ranking.graph.aggregation.AggregatorTest"
                 + " -index INDEX_PATH -links LINKS_FILE\n"
                 + "Compara resultados de varios buscadores dado un indice y el fichero de enlaces de dicha coleccion.";
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-index")==0) {
                indexPath = args[i+1];
                i++;
            }
            if(args[i].compareTo("-links")==0) {
                linksFile = args[i+1];
                i++;
            }
        }
        
        if (indexPath.isEmpty()) {
            System.err.println("No index directory provided.");
            System.out.println(usage);
            System.exit(0);
        }
        
        if (linksFile.isEmpty()) {
            System.err.println("No links file provided.");
            System.out.println(usage);
            System.exit(0);
        }
        
        if (!indexPath
                .substring(indexPath.lastIndexOf("/"), indexPath.length() - 1)
                .equalsIgnoreCase("advanced"))
        {
            System.err.println("The provided index must be Advanced.");
            System.exit(0);
        }
        
        // Indice
        AdvancedIndex index = new AdvancedIndex();
        index.load(indexPath);
        
        // Buscadores
        TFIDFSearcher tfSearcher = new TFIDFSearcher();
        LiteralMatchingSearcher literalSearcher = new LiteralMatchingSearcher();
        ProximalSearcher proxSearcher = new ProximalSearcher();
        PageRank pageRank = new PageRank();
        
    }
    
}
