# EventGO-react

## TFM Máster en Ingeniería Informática

### Plataforma de Gestión de Eventos con React

Para implementar la plataforma de asistencia a la gestión de eventos se plantea el desarrollo de una aplicación web con dos elementos principales: un back-end responsable del almacenamiento de los datos y de la implementación de la lógica de la aplicación desarrollado en Java, haciendo uso de las funcionalidades de Spring Framework y un front-end de tipo Single page application o SPA empleando la librería de JavaScript React.

A mayores, se adjunta en la ruta /documentacion la memoria del proyecto y un archivo JSON TFM.postman.json con las peticiones HTTP del back-end. Además, en el repositorio EventGo podrá encontrar el Api Rest del back-end con el proceso de puesta en marcha.

---

### Manual de instalación

Para la puesta en marcha de la aplicación, será necesario contar con un gestor de bases de
datos MySQL, donde estarán almacenados los datos del sistema. En este trabajo, se ha
tomado la decisión de hacer uso de WorkBench como herramienta visual para la gestión
de los datos. Como paso inicial es necesario la creación de la base de datos y un usuario
con permisos. Para ello se puede hacer uso de los siguientes comandos:

```
create user root@localhost identified by “root”; 
create database eventgo; 
grant all privileges on eventgo.* to root@localhost; 
```

Como siguiente paso es necesario poner en funcionamiento el Backend del proyecto. Para
poder realizar este paso es necesario contar con una versión de JDK igual o superior a 11.
Para la puesta en marcha del Backend, se puede hacer uso de los siguiente comandos
Maven, una vez situados en el directorio bases de nuestro proyecto.

```
mvn clean 
mvn spring-boot:run 
```

---

Trabajo realizado por: Martín Gil Blanco
