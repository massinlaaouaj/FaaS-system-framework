# API Contratos

### Class Controller

La clase ```Controller``` es la interfaz pública que permite a los usuarios interactuar con el sistema "Function as a Service".
La clase ```Controller``` utiliza las clases ```Invoker```, ```InvokerAction```, ```PolicyManager```, ```Metrics``` y ```ActionProxyInvocationHandler``` para la gestión de los invokers, las acciones, las políticas de ejecución y otras funcionalidades del sistema.


| Métodos                             | Descripción                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
|-------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Controller**                      | El constructor de la clase ```Controller``` recibe como parámetros el número de invokers a crear, la memoria asignada a cada invoker y un objeto ```PolicyManager``` que se encarga de gestionar las políticas de ejecución. En el constructor, se inicializan las listas de invokers y acciones, se registra una acción por defecto y se configuran los invokers.                                                                                                         |
| **registerAction**                  | Este método permite registrar una nueva acción en el sistema. Recibe como parámetros el nombre de la acción, un objeto ```Action``` que representa la implementación de la acción y la cantidad de memoria necesaria para ejecutarla.                                                                                                                                                                                                                                      |
| **invokeAction**                    | Este método se encarga de ejecutar una acción en el sistema. Recibe como parámetros el nombre de la acción y el input necesario para la ejecución. Dependiendo de la política de ejecución configurada en el ```PolicyManager```, se selecciona uno o varios invokers para ejecutar la acción. El resultado de la ejecución se devuelve como resultado del método.                                                                                                         |
| **invokeAction_async**              | Este método es similar a invokeAction, pero permite ejecutar la acción de forma asíncrona, es decir, en un hilo separado. El resultado de la ejecución se devuelve como un objeto Future, que permite obtener el resultado cuando esté disponible.                                                                                                                                                                                                                         |
| **invokeAction (sobrecarga)**       | Esta versión del método invokeAction permite ejecutar una lista de acciones en paralelo. Recibe como parámetros el nombre de la acción y una lista de inputs necesarios para la ejecución. Se seleccionan los invokers correspondientes para cada acción y se ejecutan en paralelo. El resultado de cada ejecución se devuelve como una lista de resultados.                                                                                                               |
| **invokeAction_async (sobrecarga)** | Esta versión del método invokeAction_async permite ejecutar una lista de acciones en paralelo de forma asíncrona. Recibe como parámetros el nombre de la acción y una lista de inputs necesarios para la ejecución. Se seleccionan los invokers correspondientes para cada acción y se ejecutan en paralelo en hilos separados. El resultado de cada ejecución se devuelve como una lista de objetos Future, que permiten obtener los resultados cuando estén disponibles. |
| **createActionProxy**               | Este método permite crear un proxy dinámico para una acción. Recibe como parámetros el nombre de la acción, indicadores de si la ejecución es asíncrona y si se trata de una invocación grupal. El proxy se encarga de ejecutar la acción y gestionar su ejecución.                                                                                                                                                                                                        |
| **update**                          | Este método implementa la interfaz ```Observer``` y se ejecuta cuando un invoker notifica sobre la finalización de una tarea. Actualiza las métricas del sistema con la información proporcionada.                                                                                                                                                                                                                                                                         |
| **showMetrics**                     | Este método muestra las métricas del sistema.                                                                                                                                                                                                                                                                                                                                                                                                                              |
| **add**                             | Este método implementa la interfaz Calculator y realiza una suma de dos números enteros.                                                                                                                                                                                                                                                                                                                                                                                   |
| **Getters y Setters**               | Getters y setters para cada atributo.                                                                                                                                                                                                                                                                                                                                                                                                                                      |

<br>

### Interface Action<T,R>

La interfaz ```Action``` es una representación de una acción que consta de dos tipos: ```T``` (tipo del parámetro de entrada) y ```R``` (tipo del valor de retorno).

| Métodos     | Descripción                                                                          |
|-------------|--------------------------------------------------------------------------------------|
| **run**     | Este método representa la ejecución de la tarea en la acción o función seleccionada. |
| **getName** | Obtención del nombre función/acción                                                  |

<br>

### Interface PolicyManager

La interfaz "PolicyManager" se utiliza para gestionar políticas de ejecución de acciones.

| Métodos                       | Descripción                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|-------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **invokeAction**              | Este método se utiliza para ejecutar una acción con la política seleccionada utilizando hilos. Toma como parámetros una lista de objetos ```Invoker``` que representan los invocadores de la acción, un objeto "T" que representa los parámetros necesarios según el tipo de acción a ejecutar, y un entero "memoryNeeded" que representa la memoria necesaria para la acción a ejecutar. Devuelve una lista de enteros, que representa a los invokers a invocar para la ejecució de la/s tarea/s.                     |
| **invokeAction (sobrecarga)** | Este método se utiliza para ejecutar múltiples acciones con la política seleccionada utilizando hilos. Toma como parámetros una lista de objetos ```Invoker``` que representan los invocadores de la acción, una lista de objetos "T" que representan los parámetros necesarios según el tipo de acción a ejecutar, y un entero "memoryNeeded" que representa la memoria necesaria para la acción a ejecutar. Devuelve una lista de enteros, que representa a los invokers a invocar para la ejecució de la/s tarea/s. |

### Interface Observer

La interfaz ```Observer``` se utiliza para representar objetos que desean ser notificados de cambios en un objeto observado. 

| Métodos      | Descripción                                                                                                                                                            |
|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **taskName** | Representa el nombre de la tarea, un long "executionTime" que representa el tiempo de ejecución y un entero "memoryNeeded" que representa la memoria necesaria.        |
| **update**   | Este método se utiliza para notificar cuando el estado del objeto observado cambia. Toma como parámetros un objeto ```Observable``` que representa el objeto observado |

<br>

### Interface Observable

La interfaz ```Observable``` se utiliza para representar objetos que pueden estar suscritos a otros objetos y recibir notificaciones de cambios.

| Métodos                 | Descripción                                                                                                                                                                                                                                                                          |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **subscribeObserver**   | Este método se utiliza para suscribir un objeto a otro objeto. Toma como parámetro un objeto ```Observer``` que representa el objeto a suscribir.                                                                                                                                    |
| **unsubscribeObserver** | Este método se utiliza para desuscribir un objeto de otro objeto. Toma como parámetro un objeto ```Observer``` que representa el objeto a desuscribir.                                                                                                                               |
| **notifyObserver**      | Este método se utiliza para notificar cambios a los objetos suscritos. Toma como parámetros un String "taskName" que representa el nombre de la tarea, un long "executionTime" que representa el tiempo de ejecución y un entero "memoryNeeded" que representa la memoria necesaria. |
| **getId**               | Este método se utiliza para obtener el identificador del invoker.                                                                                                                                                                                                                    |

