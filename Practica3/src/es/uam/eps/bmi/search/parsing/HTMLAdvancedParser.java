/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uam.eps.bmi.search.parsing;

/**
 * Interfaz para procesar y limpiar un texto
 * @author Ari Handler - Adrián Lorenzo
 */
public class HTMLAdvancedParser extends HTMLSimpleParser {
    
    private final HTMLStopwordsParser stopWordParser;
    private final HTMLStemParser stemParser;

    /**
     * Construye un índice que realiza eliminación de Stop Words y lexematización
     * a los documentos.
     * <p>
     * Internamente instancia un objeto de {@link HTMLStopWordsParser} y otro
     * de {@link HTMLStemParser} para la utilización secuencial de cada uno de
     * sus métodos {@link TextParser#parse(java.lang.String) }.
     * @param stopwordsPath fichero que contiene una lista de Stop Words.
     * @param stemmerName tipo de lexematizador a utilizar.
     */
    public HTMLAdvancedParser(String stopwordsPath, String stemmerName) {
        stopWordParser = new HTMLStopwordsParser(stopwordsPath);
        stemParser = new HTMLStemParser(stemmerName);
    }
    
    @Override
    public String parse(String text) {
        text = super.parse(text);

        String textNoStopWords = stopWordParser.parse(text);
        String textNoStopWordsStemmed = stemParser.parse(textNoStopWords);
        
        return textNoStopWordsStemmed;
    }
}
