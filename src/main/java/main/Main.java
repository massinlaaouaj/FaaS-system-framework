package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


/**
 * Main con ejemplo de uso
 */
@SuppressWarnings("unused")
public class Main {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Crear instancias de Runnable para cada tarea
        Runnable task1 = () -> {
            String[] arg = {};
            FaaSystem.main(arg);
        };

        Runnable task2 = () -> {
            String[] arg = {};
            Client.main(arg);
        };

        // Crear instancias de Thread para ejecutar cada tarea en un hilo separado
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        // Iniciar los hilos
        thread1.start();
        
        //Doy 3s para que el servidor se inicie y el cliente comience a hacer las peticiones
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        thread2.start();
		
    }
	
	/**
	 * 
	 * @param <R> Tipo genrico de la lista.
	 * @param list Lista de Futuros.
	 * @return true en el caso de que todos los futuros de la lista hayan terminado
	 * de ejecutarse. false en caso de que algn futuro est sin ejecutar.
	 */
	private static <R> boolean isFuturesListDone(List<Future<R>> list) {
		for(Future<R> future : list) {
			if(!future.isDone()) {
				return false;
			}
		}
		return true;
	}
	
	private static <R> List<R> getValuesFromFuturesList(List<Future<R>> list){
		List<R> listaReturned = new ArrayList<R>();
		for(Future<R> future : list) {
			try {
				listaReturned.add(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				System.err.println("Error al transformar la lista de futuros a lista de elementos.");
			}
		}
		return listaReturned;
	}
	
	private static void printMap(Map<String, Integer> map) {
		
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
