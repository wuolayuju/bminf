
package es.uam.eps.bmi.search.parsing;

/**
 * Parseador de texto HTML que realiza una lexematización de términos y eliminación
 * de Stopwrods además del limpiado correspondiente al método 
 * {@link HTMLSimpleParser#parse(java.lang.String) } de su clase padre.
 * <p>
 * Es necesario suministrar a este {@link TextParser} el tipo de lexematizador
 * que usará en su método {@link parse}, pudiéndose valer de los nombres de las
 * clases de lexematizadores declarados como atributos de clase, adeemás de un 
 * fichero de Stopwords que le sirva de referencia.
 * @author Ari Handler - Adrián Lorenzo
 * @see TextParser
 * @see HTMLSimpleParser
 * @see HTMLStemParser
 * @see HTMLStopwordsParser
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
    
    /**
     * Hace uso de los {@link TextParser} suministrados al constructor para
     * realizar un filtrado de Stopwords y lexematización de términos secuencial
     * @param text texto para procesar
     * @return el texto libre de tags HTML y Stopwords, con sus términos lexematizados.
     */
    @Override
    public String parse(String text) {
        text = super.parse(text);

        String textNoStopWords = stopWordParser.parse(text);
        String textNoStopWordsStemmed = stemParser.parse(textNoStopWords);
        
        return textNoStopWordsStemmed;
    }
}
