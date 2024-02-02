## Puntos de extensión y ciclo de vida

### Implementación de nuevas funciones

Para implementar una nueva función en el sistema se debe implementar cumplir con la interfaz ```Action``` 
con sus contratos.

#### Interface Action<T,R>

La interfaz ```Action``` es una representación de una acción que consta de dos tipos: ```T``` (tipo del parámetro de entrada) y ```R``` (tipo del valor de retorno).

| Métodos     | Descripción                                                                          |
|-------------|--------------------------------------------------------------------------------------|
| **run**     | Este método representa la ejecución de la tarea en la acción o función seleccionada. |
| **getName** | Obtención del nombre función/acción                                                  |

<br>

![Interface Action](https://i.imgur.com/W20dxYJ.png)

<br>
<br>

### Implementación de nuevas políticas de gestión de recursos

Para implementar una nueva función en el sistema se debe cumplir con la interfaz ```PolicyManager```
con sus contratos.

#### Interface PolicyManager

La interfaz "PolicyManager" se utiliza para gestionar políticas de ejecución de acciones.

| Métodos                       | Descripción                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|-------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **invokeAction**              | Este método se utiliza para ejecutar una acción con la política seleccionada utilizando hilos. Toma como parámetros una lista de objetos ```Invoker``` que representan los invocadores de la acción, un objeto "T" que representa los parámetros necesarios según el tipo de acción a ejecutar, y un entero "memoryNeeded" que representa la memoria necesaria para la acción a ejecutar. Devuelve una lista de enteros, que representa a los invokers a invocar para la ejecució de la/s tarea/s.                     |
| **invokeAction (sobrecarga)** | Este método se utiliza para ejecutar múltiples acciones con la política seleccionada utilizando hilos. Toma como parámetros una lista de objetos ```Invoker``` que representan los invocadores de la acción, una lista de objetos "T" que representan los parámetros necesarios según el tipo de acción a ejecutar, y un entero "memoryNeeded" que representa la memoria necesaria para la acción a ejecutar. Devuelve una lista de enteros, que representa a los invokers a invocar para la ejecució de la/s tarea/s. |


![Interface PolicyManager](https://i.imgur.com/u2oPucG.png)

<br>

### Ciclo de vida 

El sistema està pensado para enviar y recibir peticiones de un servidor, 
cumpliendo los contratos establecidos en el ```Request```, las peticiones son interceptadas en 
tiempo de ejecución, por el proxy dinamico, el cuál procesa los contratos e interviene entre la petición y
la respuesta, que envía de vuelta el Controller ejecutando la acción, antes aplicando la política de asignación de recursos/tareas a los 
diferentes invocadores del sistema pre-establecido.

![Life Cycle](https://i.imgur.com/ugqHIEj.png)


