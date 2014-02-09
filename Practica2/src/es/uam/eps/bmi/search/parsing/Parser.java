/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.parsing;

import java.io.*;
import java.net.URL;
import org.jsoup.Jsoup;

/**
 *
 * @author pinwi
 */
public class Parser implements TextParser{

    @Override
    public String parse(String text) throws IOException {
        String aux = "", line;
        BufferedReader in = new BufferedReader(new InputStreamReader(new URL(text).openConnection().getInputStream()));
        do {
            line = in.readLine();
            aux += line;
        } while (line != null);
        return Jsoup.parse(aux).text();
    }
    
}
