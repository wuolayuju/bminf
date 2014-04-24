/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.social.test;

import es.uam.eps.bmi.social.graph.SocialGraph;
import es.uam.eps.bmi.social.graph.exceptions.SocialException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chus
 */
public class GraphTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dataFile = null;
        
        for(int i=0;i<args.length;i++) {
            if(args[i].compareTo("-data")==0) {
                dataFile = args[i+1];
                i++;
            }
        }
        
        try {
            SocialGraph graph = new SocialGraph(new File(dataFile), dataFile.substring(dataFile.lastIndexOf("/")+1));
            System.out.println(graph.toString());
            
            /*for (String n : graph.getNodes()) {
                System.out.println("CC("+n+") = "+graph.getLocalClusteringCoefficient(n));
                System.out.println("PR("+n+") = "+graph.getPageRankNode(n, 0.8));
            }*/
            
            System.out.println("E(1,2) = " + graph.getEmbeddedness("1", "2"));
            
        } catch (SocialException | IOException ex) {
            Logger.getLogger(GraphTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}