/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uam
 */
public class PageRankTest1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String linksPath = args[0];
        try {
            PageRank pr = new PageRank();
            pr.loadFromfile(linksPath);
            pr.calculatePageRank(0.01, 0.85, true);
            
            List<String> top = pr.getTopNPages(10);
            System.out.println("TOP 10 Paginas:");
            int pos = 1;
            for (String doc : top) {
                System.out.println(pos++ + ". " + doc + " = " + pr.getScoreOf(doc));
            }
            
        } catch (IOException ex) {
            Logger.getLogger(PageRankTest1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
