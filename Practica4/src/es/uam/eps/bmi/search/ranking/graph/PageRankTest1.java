/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph;

import es.uam.eps.bmi.search.ScoredTextDocument;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Ari Handler y Adrián Lorenzo
 */
public class PageRankTest1 {

    /**
     * USAGE: java es.uam.eps.bmi.search.ranking.graph.PageRankTest1 -links LINKS_FILE<br>
     * Ejecuta PageRank sobre el grafo definido en el fichero suministrado.
     * @param args -links LINKS_FILE
     */
    public static void main(String[] args) {
        
        String usage = "USAGE: java es.uam.eps.bmi.search.ranking.graph.PageRankTest1"
                 + " -links LINKS_FILE\n"
                 + "Ejecuta PageRank sobre el grafo definido en el fichero suministrado.";
        
        String linksPath = "";
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-links")==0) {
                linksPath = args[i+1];
                i++;
            }
        }
        
        if (linksPath.isEmpty()) {
            System.err.println("No links file provided.");
            System.out.println(usage);
            System.exit(0);
        }
        
        try {
            PageRank pr = new PageRank();
            int numNodes = pr.loadFromfile(linksPath);
            pr.calculatePageRank(0.01, 0.85, true);
            
            List<ScoredTextDocument> top = pr.getTopNPages(numNodes);
            System.out.println("Nodes of the graph:");
            int pos = 1;
            for (ScoredTextDocument doc : top) {
                System.out.println(pos++ + ". " + doc.getDocumentId() + " = " + doc.getScore());
            }
            
        } catch (IOException ex) {
            Logger.getLogger(PageRankTest1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
