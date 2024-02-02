package actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Acción para que reciba los resultados de todas las funciones y agregue el resultado.
 */
public class ReduceAction implements Action<List<Map<String, Integer>>, Map<String, Integer>>, Serializable {
    @Override
    public Map<String, Integer> run(List<Map<String, Integer>> mapResults) {
        // Lgica de reduccin
        Map<String, Integer> reducedResult = new HashMap<>();

        for (Map<String, Integer> map : mapResults) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();
                reducedResult.put(word, reducedResult.getOrDefault(word, 0) + count);
            }
        }

        return reducedResult;
    }

    @Override
    public String getName() {
        return "reduceAction";
    }
}
