package es.uam.eps.bmi.search.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/**
 * Parser que limpia un texto de tags HTML 
 * @author Ari Handler - Adrián Lorenzo
 */
public class HTMLSimpleParser implements TextParser{

    /**
     * Procesa un texto mediante el método estático parse de Jsoup.
     * 
     * @param text Texto para procesar.
     * @return El texto pasado por Jsoup.
     */
    @Override
    public String parse(String text){
        Document doc = Jsoup.parse(text);
        Cleaner cl = new Cleaner(Whitelist.none());
        return cl.clean(doc).text();
    }
    
}
