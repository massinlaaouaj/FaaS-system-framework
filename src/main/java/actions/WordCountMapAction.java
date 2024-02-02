package actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Acción que contabiliza el número de repeticiones de cada palabra en un texto.
 */
public class WordCountMapAction implements Action<String, Map<String, Integer>>, Serializable {
    @Override
    public Map<String, Integer> run(String text) {

        //SIMULAMOS QUE ESTA ACCIÓN TARDA 1 segundo en realizarse.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        // Lógica de conteo de palabras, divide en base a uno o mas espacios en blanco.
        String[] words = text.split("\\s+");
        Map<String, Integer> wordCountMap = new HashMap<>();

        for (String word : words) {
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

        return wordCountMap;
    }

    @Override
    public String getName() {
        return "wordCountMapAction";
    }
}
