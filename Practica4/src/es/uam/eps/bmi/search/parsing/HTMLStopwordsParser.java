
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
 * Parseador de texto HTML que realiza eliminación de Stopwords además del
 * limpiado correspondiente al método {@link HTMLSimpleParser#parse(java.lang.String) }
 * de su clase padre.
 * <p>
 * Es necesario suministrar a este {@link TextParser} un fichero de Stopwords
 * que le sirva de referencia.
 * @author Ari Handler - Adrián Lorenzo
 * @see TextParser
 * @see HTMLSimpleParser
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
    
    /**
     * Reliza un filtrado primero de tags HTML mediante la llamada a 
     * {@link HTMLSimpleParser#parse(java.lang.String) } de su clase padre
     * {@link HTMLSimpleParser} y luego elimina las stopwords del texto suministrado
     * basándose en el fichero pasado al constructor.
     * @param text texto para procesar
     * @return el texto libre de tags HTML y Stopwords
     */
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
