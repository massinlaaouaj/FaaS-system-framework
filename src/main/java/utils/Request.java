package utils;

import actions.Action;

import java.io.Serializable;

/**
 * Utils para el server/client.
 * @param <T> Parámetro de entrada
 * @param <R> Parámetro de salida
 */
public class Request<T,R> implements Serializable {

	private Action<T,R> action;
	private T input;
	private boolean isGroupalInvoke;
	private boolean isAsync;
	private R result;
	private boolean isDone;

	/**
	 * Constructor Request
	 * @param action
	 * @param input
	 * @param isGroupalInvoke
	 * @param isAsync
	 */
	public Request(Action<T,R> action, T input, boolean isGroupalInvoke, boolean isAsync) {
		this.action = action;
		this.input = input;
		this.isGroupalInvoke = isGroupalInvoke;
		this.isAsync = isAsync;
		this.result = null;
		this.isDone = false;
	}

	/**
	 * Obtener acción
	 * @return
	 */
	public Action<T, R> getAction() {
		return action;
	}

	/**
	 * Obtener argumentos acción
	 * @return
	 */
	public T getInput() {
		return input;
	}

	/**
	 * Invocación grupal
	 * @return
	 */
	public boolean isGroupalInvoke() {
		return isGroupalInvoke;
	}

	/**
	 * Multi-hilo (True), mono-hilo (False)
	 * @return
	 */
	public boolean isAsync() {
		return isAsync;
	}

	/**
	 * Obtener resultado
	 * @return
	 */
	public R getResult() {
		return result;
	}

	/**
	 * Asignar resultado
	 * @param result
	 */
	public void setResult(R result) {
		this.result = result;
	}

	/**
	 * Asignar estado acción
	 * @param isDone
	 */
	public void setIsDone(boolean isDone) {
		this.isDone = isDone;
	}

	/**
	 * Obtener estado acción
	 * @return
	 */
	public boolean isDone() {
		return isDone;
	}
	
	
}
