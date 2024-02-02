package utils;

import model.Invoker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Utilidades del sistema
 */
public class Utils {

    /**
     * Validar si existe un invoker, con tal memoria total, capaz de ejecutar la tarea con memoria "memoryNeeded".
     * @param invokers      * Lista de invokers.
     * @param memoryNeeded  * Memoria necesaria para ejecutar la tarea.
     * @return
     */
    public static boolean isTaskApproachable(List<Invoker> invokers, int memoryNeeded) {
        //Validar primero si existe un invoker, con tal memoria total, capaz de ejecutar la tarea.
        double maxMemory  = -1;

        for(Invoker invoker: invokers){
            if(invoker.getTotalMemory() > maxMemory){
                maxMemory = invoker.getTotalMemoryAvailable();
            }
        }
        if(maxMemory < memoryNeeded){
            System.err.println("Se ha tratado de asignar una tarea de un tamaño de memoria " +
                    "que no es abarcable por nuestro sistema.");
            return false;
        }
        return true;
    }

    /**
     * Validar primero si existe un invoker, con tal memoria total, capaz de ejecutar la tarea, si no puede hacer las tareas a la vez las denegamos.
     * @param invokers
     * @param memoryNeeded
     * @param lista
     * @return
     * @param <T>
     */
    public  static <T> boolean isTaskApproachable(List<Invoker> invokers, int memoryNeeded, Collection<T> lista){
        //Validar primero si existe un invoker, con tal memoria total, capaz de ejecutar la tarea.
        //Si no podemos hacer las tareas a la vez las denegamos.
        int cantidadTareas = lista.size();
        int cantidadTareasRealizables = 0;
        for(Invoker invoker: invokers){
            if(invoker.getTotalMemory() >= memoryNeeded){
                cantidadTareasRealizables += (int) (invoker.getTotalMemory() / memoryNeeded);
            }
        }
        if(cantidadTareasRealizables < cantidadTareas){
            System.err.println("No hay memoria suficiente para asignar este grupo de tareas a la vez.");
            return false;
        }
        return true;
    }

    /**
     * Mostrar un mensaje generico a los errores de una sección crítica del codigo.
     */
    public static void showErrorMessage() {
        //Normalmente este es el motivo por el que se produce un error en esas secciones de codigo
        System.err.println("Error a la hora de castear los argumentos. Probablemente el input"
                + " que has introducido no es del tipo esperado para la accion");
    }

    /**
     * Limpieza del texto del libro, pasado por paràmtero.
     * @param filePath  * Ruta del libro
     * @return
     */
    public static String readFileAsString(String filePath) {

        byte[] encodedBytes;

        try {
            encodedBytes = Files.readAllBytes(Paths.get(filePath));
            String stringReturned = new String(encodedBytes, StandardCharsets.UTF_8);

            // Limpio el texto de caracteres especiales:
            String cleanText = keepOnlyAlphabeticAndSpaces(stringReturned);

            return cleanText;
        } catch (IOException e) {
            System.err.println("Error while reading a file");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Restringir unicamente caràcteres del alfabeto.
     * @param input
     * @return
     */
    public static String keepOnlyAlphabeticAndSpaces(String input) {

        // Utiliza una expresión regular para mantener solo letras y espacios utilizando regex, expresión regular ajustada con Unicode: "[^a-zA-Z\\u00E1\\u00E9\\u00ED\\u00F3\\u00FA\\u00FC\\u00C1\\u00C9\\u00CD\\u00D3\\u00DA\\u00DC\\u00F1\\u00D1\\s]";
        return input.replaceAll("[^a-zA-ZáéíóúüÁÉÍÓÚÜñÑ\\s]", "");
    }

    /**
     * Validar lista invokers
     * @param lengthIndexInvokers
     * @param tasks
     * @return
     */
    public static boolean validateIfListInvokersCorrect(int lengthIndexInvokers, int tasks) {
        if(lengthIndexInvokers != tasks) {
            System.err.print("El tamaño de la lista de invokers generado por el PM y el numero"
                    + " de tareas no coinciden");
            return false;
        }
        return true;
    }

    /**
     * Estado lista multi-hilo
     * @param list
     * @return
     * @param <R>
     */
    public static <R> boolean isFuturesListDone(List<Future<R>> list) {
        for(Future<R> future : list) {
            if(!future.isDone()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Obtener lista future, multi-hilo
     * @param list
     * @return
     * @param <R>
     */
    public static <R> List<R> getValuesFromFuturesList(List<Future<R>> list){
        List<R> listaReturned = new ArrayList<R>();
        for(Future<R> future : list) {
            try {
                listaReturned.add(future.get());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.err.println("Error al transformar la lista de futuros a lista de elementos.");
            }
        }
        return listaReturned;
    }

    /**
     * Mostrar contenido map
     * @param map
     */
    public static void printMap(Map<String, Integer> map) {

        //.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) EN ORDEN DESCENDENTE

        List<Map.Entry<String, Integer>> sortedEntries = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());


        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key + ": " + value);
        }
    }
}

