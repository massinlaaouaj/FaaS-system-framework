package decorators;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import actions.Action;

/**
 * Guardar en una caché el resultado de una acción. El
 * sistema accede a una caché guardada en el Invoker, y guardará el resultado de una acción,
 * junto con su identificador y parámetros de entrada. En invocaciones futuras donde coincidan
 * los parámetros de entrada, devolveremos el resultado de la acción directamente
 * recuperándose de la caché, sin ejecutar la acción
 * @param <T>
 * @param <R>
 */
public class MemoizationDecorator<T,R> implements Action<T,R>{

    private final Action<T,R> action;
    private final Map<T,R> cache;
    private final Lock lock = new ReentrantLock();

    /**
     * Constructor MemoizationDecorator.
     * @param action Acción a almacenar en caché
     */
    public MemoizationDecorator(Action<T, R> action) {
        this.action = action;
        this.cache = new HashMap<T,R>();
    }

    @Override
    public R run(T input) throws Exception {
        //Si estamos trabajando con caché, bloqueamos el  hilo, ya que la caché
        //es una sección crítica de código.
        lock.lock();
        if (cache.containsKey(input)) {
            lock.unlock();
            System.out.println("Recuperando resultado de caché para '" +
                    action.getName() + "' con parámetro " + input);
            return cache.get(input);
        } else {
            R result = action.run(input);
            cache.put(input, result);
            lock.unlock();
            return result;
        }
    }

    @Override
    public String getName() {
        return this.action.getName();
    }
}
