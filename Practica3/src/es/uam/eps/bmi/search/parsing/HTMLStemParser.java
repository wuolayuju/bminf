/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tartarus.snowball.SnowballStemmer;

/**
 * Interfaz para procesar y limpiar un texto
 * @author Ari Handler - Adrián Lorenzo
 */
public class HTMLStemParser extends HTMLSimpleParser {
    
    /**
     * Representa el lexematizador en idioma inglés
     */
    public static final String ENGLISH_STEMMER = "english";
    /**
     * Representa el lexematizador de Porter
     */
    public static final String PORTER_STEMMER = "porter";
    
    private SnowballStemmer stemmer;

    /**
     * Construye un {@link TextParser} que realiza una lexematización de un
     * texto a la hora de llamar al método {@link HTMLStemParser#parse(java.lang.String)}.
     * <p>
     * Recibe como argumento el stemmizador que se usará.
     * @param stemmerName stemmizador que se utilizará a la hora del parseo.
     */
    public HTMLStemParser(String stemmerName) {
        super();
        try {
            Class stemClass = Class.forName("org.tartarus.snowball.ext." +
                    stemmerName + "Stemmer");
            stemmer = (SnowballStemmer) stemClass.newInstance();
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(HTMLStemParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String parse(String text) {
        text = super.parse(text);
        
        String stemmedText = "";
        List<String> listText = Arrays.asList(text.split(" "));
        
        ListIterator<String> itr = listText.listIterator();
        while(itr.hasNext()) {
            stemmer.setCurrent(itr.next());
            stemmer.stem();
            stemmedText += stemmer.getCurrent() + " ";
        }
        
        return stemmedText;
    }
    
}
