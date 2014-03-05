package es.uam.eps.bmi.search;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa la estructura básica de un documento de texto.
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class TextDocument implements Serializable{
    
    private final String id;
    private final String name;
    private final int size;

    /**
     * Crea un nuevo documento dado un identificador y la ruta al mismo.
     * @param id Número de identifiación del documento.
     * @param name Ruta completa del archivo.
     * @param size Tamaño del documento.
     */
    public TextDocument(String id, String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     * Determina si un documento es igual a otro, comparando sus identificadores.
     * @param obj Documento con el que comparar.
     * @return True si coinciden sus respectivos identificadores, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TextDocument other = (TextDocument) obj;
        return Objects.equals(this.id, other.id);
    }
    
    /**
     * Devuelve la ruta completa del archivo.
     * @return Cadena de caracteres a la ruta.
     */
    public String getName() {
        return name;
    }

    /**
     * Devuelve el identificador del archivo.
     *
     * @return Cadena de caracteres del identificador.
     */
    public String getId() {
        return id;
    }

    /**
     * Devuelve el tamaño del documento.
     * @return tamaño del documento.
     */
    public int getSize() {
        return size;
    }
}
