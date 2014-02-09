/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.parsing;

import java.io.IOException;

/**
 *
 * @author e185318
 */
public interface TextParser {
    
    public String parse(String text) throws IOException;
}
