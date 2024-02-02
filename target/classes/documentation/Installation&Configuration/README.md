## Instalacion y configuracion


####  ****Maven:****
Utilizar el siguiente comando en caso de utilizar Maven.
````cmd
mvn clean package
````

#### ****MV Java:****
````cmd
java -main tumain.class -cp <ruta_a_jar_de_faas>
````

####  ****Importar .jar:****

> Si está compilando el código fuente.
En caso de que se esté importando el .jar añadir al classpath de tu aplicación.<br>
> Verá un .jar del sistema que puede descargarse en el repositorio, [clic aquí para acceder](https://github.com/massinlaaouaj/FaaS-system-framework/).

<br>

### Configuración

> El servidor utiliza el puerto ```12345```, por favor si encuentras que no funciona, 
> en caso de tener un ****SO Windows****, añadir una regla de entrada y salida para el puerto que recibe y envía peticiones el servidor ```12345```, ver aquí [como configurar reglas de entrada y salidas en Windows](https://learn.microsoft.com/en-us/sql/reporting-services/report-server/configure-a-firewall-for-report-server-access?view=sql-server-ver16). <br>
> En caso de utilizar un ****SO Linux****, añadir reglas de entrada y salida para el puerto del servidor en IPTABLES ( consulta la documentación de tu distribución para los detalles )

