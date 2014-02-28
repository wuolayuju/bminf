/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ari.handler
 */
public class HTMLStopwordsParser extends HTMLSimpleParser{

    private List<String> stopwords;
    
    /**
     * Construye un {@link TextParser} que realiza un filtrado de Stopwords
     * de un texto, ayudándose del fichero de Stopwords pasado al constructor.
     * @param stopwordsPath ruta al archivo de Stopwords
     */
    public HTMLStopwordsParser(String stopwordsPath) {
        super();
        try {
            try (Scanner s = new Scanner(new File(stopwordsPath))) {
                stopwords = new ArrayList<>();
                while (s.hasNext()){
                    stopwords.add(s.next());
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HTMLStopwordsParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String parse(String text) {
        text = super.parse(text);
        
        ListIterator<String> itr = stopwords.listIterator();
        while(itr.hasNext()) {
            text = text.replaceAll("\\s"+itr.next()+"\\s", " ");
        }
        
        return text;
    }
    
}
