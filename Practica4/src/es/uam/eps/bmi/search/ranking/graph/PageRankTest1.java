/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.ranking.graph;

import java.io.IOException;
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
            PageRank pr = new PageRank(linksPath);
        } catch (IOException ex) {
            Logger.getLogger(PageRankTest1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
