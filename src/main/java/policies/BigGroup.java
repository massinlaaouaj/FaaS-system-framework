package policies;

import comparators.ComparadorPorCantidadTareas;
import model.Invoker;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * En un mismo Invoker en caso de abarcar más de un grupo de acciones se le asigna.
 */
public class BigGroup implements PolicyManager{

    private int groupSize;    // Tamaño del grupo.

    /**
     * Construcot BigGroup
     * @param groupSize
     */
    public BigGroup(int groupSize){
        this.groupSize = groupSize;
    }

    /**
     * Invocación de invokers, realizando un grupo,  usando hilos.
     * @param invokers
     * @param input
     * @param memoryNeeded
     * @return
     * @param <T>
     * @param <R>
     */
    public <T,R> List<Integer> invokeAction(List<Invoker> invokers, T input, int memoryNeeded) {
    	List<Integer> listIndexInvoker = new ArrayList<Integer>();
    	
        if(!Utils.isTaskApproachable(invokers, memoryNeeded)) {
            return listIndexInvoker;
        }
 
        Collections.sort(invokers, new ComparadorPorCantidadTareas());



        int indexInvoker= 0;

        while(true){
            int possibleGroupsByThisInvoker =
                    (int)((int)(invokers.get(indexInvoker).getTotalMemory() / memoryNeeded) / this.groupSize);
            if(possibleGroupsByThisInvoker == 0) {possibleGroupsByThisInvoker=1;}
            if(invokers.get(indexInvoker).getCurrentTasks() < this.groupSize * possibleGroupsByThisInvoker
                    && memoryNeeded <= invokers.get(indexInvoker).getTotalMemoryAvailable() ){

				listIndexInvoker.add(indexInvoker);
				break;
            }
            indexInvoker++;
            if(indexInvoker == invokers.size()){
                indexInvoker = 0; 
                Collections.sort(invokers, new ComparadorPorCantidadTareas()); 
            }
        }
        return listIndexInvoker;
    }

    /**
     * Invocación de invokers, realizando un grupo, usando hilos.
     * @param invokers
     * @param input
     * @param memoryNeeded
     * @return
     * @param <T>
     * @param <R>
     */
    public <T,R> List<Integer> invokeAction(List<Invoker> invokers, List<T> input, int memoryNeeded) {
    	
    	List<Integer> listIndexInvoker = new ArrayList<Integer>();
    	
        if(!Utils.isTaskApproachable(invokers, memoryNeeded, input)){
            return listIndexInvoker;
        }
        
        Collections.sort(invokers, new ComparadorPorCantidadTareas());
        
        boolean exit = false;
        int indexInvoker= 0;

        for(int i = 0; i < input.size(); i++) {
            while (!exit) {
                int possibleGroupsByThisInvoker =
                        (int)((int)(invokers.get(indexInvoker).getTotalMemory() / memoryNeeded) / this.groupSize);
                if(possibleGroupsByThisInvoker == 0) {possibleGroupsByThisInvoker=1;}
                if (invokers.get(indexInvoker).getCurrentTasks() < this.groupSize * possibleGroupsByThisInvoker
                        && memoryNeeded <= invokers.get(indexInvoker).getTotalMemoryAvailable()) {

                	listIndexInvoker.add(indexInvoker);
					
                    exit = true;
                    indexInvoker = -1;
                    Collections.sort(invokers, new ComparadorPorCantidadTareas()); 
                }
                indexInvoker++;
                if (indexInvoker == invokers.size()) {
                    indexInvoker = 0;
                    Collections.sort(invokers, new ComparadorPorCantidadTareas()); 
                }
            }

            exit = false;
        }
        
        return listIndexInvoker;
    }
}
