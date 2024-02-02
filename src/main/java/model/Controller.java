package model;

import java.lang.reflect.Proxy;
import java.util.ArrayList;


import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import actions.*;
import observer.Metrics;
import observer.Observable;
import observer.Observer;
import policies.PolicyManager;
import reflect.ActionProxyInvocationHandler;
import reflect.Calculator;
import utils.Utils;


/**
 * Responsable de recibir las peticiones de invocación de los usuarios
 * y seleccionar los Invokers que ejecutarán las acciones.
 */
public final class Controller implements Observer, Calculator {

    /* Atributos */
    private List<Invoker> invokers;
    @SuppressWarnings("rawtypes")
    private List<InvokerAction> actionsList;
    private PolicyManager pm;
    private Metrics metrics;


    /* Constructor */

    /**
     * Constructor clase Controller
     * @param numberOfInvokers
     * @param memoryOfInvoker
     * @param pm
     */
    @SuppressWarnings("rawtypes")
    public Controller (int numberOfInvokers, int memoryOfInvoker, PolicyManager pm) {
        this.invokers = new ArrayList<Invoker>();
        this.actionsList = new ArrayList<InvokerAction>();
        this.metrics = new Metrics();
        int initialPort = 12344;
        this.pm = pm;

        for(int i = 1; i <= numberOfInvokers; i++){
            Invoker invoker = new Invoker(i, memoryOfInvoker);
            invoker.subscribeObserver(this);
            invokers.add(invoker);
        }

        //Registro la accion por defecto.
        this.registerAction("addAction", new AddAction(), 100);
    }


    /**
     * Añadir una nueva acción a la lista de acciones del Controller.
     * @param actionName
     * @param action
     * @param memoryNeeded
     * @param <T>
     * @param <R>
     */
    public <T,R> void registerAction(String actionName, Action<T,R> action, int memoryNeeded){
        actionsList.add(new InvokerAction<T,R>(actionName, action, memoryNeeded));
    }


    /**
     * Ejecución de una acción.
     * @param actionName
     * @param input
     * @return
     * @param <T>
     * @param <R>
     */
    public <T,R> R invokeAction (String actionName, T input) {

        Action<T,R> actionToExecute = null;
        int memoryNeeded = 0;

        for(InvokerAction<T,R> action: actionsList){
            if(action.getName().equals(actionName)){
                actionToExecute =  action.getAction();
                memoryNeeded = action.getMemoryNeeded();
            }
        }

        // Mostramos un mensaje de error en caso de no encontrar la accion
        if(actionToExecute == null){
            System.err.println("La acción " + actionName + " no está registrada en nuestro sistema");
            return null;
        }

        List<Integer> listaInvokers = pm.invokeAction(this.invokers,input,memoryNeeded);
        R result = this.invokers.get(listaInvokers.get(0)).executeAction(actionToExecute, input, memoryNeeded);
        return result;
    }

    /**
     * Ejecución de una acción, en multi-hilo.
     * @param actionName
     * @param input
     * @return
     * @param <T>
     * @param <R>
     */
    public <T,R> R invokeAction_async (String actionName, T input) {

        Action<T,R> actionToExecute = null;
        int memoryNeeded = 0;

        for(InvokerAction<T,R> action: actionsList){
            if(action.getName().equals(actionName)){
                actionToExecute =  action.getAction();
                memoryNeeded = action.getMemoryNeeded();
            }
        }

        // Mostramos un mensaje de error en caso de no encontrar la accion
        if(actionToExecute == null){
            System.err.println("La acción " + actionName + " no está registrada en nuestro sistema");
            return null;
        }

        List<Integer> listaInvokers = pm.invokeAction(this.invokers,input,memoryNeeded);
        Future<R> result = this.invokers.get(listaInvokers.get(0)).executeAction_async(actionToExecute, input, memoryNeeded);

        while(!result.isDone()) {}

        try {
            return result.get();
        } catch (Exception e) {}

        return null;
    }

    /**
     * Sobrecarga del método "invokeAction". Ejecución de una lista de acciones.
     * @param actionName
     * @param input
     * @param <T>
     * @param <R>
     */
    public <T,R> List<R> invokeAction (String actionName, List<T> input) {
        Action<T,R> actionToExecute = null;
        int memoryNeeded = 0;

        for(InvokerAction<T,R> action: actionsList){
            if(action.getName().equals(actionName)){
                actionToExecute = action.getAction();
                memoryNeeded = action.getMemoryNeeded();
            }
        }

        // Mostramos un mensaje de error en caso de no encontrar la accion
        if(actionToExecute == null){
            System.err.println("La accion " + actionName + " no esta registrada en nuestro sistema");
            return null;
        }

        List<Integer> listaInvokers = pm.invokeAction(this.invokers,input,memoryNeeded);
        List<R> listaResult = new ArrayList<R>();

        if(Utils.validateIfListInvokersCorrect(listaInvokers.size(), input.size())) {
            for(int i = 0; i < listaInvokers.size(); i++) {
                listaResult.add(this.invokers.get(listaInvokers.get(i)).executeAction(actionToExecute, input.get(i), memoryNeeded));
            }
        }

        return listaResult;

    }

