package model;

import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import actions.Action;
import observer.Observable;
import observer.Observer;
import utils.Utils;

/**
 * Encargado de llamar a la funcion correspondiente. El Controller, es el encargado de asignarle acciones y ocupando su memoria.
 */
public class Invoker implements Comparable<Invoker>, Observable {

    /* Atributos */

    private int id;                     // Identificador.
    private int currentTasks;           // Cantidad de tareas actuales.
    private int totalMemory;            // Memoria total del invoker.
    private int totalMemoryAvailable;   // Memoria disponible del invoker.
    private Observer observer;          // Su observador, en este caso el Controller.
    private ExecutorService executorService = Executors.newCachedThreadPool();
    // Lock para proteger el acceso al contador
    private final Lock lock = new ReentrantLock();

    /* Constructor */

    /**
     * Constructor Invoker
     * @param id
     * @param totalMemory
     */
    public Invoker (int id, int totalMemory) {
        this.id = id;
        this.totalMemory = totalMemory;
        this.totalMemoryAvailable = totalMemory;
    }

    /* Metodos */

    /**
     * Ejecución de la acción sobre el invoker, en mono-hilo.
     * @param action
     * @param input
     * @param memoryNeeded
     * @return
     * @param <T>
     * @param <R>
     */
    public <T,R> R executeAction (Action<T,R> action, T input, int memoryNeeded){

        this.lock.lock();
        if (this.totalMemoryAvailable >= memoryNeeded) { // En caso de memoria disponible

            // Reservar memoria para la funcin
            this.totalMemoryAvailable -= memoryNeeded;
            currentTasks++;

            this.lock.unlock();

            R result = null;
            long startingTime = System.currentTimeMillis();

            try {
                result = action.run(input);
            } catch (Exception e) {
                e.printStackTrace();
                Utils.showErrorMessage();
            }


            long finishTime = System.currentTimeMillis();
            long timeEllapsed = finishTime - startingTime;

            this.lock.lock();
            notifyObserver(action.getName(), timeEllapsed, memoryNeeded);
            // Liberar memoria de la funcin ocupada en monohilo (Comentar)
            this.totalMemoryAvailable += memoryNeeded;
            this.currentTasks--;
            this.lock.unlock();

            System.out.println("Accin ejecutada: " + result + " por el invoker: " + id);

            return result;
        } else { // No hay suficiente memoria disponible
            System.out.println("Invoker ocupado, asignando otro invoker...");
            this.lock.unlock();

            return null;
        }
    }

    /**
     * Ejecución de la acción sobre el invoker, en multi-hilo.
     * @param action
     * @param input
     * @param memoryNeeded
     * @return
     * @param <T>
     * @param <R>
     */
    public <T,R> Future<R> executeAction_async (Action<T,R> action, T input, int memoryNeeded){
        Future<R> futureResult = executorService.submit(new Callable<R>() {
            @Override
            public R call() throws Exception {
                return executeAction(action, input, memoryNeeded);
            }
        });

        //Necesito dormir el hilo 1ms para que el futureResult.isDone() pueda
        //valorar con claridad si el invokeAction devolvi null o est haciendo una tarea.
        try {
            Thread.sleep(1);
        }catch(Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            if(futureResult.isDone() && futureResult.get() == null) {
                return null;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return futureResult;

    }


    @Override
    public int compareTo(Invoker o) {
        //El metodo compareTo devuelve 1, -1, o 0. En funcion de si la comparacion
        //entre "this", el objeto que llama al metodo, y "o" el objeto que se pasa como parametro
        //es mayor, igual, o menor.
        return o.totalMemoryAvailable - this.totalMemoryAvailable;
    }

    @Override
    public void subscribeObserver(Observer o) {
        this.observer = o;
    }

    @Override
    public void unsubscribeObserver(Observer o) {
        this.observer = null;
    }

    @Override
    public void notifyObserver(String taskName, long executionTime, int memoryNeeded) {
        this.observer.update(this, taskName, executionTime, memoryNeeded);
    }

    /* Getters y Setters */

    /**
     * Obtener identificador invoker.
     * @return
     */
    public int getId(){
        return this.id;
    }

    /**
     * Obtener memoria total.
     * @return
     */
    public int getTotalMemory() {
        return this.totalMemory;
    }

    /**
     * Obtener memoria disponible.
     * @return
     */
    public int getTotalMemoryAvailable() {
        return this.totalMemoryAvailable;
    }

    /**
     * Asignar memoria total.
     * @param totalMemory
     */
    public void setTotalMemory(int totalMemory) {
        this.totalMemory = totalMemory;
    }

    /**
     * Asignar tarea.
     * @param currentTasks
     */
    public void setCurrentTasks(int currentTasks) {
        this.currentTasks = currentTasks;
    }

    /**
     * Asignar memoria disponible.
     * @param totalMemoryAvailable
     */
    public void setotalMemoryAvailable(int totalMemoryAvailable) {
        this.totalMemoryAvailable = totalMemoryAvailable;
    }

    /**
     * Obtener tareas actuales.
     * @return
     */
    public int getCurrentTasks(){
        return this.currentTasks;
    }

}
