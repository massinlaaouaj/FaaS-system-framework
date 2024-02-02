package reflect;

import java.lang.reflect.InvocationHandler;

import java.lang.reflect.Method;
import java.util.List;

import model.Controller;

/**
 * Clase itermediaria.
 * @param <T>
 * @param <R>
 */
public class ActionProxyInvocationHandler<T, R> implements InvocationHandler {

    private final String actionName;
    private final Controller controller;
    private boolean isAsync;
    private boolean isGroupalInvoke;

    /**
     * Constructor ActionProxyInvocationHandler.
     * @param actionName
     * @param controller
     * @param isAsync
     * @param isGroupalInvoke
     */
    public ActionProxyInvocationHandler(String actionName, Controller controller, boolean isAsync, boolean isGroupalInvoke) {
        this.actionName = actionName;
        this.controller = controller;
        this.isAsync = isAsync;
        this.isGroupalInvoke = isGroupalInvoke;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("run")) {
            // Redirige la invocación al Controller usando el actionName
            if(!isAsync) {
                if(!isGroupalInvoke) {
                    return controller.invokeAction(actionName, args[0]);
                } else if (isGroupalInvoke){
                    List<T> lista = (List<T>) args[0];
                    return controller.invokeAction(actionName, lista);
                } else {
                    System.err.print("NO HAY ARGUMENTOS");
                }
            } else {
                if(!isGroupalInvoke) {
                    return controller.invokeAction_async(actionName, args[0]);
                } else if (isGroupalInvoke){
                    List<T> lista = (List<T>) args[0];
                    return controller.invokeAction_async(actionName, lista);
                } else {
                    System.err.print("NO HAY ARGUMENTOS");
                }
            }
        } else if (method.getName().equals("getName")) {
            return method.getName();
        }
        // Puedes manejar otros métodos si es necesario
        return null;
    }
}