    /**
     * Ejecución de una lista de acciones en multi-hilo.
     * @param actionName
     * @param input
     * @return
     * @param <T>
     * @param <R>
     */
    @SuppressWarnings("unchecked")
    public <T,R> List<R> invokeAction_async (String actionName, List<T> input) {

        Action<T,R> actionToExecute = null;
        int memoryNeeded = 0;

        for(InvokerAction<T,R> action: actionsList){
            if(action.getName().equals(actionName)){
                actionToExecute =  action.getAction();
                memoryNeeded = action.getMemoryNeeded();
            }
        }

        // Mostramos un mensaje de error en caso de no encontrar la accion
        if(actionToExecute == null){
            System.err.println("La acción " + actionName + " no está registrada en nuestro sistema");
            return null;
        }

        List<Integer> listaInvokers = pm.invokeAction(this.invokers,input,memoryNeeded);
        List<Future<R>> listaResult = new ArrayList<Future<R>>();

        if(Utils.validateIfListInvokersCorrect(listaInvokers.size(), input.size())) {
            for(int i = 0; i < listaInvokers.size(); i++) {
                listaResult.add(this.invokers.get(listaInvokers.get(i)).executeAction_async(actionToExecute, input.get(i), memoryNeeded));
            }
        }

        while(!Utils.isFuturesListDone(listaResult)) {}

        List<R> listaRet = Utils.getValuesFromFuturesList(listaResult);

        return listaRet;
    }

    /**
     * Crear el proxy de la acción.
     *
     * En cuanto al metodo newProxyInstance recibe 3 parámetros
     * El cargador de clase: Este es el cargador de clases que se utiliza para cargar la interfaz Action y sus dependencias.
     *
     * Aquí se especifica la interfaz que el proxy debe implementar. En este caso, se proporciona un array
     * de clases, y solo hay una clase, que es Action.class.
     * Esto significa que el proxy implementará la interfaz Action.
     *
     * Le pasamos al action proxy un gesto de invocaciones de cara a que sepa como ejecutar las acciones.
     *
     * En resumen: Estamos creando un action proxy dinámimco, encargado de quitarle el peso de ejecutar
     * las acciones al controller, a continuación las acciones serán ejecutadas por el actionproxy. A la hora
     * de crearlo se le pasa el cargador de clases que se utiliza para cargar la interfaz action,
     * se le pasa  además la interfaz que el proxy debe implementar y además le pasamos un handler (gestor)
     * de cara a que este action proxy sepa como lidiar con las acciones
     *
     * @param actionName        Nombre de la acción.
     * @param isAsync           En caso de ejecutar en multi-hilo, pasar por valor "true".
     * @param isGroupalInvoke   En caso de ejecutar invocaciones grupales, pasar por valor "true", en caso de ser valor "false" realizar una invocación individual.
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Action createActionProxy(String actionName, boolean isAsync, boolean isGroupalInvoke) {
        ActionProxyInvocationHandler handler = new ActionProxyInvocationHandler<>(actionName, this,isAsync, isGroupalInvoke);
        return (Action) Proxy.newProxyInstance(
                Action.class.getClassLoader(),
                new Class<?>[]{Action.class},
                handler
        );
    }

    @Override
    public void update(Observable observable, String taskName, long executionTime, int memoryNeeded) {
        this.metrics.addMetric(observable, taskName, executionTime, memoryNeeded);
    }

    /**
     * Mostrar las metricas.
     */
    public void showMetrics() {
        System.out.println(metrics.showMetrics());
    }


    @SuppressWarnings("unchecked")
    @Override
    public int add(int arg1, int arg2) throws Exception {
        return (int) this.createActionProxy("addAction", false, false).run(Map.of("x", arg1, "y", arg2));
    }

    /* Getters y Setters */

    /**
     * Obtener lista invokers.
     * @return
     */
    public List<Invoker> getInvokers() {
        return this.invokers;
    }

    /**
     * Asignar lista invokers.
     * @param invokers
     */
    public void setInvokers(List<Invoker> invokers) {
        this.invokers = invokers;
    }

    /**
     * Obtener lista acciones.
     * @return
     */
    public List<InvokerAction> getActionsList() {
        return this.actionsList;
    }

    /**
     * Asignar lista de acciones.
     * @param actionsList
     */
    public void setActionsList(List<InvokerAction> actionsList) {
        this.actionsList = actionsList;
    }

    /**
     * Ontener política.
     * @return
     */
    public PolicyManager getPm() {
        return this.pm;
    }

    /**
     * Asignar política.
     * @param pm
     */
    public void setPm(PolicyManager pm) {
        this.pm = pm;
    }

    /**
     * Obtener metricas.
     * @return
     */
    public Metrics getMetrics() {
        return this.metrics;
    }

    /**
     * Asignar metricas.
     * @param metrics
     */
    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

}
