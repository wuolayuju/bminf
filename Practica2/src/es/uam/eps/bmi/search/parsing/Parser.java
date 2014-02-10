/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.parsing;

import org.jsoup.Jsoup;

/**
 *
 * @author pinwi
 */
public class Parser implements TextParser{

    @Override
    public String parse(String text){
        return Jsoup.parse(text).text();
    }
    
}
