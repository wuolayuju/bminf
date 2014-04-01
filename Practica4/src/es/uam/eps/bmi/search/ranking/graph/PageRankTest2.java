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
 * @author uam
 */
public class PageRankTest2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String usage = "USAGE: java es.uam.eps.bmi.search.ranking.graph.PageRankTest2"
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
            pr.calculatePageRank(0.0, 0.85, false);
            List<ScoredTextDocument> top = pr.getTopNPages(10);
            System.out.println(numNodes + " total nodes.");
            System.out.println("Top 10 nodes:");
            int pos = 1;
            for (ScoredTextDocument doc : top) {
                System.out.println(pos++ + ". " + doc.getDocumentId() + " = " + doc.getScore());
            }
        } catch (IOException ex) {
            Logger.getLogger(PageRankTest1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
