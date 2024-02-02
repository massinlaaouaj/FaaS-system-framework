package decorators;

import actions.Action;

/**
 * Cronometro que monitorea el tiempo de ejecución de la función de usuario e imprima por pantalla el tiempo transcurrido.
 * @param <T>
 * @param <R>
 */
public class TimerDecorator<T,R> implements Action<T,R>{
	
	private final Action<T,R> action;

	/**
	 * Constructor TimerDecorator
	 * @param action
	 */
	public TimerDecorator(Action<T,R> action) {
		this.action = action;
	}
	
	@Override
	public R run(T arg) throws Exception {
		long startTime = System.currentTimeMillis();
        R result = action.run(arg);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Tiempo de ejecución para '" + action.getName() +
                "': " + elapsedTime + " milisegundos");
        return result;
	}

	@Override
	public String getName() {
		return this.action.getName();
	}

}
