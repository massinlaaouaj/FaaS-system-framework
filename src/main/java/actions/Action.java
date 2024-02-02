package actions;

/**
 * Una accion va a constar de dos tipos.
 * @param <T> Tipo del parámetro
 * @param <R> Tipo del retorno.
 */
public interface Action<T,R> {

    /* Contrato */

    /**
     * Ejecución de la tarea en la acción/función escoguida.
     * @param arg Parámetro de entrada.
     * @return Valor de retorno.
     * @throws Exception
     */
    R run(T arg) throws Exception;

    /**
     * Obtener el nombre de la acción
     * @return Devuelve un string con el nombre de la acción
     */
    String getName();
}