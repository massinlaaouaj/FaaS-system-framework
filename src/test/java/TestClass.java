import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import actions.Action;
import actions.AddAction;
import actions.SleepAction;
import model.Controller;
import policies.RoundRobin;
import reflect.Calculator;

class TestClass {

	@Test
	void testReflection() throws Exception {
		Calculator calculator = new Controller(3,1024, new RoundRobin());
		int result = calculator.add(5, 28);
		assertEquals(33, result);
	}
	
	@Test
	void testAdd() throws Exception {
		Controller controller = new Controller(3,1024, new RoundRobin());
		Action addAction = new AddAction();
		controller.registerAction("addAction", addAction, 100);
		Action<Map<String,Integer>, Integer> proxy = controller.createActionProxy("addAction", false, false);
		int result = proxy.run(Map.of("x",5,"y",28));
		assertEquals(33, result);
	}
	
	@Test
	void testAddGroup() throws Exception {
		Controller controller = new Controller(3,1024, new RoundRobin());
		Action addAction = new AddAction();
		controller.registerAction("addAction", addAction, 100);
		
		List<Map<String,Integer>> list = new ArrayList<>();
		list.add(Map.of("x",12,"y",25));
		list.add(Map.of("x",2,"y",5));
		list.add(Map.of("x",4,"y",265));
		list.add(Map.of("x",9,"y",218));
		
		
		Action<List<Map<String,Integer>>, List<Integer>> proxy = controller.createActionProxy("addAction", false, true);
		List<Integer> result = proxy.run(list);
		assertEquals(37, result.get(0));
		assertEquals(7, result.get(1));
		assertEquals(269, result.get(2));
		assertEquals(227, result.get(3));
	}
	
	@Test
	void testAddGroup_async() throws Exception {
		Controller controller = new Controller(3,1024, new RoundRobin());
		Action addAction = new AddAction();
		controller.registerAction("addAction", addAction, 100);
		
		List<Map<String,Integer>> list = new ArrayList<>();
		list.add(Map.of("x",12,"y",25));
		list.add(Map.of("x",2,"y",5));
		list.add(Map.of("x",4,"y",265));
		list.add(Map.of("x",9,"y",218));
		
		
		Action<List<Map<String,Integer>>, List<Integer>> proxy = controller.createActionProxy("addAction", true, true);
		List<Integer> result = proxy.run(list);
		assertEquals(37, result.get(0));
		assertEquals(7, result.get(1));
		assertEquals(269, result.get(2));
		assertEquals(227, result.get(3));
	}
	
	/**
	 * Si la aplicacion no fuera asincrona, es decir no funcionase correctamente,
	 * el tiempo de ejecucion de este test seria de 38 segundos minimo, porque son los sleep
	 * que hay. Pues bien, el test se ejecuta en 20 segundos, por lo que concluimos en que
	 * se realiza correctamente, ya que el tiempo iterativo acumulado serian minimo 38.
	 * Si acaba en 20 es porque las tareas se hacen CONCURRENTEMENTE.
	 */
	@Test
	void testSleepGroup_async() throws Exception {
		Controller controller = new Controller(3,1024, new RoundRobin());
		Action sleepAction = new SleepAction();
		controller.registerAction("sleepAction", sleepAction, 100);
		
		List<Integer> list = new ArrayList<>();
		list.add(1000);
		list.add(2000);
		list.add(5000);
		list.add(10000);
		list.add(20000);
		
		
		Action<List<Integer>, List<String>> proxy = controller.createActionProxy("sleepAction", true, true);
		List<String> result = proxy.run(list);
		assertEquals("Done!", result.get(0));
		assertEquals("Done!", result.get(1));
		assertEquals("Done!", result.get(2));
		assertEquals("Done!", result.get(3));
		assertEquals("Done!", result.get(3));
	}
}
