/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.social.graph;

/**
 *
 * @author chus
 */
public class Node {
    private String id;
    
    public Node(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "V" + id;
    }
    
}
