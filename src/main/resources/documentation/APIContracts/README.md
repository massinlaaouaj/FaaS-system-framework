# API Contratos


### Class Request

Se ha diseñado un sistema de comunicación más orientado a graphql que a un sistema api rest. El motivo es que siempre van a utilizar un mismo punto de entrada que recibe una clase request.

La clase Request contiene:

El tipo de la acción que se pretende ejecutar. Equivalente a una query de graphql o equivalente a un endpoint de un servicio rest.
Adicionalmente se le indica si esta debe ser ejecutada de manera asincrónica (isAsync) y la opción "isGroupalInvoke" que indica si la tarea es única o es un grupo de tareas a ejecutar.

Para las respuestas, disponemos de el atributo "response" tipado en función de la acción a ejecutar y "isDone" que indica si la tera se ejecutó correctamente. como indico este "contrato" es único ya que he diseñado el sistema más enfocado a Graphql

<br>

*Ejemplo de una petición asíncrona, grupal, aplicar la función Word Count, sobre un texto:*
````Java
Request request = new Request(wordCountMapAction, textos, true, true);
````

<br>
<hr>

[Diseño de sistema](DesignSystem.md)