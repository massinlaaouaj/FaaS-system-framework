package actions;

import java.util.HashMap;
import java.util.Map;

/**
 * Acción para el contaje del número de palabras totales en un texto.
 */
public class CountWordsAction implements Action<String, Map<String, Integer>> {
    @Override
    public Map<String, Integer> run(String text) {
        String[] words = text.split("\\s+");

        // Crear un mapa para almacenar el recuento de cada palabra
        Map<String, Integer> wordCountMap = new HashMap<>();

        // Iterar sobre las palabras y actualizar el recuento en el mapa
        for (String word : words) {
            // Incrementar el recuento para la palabra actual
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

        // Crear un mapa final con claves únicas y el número total de palabras
        Map<String, Integer> result = new HashMap<>();
        int totalWords = words.length;

        for (String uniqueWord : wordCountMap.keySet()) {
            result.put(uniqueWord, totalWords);
        }

        return result;
    }

    @Override
    public String getName() {
        return "countWordsAction";
    }
}
