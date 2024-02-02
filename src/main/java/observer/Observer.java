package observer;

/**
 *  Interficie para ser implementada por objetos que deseen ser notificados de cambios en un objeto observado "Observable".
 */
public interface Observer {

    /* Contrato */

    /**
     * Notifica cuando el estado del objeto observado cambia.
     * @param observable
     * @param taskName
     * @param executionTime
     * @param memoryNeeded
     */
    void update(Observable observable, String taskName, long executionTime, int memoryNeeded);
}
