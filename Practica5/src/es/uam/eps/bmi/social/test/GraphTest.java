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
            SocialGraph graph = new SocialGraph(new File(dataFile));
            System.out.println(graph.getGraph().toString());
            
            
            
        } catch (SocialException | IOException ex) {
            Logger.getLogger(GraphTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
