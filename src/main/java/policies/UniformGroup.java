package policies;

import comparators.ComparadorPorCantidadTareas;
import model.Invoker;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Colocación de las acciones según el número de grupo establecido y de manera uniforme ejecutar entre todos los Invokers.
 * Dado el siguiente ejemplo, en caso de hacer 6 tareas y agrupar en 3 tareas, y tener tres invokers (invoker1, invoker2, invoker3)
 * el invoker3 dado el caso que ya tuviese una tarea asignada previamente a la asignación de tareas del UniformGroup, se pondra en la cabeza
 * de la lista de invokers y se le "llenara" de tareas, hasta formar el grupo de 3 tareas (1 tarea previamente asignada + 2 nuevas tareas del grupo),
 * entonces para al invoker1 se le asigna 3 tareas y finalmente al invoker2 el cual se le asigna las restantes.
 */
public class UniformGroup implements PolicyManager {

    private int groupSize;

    /**
     * Constructor política UniformGroup
     * @param groupSize
     */
    public UniformGroup(int groupSize){
        this.groupSize = groupSize;
    }

    @Override
    public <T,R> List<Integer> invokeAction (List<Invoker> invokers, T input, int memoryNeeded) {
    	
    	List<Integer> listIndexInvoker = new ArrayList<Integer>();
    	
        if(!Utils.isTaskApproachable(invokers, memoryNeeded)) {
            return listIndexInvoker;
        }

        //Ordenar la lista de invokers en base a la cantidad de tareas con las que están trabajando
        //El invoker que mas tareas esta realizando ahora está en la posicion 0 de la lista.
        Collections.sort(invokers, new ComparadorPorCantidadTareas());

        boolean exit = false;
        int indexInvoker= 0;

        while(!exit){
            //Si el invoker no tiene aun el tamaño de grupo de tareas asignado, mientras
            //tenga memoria suficiente disponible para la tarea se le asignará.
            if(invokers.get(indexInvoker).getCurrentTasks() < this.groupSize
                    && memoryNeeded <= invokers.get(indexInvoker).getTotalMemoryAvailable() ){
                
				listIndexInvoker.add(indexInvoker);
                exit = true;
            }
            indexInvoker++;
            //Si ya hemos llegado al ultimo invoker, volvemos al principio.
            if(indexInvoker == invokers.size()){
                indexInvoker = 0; //Establecemos de nuevo el indice a 0.
                Collections.sort(invokers, new ComparadorPorCantidadTareas()); //Actualizamos el orden de invokers.
            }
        }
        return listIndexInvoker;
    }


    @Override
    public <T,R> List<Integer> invokeAction (List<Invoker> invokers, List<T> input, int memoryNeeded) {

    	List<Integer> listIndexInvoker = new ArrayList<Integer>();
    	
        if(!Utils.isTaskApproachable(invokers, memoryNeeded, input)){
            return listIndexInvoker;
        }

        Collections.sort(invokers, new ComparadorPorCantidadTareas());

        boolean exit = false;
        int indexInvoker= 0;
        
        //Recorre la lista de tareas.
        for(int i = 0; i < input.size(); i++) {
            while (!exit) {
                //Si el invoker no tiene aun el tamaño de grupo de tareas asignado, mientras
                //tenga memoria suficiente disponible para la tarea se le asignará.
                if (invokers.get(indexInvoker).getCurrentTasks() < this.groupSize
                        && memoryNeeded <= invokers.get(indexInvoker).getTotalMemoryAvailable()) {
					listIndexInvoker.add(indexInvoker);
                    exit = true;
                    indexInvoker = -1;
                    Collections.sort(invokers, new ComparadorPorCantidadTareas()); //Actualizo el orden en la lista
                }
                indexInvoker++;
                if (indexInvoker == invokers.size()) {
                    indexInvoker = 0;
                    Collections.sort(invokers, new ComparadorPorCantidadTareas()); //Actualizo el orden en la lista.
                }
            }
            exit=false; //Devolvemos la variable exit a su valor inicial
        }
        return listIndexInvoker;
    }
    
    
}
