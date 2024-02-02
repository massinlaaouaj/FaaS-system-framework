package main;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        thread2.start();
		
		/*//---------------------------------	MAIN	2	---------------------------------------------------
		
		
		//MAIN ALTERNATIVO PARA QUE VEAS LAS DISTINTAS COSAS QUE PODEMOS EJECUTAR Y COMO SE EJECUTAN
		//EN NUESTRO SISTEMA faas.
		
		
		
		
		// CREA EL CONTROLLER. ASIGNANDOLE EL NUMERO DE INVOKERS, LA MEMORIA DE CADA UNO, Y LA POLITICA DE ASIGNACION.
		Controller controller1 = new Controller(3,1024, new UniformGroup(3));
		
		
		// CREA LAS ACCIONES. Tenemos que especificar en los <> el tipo del par�metro y el tipo de retorno.
	 	Action<Integer, String> sleep = new SleepAction();
	 	Action<Map<String,Integer>,Integer> add = new AddAction();
	 	
	 	//PUEDES CREAR ACCIONES USANDO DECORATORS, POR EJEMPLO. Tambien podriamos anidar decorators para aplicar ambos.
	 	Action<Map<String,Integer>,Integer> addTimed = new TimerDecorator<>(add);
	 	Action<Map<String,Integer>,Integer> addMemoized = new MemoizationDecorator<>(add);
	 	Action<Map<String,Integer>,Integer> addTimedMemoized = new MemoizationDecorator<>(addTimed);
        // REGISTRA LAS ACCIONES EN EL CONTROLLER. Para registrar una accion tengo que darle a esta un nombre
        // pasarle un objeto de tipo Action el cual vamos a registrar, y una memoria necesaria por la accion.
        controller1.registerAction("sleepAction", sleep, 100);
        controller1.registerAction("addAction", add, 100);
        controller1.registerAction("addTimed", addTimed, 110);
        controller1.registerAction("addMemoized", addMemoized, 110);
        controller1.registerAction("addTimedMemoized", addTimedMemoized, 110);

        // CREA EL PROXY ESPECIFICANDO SI LA TAREA VA A SER AS�NCRONA. (MULTI-HILO) y ADEMAS ESPECIFICAMOS
        // SI SE TRATA DE UNA INVOCACION GRUPAL LO QUE VAMOS A HACER O NO.
        Action<List<Integer>, List<Future<String>>> proxy1 = controller1.createActionProxy("sleepAction", true,true);
        Action<Map<String,Integer>,Integer> proxy2 = controller1.createActionProxy("addTimed", false, false);
        Action<List<Map<String,Integer>>,List<Future<Integer>>> proxy3 = controller1.createActionProxy("addMemoized", true, true);
        Action<List<Map<String,Integer>>,List<Future<Integer>>> proxy4 = controller1.createActionProxy("addTimedMemoized", true, true);
        
        //CREAMOS EL INPUT PARA LAS ACCIONES
        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(1000);
        numbers.add(2000);
        numbers.add(5000);
        numbers.add(10000);
        
        Map<String,Integer> sumElements = Map.of("x",2,"y",7);
        
        
        List<Map<String,Integer>> listaSumElems = new ArrayList<Map<String,Integer>>();
        //EN EL CASO DE QUE ESTOS FUERAN LOS ELEMENTOS A SUMAR, AUNQUE TENGAMOS LA CACHE, EN ESTAS EJECUCIONES
        //EL TIEMPO NO ES 0 YA QUE A LA HORA DE PODER AADIR A LA CACHE, TENEMOS QUE ESPERAR A QUE UNA ACCION
        //QUE PUEDA AFECTARLA CONCLUYA, ENTONCES POR ESO LOS TIEMPOS VAN HACIENDOSE MAS PESADOS COMO PODEMOS VER
        //EN LAS METRICAS DEL CONTROLLER.
        
        
        Map<String,Integer> sumElem1 = Map.of("x",2,"y",7);
        Map<String,Integer> sumElem2 = Map.of("x",3,"y",9);
        Map<String,Integer> sumElem3 = Map.of("x",1,"y",5);
        Map<String,Integer> sumElem4 = Map.of("x",2,"y",7);
        Map<String,Integer> sumElem5 = Map.of("x",7,"y",2);
        Map<String,Integer> sumElem6 = Map.of("x",2,"y",7);
        
        
        //SI EJECUTARAMOS ALGO TAN EXPLICITO Y REPETIDO COMO ESTO, SI QUE PODEMOS VER EN LAS METRICAS EL EFECTO
        //DE HABER AÑADIDO LA CACHE EN UN CORTO PLAZO, Y NO TENEMOS QUE "ESPERAR" NI SE GENERA ESA COLA TAN LARGA
        //DE LAS SUMAS ESPERANDO LA UNA POR LA OTRA A QUE SE COMPLETEN PARA PODER AÑADIR LA INFORMACION A LA CACHE.
        
        //AQUI EL TIEMPO DE TODAS SERA DE 100MS PORQUE UNICAMENTE TODAS ESPERARAN A QUE LA PRIMERA ACCION TERMINE
        //Y AÑADA SU INFORMACION A LA CACHE, Y POSTERIORMENTE TODAS SON EJECUTADAS AL INSTANTE PORQUE EL INPUT
        //ESTA CACHEADO. RECORDEMOS QUE ESTO ES PORQUE ESTAMOS ESTABLECIENDO UN TIEMPO FICTICIO DE 100 MS PARA LA SUMA.
        
        //Map<String,Integer> sumElem1 = Map.of("x",2,"y",7);
        //Map<String,Integer> sumElem2 = Map.of("x",2,"y",7);
        //Map<String,Integer> sumElem3 = Map.of("x",2,"y",7);
        //Map<String,Integer> sumElem4 = Map.of("x",2,"y",7);
        //Map<String,Integer> sumElem5 = Map.of("x",2,"y",7);
        //Map<String,Integer> sumElem6 = Map.of("x",2,"y",7);
        
        
        
        listaSumElems.add(sumElem1);listaSumElems.add(sumElem2);listaSumElems.add(sumElem3);
        listaSumElems.add(sumElem4);listaSumElems.add(sumElem5);listaSumElems.add(sumElem6);
        
        //LE PASAMOS AL PROXY EL INPUT PARA LA ACCION Y LO EJECUTAMOS. ADEMAS LO ALMACENAMOS EN UNA LISTA DE FUTURES
       	//YA QUE AL LA ACCION SER ASINCRONA, MULTI-HILO, SE CREARAN FUTURES PARA ALMACENAR EL RESULTADO
       	//DE LA EJECUCION DE LAS MISMAS. En este caso al ser la invocacion grupal, tendremos una lista de 
       	//futures del tipo que retornase esa accion.
        List<Future<String>> listResult = proxy1.run(numbers);
        
        int resultSum2 = proxy2.run(sumElements);		
        System.out.println("El resultado de la suma es: " + resultSum2);
        
        List<Future<Integer>> listResult3 = proxy3.run(listaSumElems);
        
        List<Future<Integer>> listResult4 = proxy4.run(listaSumElems);
        
        
        //MIENTRAS TODOS LOS FUTURES DE LA LISTA DE FUTURES NO HAYAN COMPLETADO SU EJECUCIN ESPERAMOS.
        
        while(!isFuturesListDone(listResult)) {}
        while(!isFuturesListDone(listResult3)) {}
        while(!isFuturesListDone(listResult4)) {}
        
        //UNA VEZ TERMINE, MUESTRO LAS MÉTRICAS DEL CONTROLLER
         
        controller1.showMetrics();
        
		*/
		
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
				// TODO Auto-generated catch block
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
	
	private static void saveOutputInResources() {
		// Especifica el archivo al que deseas escribir
        String outputFile = "src/resources/output.txt";
        File file = new File(outputFile);

        // Crea un flujo de salida para la consola (System.out)
        OutputStream consoleOutputStream = System.out;

        // Crea un flujo de salida para el archivo
        OutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(file);
			// Combina los flujos de salida (consola y archivo)
	        //OutputStream combinedOutputStream = new TeeOutputStream(consoleOutputStream, fileOutputStream);
	     // Crea un nuevo PrintStream que apunta al flujo combinado
	        //PrintStream printStream = new PrintStream(combinedOutputStream);
	     // Redirige la salida estàndar a nuestro nuevo PrintStream
	        //System.setOut(printStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
