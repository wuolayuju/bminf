/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.parsing;

import org.jsoup.Jsoup;

/**
 * Clase que representa la estructura básica de un documento de texto.
 * @author Ari Handler - Adrián Lorenzo
 */
public class HTMLSimpleParser implements TextParser{

    /**
     * 
     * Procesa un texto mediante el método estático parse de Jsoup
     * 
     * @param text
     * @return texto procesado
     */
    @Override
    public String parse(String text){
        return Jsoup.parse(text).text();
    }
    
}
