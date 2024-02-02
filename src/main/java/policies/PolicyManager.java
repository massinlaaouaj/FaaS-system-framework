package policies;

import model.Invoker;

import java.util.List;

public interface PolicyManager {

    /**
     * Ejecución de la acción, con la politica seleccionada,  usando hilos.
     * @param list          * Lista de invokers
     * @param input         * Paràmetros necesarios según el tipo de acción a ejecutar
     * @param memoryNeeded  * Memoria necesaria para la acción a ejecutar
     * @return
     * @param <T>
     * @param <R>
     */
    public <T,R> List<Integer> invokeAction
            (List<Invoker> list, 
             T input, int memoryNeeded);


    /**
     * Ejecución de las acciones, con la politica seleccionada,  usando hilos.
     * @param list          * Lista de invokers
     * @param input         * Paràmetros necesarios según el tipo de acción a ejecutar
     * @param memoryNeeded  * Memoria necesaria para la acción a ejecutar
     * @return
     * @param <T>
     * @param <R>
     */
    public <T,R> List<Integer> invokeAction
            (List<Invoker> list,
             List<T> input, int memoryNeeded);
    
}
