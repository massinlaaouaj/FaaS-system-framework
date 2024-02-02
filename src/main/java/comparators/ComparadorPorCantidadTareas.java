package comparators;

import model.Invoker;
import java.util.Comparator;

/**
 * Los objetos de una lista que queramos ordenar TIENEN que ser comparables. Implementar Comparable<Objeto>
 *     (metodo CompareTo)
 * Si creo un comparator, un criterio de comparación TIENE que ser un elemento capaz de comparar.
 * Implementar la inferfaz Comparator (metodo Compare)
 */
public class ComparadorPorCantidadTareas implements Comparator<Invoker> {

    /**
     *
     * @param i1 the first object to be compared.
     * @param i2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(Invoker i1, Invoker i2) {

        // En caso que los dos invokers tengan la misma cantidad de tareas,
        // comparar por la memoria total de los invokers, en caso contrario
        // devolver el resultado de comparar por el numero de tareas
        if(i2.getCurrentTasks() - i1.getCurrentTasks() == 0){
            //Comparame por memoria total.
            return i2.getTotalMemory() - i1.getTotalMemory();
        }
        return i2.getCurrentTasks() - i1.getCurrentTasks();
    }
}
