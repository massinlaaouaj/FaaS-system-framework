package policies;

import comparators.ComparadorPorCantidadTareas;
import model.Invoker;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Coge la primera tarea, va recorriendo los invokers mientrás no se ejecute pasa al siguiente, es decir, mientrás pueda
 * abarcar tareas, se le asignan, hasta que no pueda más y pasa al siguiente. Teniendo en cuenta que la lista de  invokers
 * esta ordenado, siempre teniendo en la cabeza de la lista el invoker que esta más lleno de tareas, según
 * la política GreedyGroup nos interesa siempre asignar tareas al que más tenga hasta que este lleno.
 */
public class GreedyGroup implements PolicyManager {

    @Override
    public <T,R> List<Integer> invokeAction
            (List<Invoker> invokers,
             T input, int memoryNeeded) {
    	List<Integer> indexInvokersPack1 = new ArrayList<Integer>();
    	
        if(!Utils.isTaskApproachable(invokers, memoryNeeded)) {
            return indexInvokersPack1;
        }

        //Recorrer los invokers iterativamente, los voy rellenando al maximo hasta que no me permita mas, entonces
        //paso al siguiente.
        Collections.sort(invokers, new ComparadorPorCantidadTareas());
        int indexInvokers = 0;
        //Mientras no se ejecute, paso al siguiente invoker.
		while(invokers.get(indexInvokers).getTotalMemoryAvailable() < memoryNeeded){
		    indexInvokers++;
		    if(indexInvokers == invokers.size()){
		        indexInvokers = 0;
		    }
		}
		indexInvokersPack1.add(indexInvokers);
		return indexInvokersPack1;


    }

    @Override
    public <T,R> List<Integer> invokeAction
            (List<Invoker> invokers,
             List<T> input, int memoryNeeded) {
    	List<Integer> indexInvokersPack1 = new ArrayList<Integer>();
    	
        if(!Utils.isTaskApproachable(invokers, memoryNeeded, input)){
            return indexInvokersPack1;
        }
        Collections.sort(invokers, new ComparadorPorCantidadTareas());
        

        int indexInvokers = 0;
        for(int i = 0; i<input.size();i++) {
			while(invokers.get(indexInvokers).getTotalMemoryAvailable() < memoryNeeded){
			    //Pasar al siguiente invoker.
			    indexInvokers++;
			    //En caso de que me haya salido vuelvo a empezar.
			    if(indexInvokers == invokers.size()){
			        indexInvokers = 0;
			    }
			}
			indexInvokersPack1.add(indexInvokers);
            Collections.sort(invokers, new ComparadorPorCantidadTareas());
        }
        
        return indexInvokersPack1;
    }
}
