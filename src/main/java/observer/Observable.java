package observer;

/**
 *  Objetos que pueden estar suscritos a otros objetos.
 */
public interface Observable {

    /* Contrato */

    /**
     * Suscribir objeto.
     * @param o
     */
    public void subscribeObserver(Observer o);

    /**
     * Desuscribir objeto.
     * @param o
     */
    public void unsubscribeObserver(Observer o);

    /**
     * Notificar cambios a los objetos suscritos.
     * @param taskName
     * @param executionTime
     * @param memoryNeeded
     */
    public void notifyObserver(String taskName, long executionTime, int memoryNeeded);

    /**
     * Obtener identificador.
     * @return
     */
    public int getId();
}
