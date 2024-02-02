package model;

import actions.Action;

/**
 * Clase representativa de una acción
 */
public class InvokerAction<T,R> {

    private String name;        // Nombre de la acción
    private Action<T,R> action; // Acción a ejecutar
    private int memoryNeeded;   // Memoria necesaria, para la ejecución de la acción


    /**
     * Constructor InvokerAction
     * @param name
     * @param action
     * @param memoryNeeded
     */
    public InvokerAction(String name, Action<T,R> action, int memoryNeeded){
        this.name = name;
        this.action = action;
        this.memoryNeeded = memoryNeeded;
    }

    /* Getters y Setters */

    /**
     * Obtener nombre de la acción.
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Asignar nombre a la acción.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtener acción.
     * @return
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Asignar acción.
     * @param action
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * Obtener memoria necesaria para ejecutar la acción.
     * @return
     */
    public int getMemoryNeeded() {
        return this.memoryNeeded;
    }

    /**
     * Asignar memoria necesaria para ejecutar la acción.
     * @param memoryNeeded
     */
    public void setMemoryNeeded(int memoryNeeded) {
        this.memoryNeeded = memoryNeeded;
    }
}
