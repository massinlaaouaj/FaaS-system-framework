package policies;


import java.util.ArrayList;
import java.util.List;

import model.Invoker;
import utils.Utils;

/**
 * Distribuya uniformemente las acciones entre los Invokers que tengan recursos disponibles.
 */
public class RoundRobin implements PolicyManager {

    @Override
    public <T,R> List<Integer> invokeAction
            (List<Invoker> invokers,
             T input, int memoryNeeded) {
        List<Integer> indexInvokersPack1 = new ArrayList<Integer>();

        if(!Utils.isTaskApproachable(invokers, memoryNeeded)) {
            return indexInvokersPack1;
        }

        int indexInvokers = 0;
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

        int cantidadDeTareasRealizables = 0;
        int cantidadTareas = input.size();


        while(cantidadDeTareasRealizables < cantidadTareas){ //  ejecutar todo el grupo a la vez

            cantidadDeTareasRealizables = 0;
            //Recorremos los invokers
            for(Invoker invoker : invokers){
                double memoriaDisponible = invoker.getTotalMemoryAvailable();
                int tareasRealizables = (int) (memoriaDisponible / memoryNeeded);

                cantidadDeTareasRealizables += tareasRealizables;
            }
        }


        int indexInvokers = 0; // <-- Descomentar si uso ROUND ROBIN SECUENCIAL.
        for(int i = 0; i<input.size();i++) {
            while(invokers.get(indexInvokers).getTotalMemoryAvailable() < memoryNeeded ){

                //Pasar al siguiente invoker.
                indexInvokers++;
                //En caso de que me haya salido vuelvo a empezar.
                if(indexInvokers == invokers.size()){
                    indexInvokers = 0;
                }
            }

            indexInvokersPack1.add(indexInvokers);

            indexInvokers++;
            if(indexInvokers == invokers.size()){
                indexInvokers = 0;
            }
        }

        return indexInvokersPack1;

    }

}
