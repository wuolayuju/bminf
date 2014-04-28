package es.uam.eps.bmi.recom.impl.recommender;

import es.uam.eps.bmi.recom.impl.similarity.ItemFeature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Clase que monta un perfil de usuario basándose en diferentes aspectos sacados
 * tanto de un fichero como de un modelo de datos.
 * @author Ari Handler - Adrián Lorenzo
 */
public class UserProfiler {

	/**
	 * Obtiene una lista de características de ítems que un determinado usuario
	 * ha asignado a una lista de ítems, que servirá para crear un perfil de dicho
	 * usuario.
	 * @param userID identificador de usuario.
	 * @param tagsFile fichero de usuarios asignando características a ítems.
	 * @return lista de características asignadas a ítems por el usuario.
	 * @throws IOException si se produce algún error con el fichero.
	 */
    public static List<ItemFeature> getProfileTagsFromUser(long userID, File tagsFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tagsFile));
        
        String line = br.readLine(); // Headers
        
        List<ItemFeature> listItf = new ArrayList<>();
        
        boolean found = false;
        while((line = br.readLine())!= null) {
            StringTokenizer st = new StringTokenizer(line);
            long uId = Long.parseLong(st.nextToken());
            if (uId == userID) {
                ItemFeature itf = new ItemFeature(Long.parseLong(st.nextToken()), Long.parseLong(st.nextToken()), 0);
                listItf.add(itf);
            }
            if (found) break;
        }
        
        return listItf;
    }
}
