# Documentación sistema FaaS

Function-as-a-Service (FaaS) es un paradigma Cloud que permite la ejecución de código
sencillo, llamadas funciones o acciones, en la nube en servidores virtualizados. En un
sistema FaaS, los usuarios pueden registrar, listar, invocar y eliminar acciones. Para
registrar una nueva acción, el usuario debe definir su código, e indicar un identificador y la
cantidad de memoria RAM (en megabytes) que necesita la acción. Tras crear una acción,
ésta se puede invocar mediante el identificador pudiendo indicar además unos parámetros
de entrada.

## Descripción del proyecto

Modelar con clases un sistema FaaS completo que permita la ejecución de
funciones de manera adaptativa a los recursos disponibles. El proyecto se basa en la
arquitectura de OpenWhisk, un sistema de funciones de código abierto creado por IBM,
usado internamente como servicio en IBM Cloud.

En OpenWhisk, la arquitectura está principalmente formada por dos componentes: Un
Controller y múltiples Invokers.

- **El Controller** es responsable de recibir las peticiones de invocación de los usuarios
y seleccionar los Invokers que ejecutarán las acciones. El controller es único, y tiene
una vista global de los Invokers disponibles y los recursos libres de cada uno.


- **El Invoker** recibe las órdenes de ejecución de acciones del Controller, y es el
responsable de reservar los recursos (memoria) necesarios y ejecutar el código de
las acciones. Para simular la reserva de memoria, indicaremos al Invoker la cantidad
total de memoria RAM en megabytes en un atributo, que iremos modificando a
medida que se le asignan acciones. El número de invokers es constante durante
toda la ejecución del programa y se indica al inicializar el programa.

![faas-architecture](https://i.ibb.co/pjKF6ph/Capture.png)

<br>
<hr>

## Indice de contenido

<br>

- #### [APIs Contratos](src/main/resources/documentation/APIContracts/README.md)
- #### [Puntos de extensión y ciclo de vida](src/main/resources/documentation/ExtensionPointsAndLiveCycle/README.md)
- #### [Validation y Tests](src/main/resources/documentation/ValidationAndTests/README.md)
- #### [Instalacion y configuracion](src/main/resources/documentation/Installation&Configuration/README.md)
<!-- - #### [Ejemplos](src/main/resources/documentation/Examples/README.md) -->
- #### [Ejemplos](src/main/java/main/Main.java)
- #### [Javadoc del proyecto](src/main/resources/javadoc)