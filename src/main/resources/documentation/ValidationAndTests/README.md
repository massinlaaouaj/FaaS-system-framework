## Validacion y testeo

Explicación de los tests unitarios que se han implementado y que prueban cada uno.

### @Test testReflection

Probar la funcionalidad de reflexión del sistema Function as a Service (FaaS), verificando si la suma de 5 y 28 utilizando un objeto Calculator creado con ciertos parámetros y un algoritmo de balanceo de carga RoundRobin da como resultado 33.


`````Java
	@Test
	void testReflection() throws Exception {
		Calculator calculator = new Controller(3,1024, new RoundRobin());
		int result = calculator.add(5, 28);
		assertEquals(33, result);
	}
`````



### @Test testAdd

Testeo de flujo de ejecución de una acción, mediante Proxy. Registramos una acción de suma en el controlador, se crea un proxy para la acción, se ejecuta la acción a través del proxy con ciertos valores de entrada, y se verifica si el resultado es el esperado.


`````Java
	@Test
	void testAdd() throws Exception {
		Controller controller = new Controller(3,1024, new RoundRobin());
		Action addAction = new AddAction();
		controller.registerAction("addAction", addAction, 100);
		Action<Map<String,Integer>, Integer> proxy = 
        controller.createActionProxy("addAction", false, false);
		int result = proxy.run(Map.of("x",5,"y",28));
		assertEquals(33, result);
	}
`````


### @Test testAddGroup

Testeo de flujo de ejecución de un grupo de acción, mediante Proxy. Registramos acciones grupales de acción de suma en el controlador, se crea un proxy para la acción grupal, se ejecuta las acciones a través del proxy con ciertos valores de entrada, y se verifica si el resultado es el esperado para cada acción del grupo.


`````Java
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
`````

### @Test testAddGroup_async

Testeo de flujo de ejecución de un grupo de acción, mediante Proxy. Registramos acciones grupales de acción de suma en el controlador, se crea un proxy para la acción grupal, se ejecuta las acciones a través del proxy con ciertos valores de entrada, y se verifica si el resultado es el esperado para cada acción del grupo, en multi-hilo.


`````Java
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
`````


### @Test testSleepGroup_async

Concurrencia y robustez, del sistema. Si la aplicacion no fuera asincrona, es decir no funcionase correctamente, el tiempo de ejecucion de este test seria de 38 segundos minimo, porque son los sleep que hay. Pues, el test se ejecuta en 20 segundos, por lo que concluimos en que se realiza correctamente, ya que el tiempo iterativo acumulado serian minimo 38. Si acaba en 20 es porque las tareas se hacen concurrentemente.


`````Java
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
`````