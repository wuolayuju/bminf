package es.uam.eps.bmi.search.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/**
 * Parser que limpia un texto de tags HTML haciendo uso de <a href="http://jsoup.org/apidocs/">JSoup</a>.
 * @author Ari Handler - Adrián Lorenzo
 */
public class HTMLSimpleParser implements TextParser{

    /**
     * Procesa un texto mediante el método estático 
     * <a href="http://jsoup.org/apidocs/org/jsoup/parser/Parser.html#parse%28java.lang.String,%20java.lang.String%29">parse</a> de JSoup y luego
     * pasando el texto procesado por un limpiador adicional de etiquetas
     * HTML, <a href="http://jsoup.org/apidocs/org/jsoup/safety/Cleaner.html">Cleaner</a>, también de JSoup.
     * 
     * @param text Texto para procesar.
     * @return El texto pasado por Jsoup.
     * @see <a href="http://jsoup.org/apidocs/">JSoup</a><br>
     * <a href="http://jsoup.org/apidocs/org/jsoup/parser/Parser.html#parse%28java.lang.String,%20java.lang.String%29">parse</a><br>
     * <a href="http://jsoup.org/apidocs/org/jsoup/safety/Cleaner.html">Cleaner</a><br>
     * <a href="http://jsoup.org/apidocs/org/jsoup/safety/Whitelist.html">Whitelist</a><br>
     */
    @Override
    public String parse(String text){
        text = text.toLowerCase();
        if (text.indexOf("<!doctype") >= 0)
            text = text.substring(text.indexOf("<!doctype"));
        Document doc = Jsoup.parse(text);
        //Cleaner cl = new Cleaner(Whitelist.none());
        //text = cl.clean(doc).text();
        text = doc.text();
        text = text.replaceAll("[^a-zA-Z0-9\\s]", " ");
        text = text.replaceAll("\\s+\\s", " ");
        return text;
    }
    
}
