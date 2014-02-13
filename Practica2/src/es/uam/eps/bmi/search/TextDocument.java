package es.uam.eps.bmi.search;

import java.util.Objects;

/**
 * Clase que representa la estructura básica de un documento de texto.
 * 
 * @author Ari Handler - Adrián Lorenzo
 */
public class TextDocument {
    
    private final String id;
    private final String name;

    /**
     * Crea un nuevo documento dado un identificador y la ruta al mismo.
     * @param id Número de identifiación del documento
     * @param name Ruta completa del archivo
     */
    public TextDocument(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

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
     * Devuelve la ruta completa del archivo
     * @return Cadena de caracteres a la ruta
     */
    public String getName() {
        return name;
    }

    /**
     * Devuelve el identificador del archivo
     *
     * @return Cadena de caracteres del identificador
     */
    public String getId() {
        return id;
    }

}
