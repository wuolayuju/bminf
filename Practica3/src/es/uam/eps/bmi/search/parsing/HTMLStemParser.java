
package es.uam.eps.bmi.search.parsing;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tartarus.snowball.SnowballStemmer;

/**
 * Parseador de texto HTML que realiza una lexematización de términos además del
 * limpiado correspondiente al método {@link HTMLSimpleParser#parse(java.lang.String) }
 * de su clase padre.
 * <p>
 * Es necesario suministrar a este {@link TextParser} el tipo de lexematizador
 * que usará en su método {@link parse}, pudiéndose valer de los nombres de las
 * clases de lexematizadores declarados como atributos de clase.
 * @author Ari Handler - Adrián Lorenzo
 * @see TextParser
 * @see HTMLSimpleParser
 * @see <a href="http://snowball.tartarus.org/">SnowBall Stemmer</a>
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

    /**
     * Reliza un filtrado primero de tags HTML mediante la llamada a 
     * {@link HTMLSimpleParser#parse(java.lang.String) } de su clase padre
     * {@link HTMLSimpleParser} y luego lexematiza cada término con la API
     * <a href="http://snowball.tartarus.org/">SnowBall Stemmer</a>.
     * basándose en el fichero pasado al constructor.
     * @param text texto para procesar
     * @return el texto libre de tags HTML y con sus términos lexematizados
     */
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
