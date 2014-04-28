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
public class ScoredNode {
    
    private String node;
    private double score;

    public ScoredNode(String node, double score) {
        this.node = node;
        this.score = score;
    }

    public String getNode() {
        return node;
    }

    public double getScore() {
        return score;
    }
    
    
}
