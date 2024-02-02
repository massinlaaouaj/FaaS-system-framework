package observer;

import java.util.ArrayList;
import java.util.List;

public class Metrics {
	
	private List<Integer> idInvoker;
	private List<String> taskName;
	private List<Long> timeEllapsed;
	private List<Integer> memoryNeeded;

	/**
	 * Contructor Metrics
	 */
	public Metrics() {
		this.idInvoker = new ArrayList<Integer>();
		this.taskName = new ArrayList<String>();
		this.timeEllapsed = new ArrayList<Long>();
		this.memoryNeeded = new ArrayList<Integer>();
	}

	/**
	 * Asignar valores a las metricas.
	 * @param observable
	 * @param taskName
	 * @param timeEllapsed
	 * @param memoryNeeded
	 */
	public void addMetric(Observable observable, String taskName, long timeEllapsed, int memoryNeeded) {
		this.idInvoker.add(observable.getId());
		this.taskName.add(taskName);
		this.timeEllapsed.add(timeEllapsed);
		this.memoryNeeded.add(memoryNeeded);
	}

	/**
	 * Mostrar metricas y estadísticas.
	 * @return
	 */
	public String showMetrics() {
		
		//Si no hay mtricas disponibles.
		if(idInvoker.size() == 0) {
			System.err.println("No actions were executed so there are not metrics available");
			return "";
		}
		
		StringBuilder str = new StringBuilder();
		
		//Si todas las listas no tienen la misma longitud alog ha ido mal
		if(!(idInvoker.size() == taskName.size() && idInvoker.size() == timeEllapsed.size()
				&& idInvoker.size() == memoryNeeded.size())) {
			System.err.println("Alguna mtrica no fue almacenada correctamente."
					+ " No pueden mostrarse las mtricas");
			return "";
		}
		str.append("---------- METRICS ----------\n");
		for(int i = 0; i < idInvoker.size();i++) {
			str.append("El invoker " + idInvoker.get(i) + " ejecut la tarea " + taskName.get(i) + 
					" con memoria necesaria " + memoryNeeded.get(i) + " en un tiempo de " + timeEllapsed.get(i) + 
					" milisegundos.\n");
		}
		str.append("-------- ESTADISTICAS -------\n");
		str.append(getTimeMaxAction());
		str.append(getTimeMinAction());
		str.append(getTotalMemoryConsumed());
		str.append("-----------------------------");
		
		return str.toString();
	}

	/**
	 * Invoker con mayor tiempo de ejecución.
	 * @return
	 */
	private String getTimeMaxAction() {
		int index = 0;
		long time = -1;
		
		for(int i = 0; i < idInvoker.size(); i++) {
			if(timeEllapsed.get(i) > time) {
				index = i;
				time = timeEllapsed.get(i);
			}
		}
		
		return "El invoker " + idInvoker.get(index) + " obtuvo el mayor tiempo de ejecucin para " +
				"una tarea " + taskName.get(index) + " con un tiempo de " + time + " milisegundos.\n";
		
	}

	/**
	 * Invoker con menor tiempo de ejecución.
	 * @return
	 */
	private String getTimeMinAction() {
		int index = 0;
		long time = Long.MAX_VALUE;
		
		for(int i = 0; i < idInvoker.size(); i++) {
			if(timeEllapsed.get(i) < time) {
				index = i;
				time = timeEllapsed.get(i);
			}
		}
		
		return "El invoker " + idInvoker.get(index) + " obtuvo el menor tiempo de ejecucin para " +
				"una tarea " + taskName.get(index) + " con un tiempo de " + time + " milisegundos.\n";
	}

	/**
	 * Obtener memoria total consumida.
	 * @return
	 */
	private String getTotalMemoryConsumed() {
		int counter = 0;
		for(int memory : memoryNeeded) {
			counter += memory;
		}
		return "La memoria total consumida por nuestro sistema han sido " + counter + " u.\n";
	}

	/* Getters y Setters */

	/**
	 * Obtener lista identificadores de invokers.
	 * @return
	 */
	public List<Integer> getIdInvoker() {
		return this.idInvoker;
	}

	/**
	 * Asignar lista de identificadores de invokers.
	 * @param idInvoker
	 */
	public void setIdInvoker(List<Integer> idInvoker) {
		this.idInvoker = idInvoker;
	}

	/**
	 * Obtener lista de nombres de tareas.
	 * @return
	 */
	public List<String> getTaskName() {
		return this.taskName;
	}

	/**
	 * Asignar lista de nombres de tareas.
	 * @param taskName
	 */
	public void setTaskName(List<String> taskName) {
		this.taskName = taskName;
	}

	/**
	 * Obtener lista de tiempo transcurrido.
	 * @return
	 */
	public List<Long> getTimeEllapsed() {
		return this.timeEllapsed;
	}

	/**
	 * Asignar lista de tiempo transcurrido.
	 * @param timeEllapsed
	 */
	public void setTimeEllapsed(List<Long> timeEllapsed) {
		this.timeEllapsed = timeEllapsed;
	}

	/**
	 * Obtener lista de memoria necesaria para la acción.
	 * @return
	 */
	public List<Integer> getMemoryNeeded() {
		return this.memoryNeeded;
	}

	/**
	 * Asignar lista de memoria necesaria para la acción.
	 * @param memoryNeeded
	 */
	public void setMemoryNeeded(List<Integer> memoryNeeded) {
		this.memoryNeeded = memoryNeeded;
	}
}
