# Sistema CONCYSSA - Control Operativo y Órdenes de Trabajo

Sistema backend RESTful desarrollado para la gestión integral de órdenes de trabajo, control de trazabilidad e historial de mantenimiento en estaciones de bombeo.

## 🚀 Tecnologías
* **Java 17** con **Spring Boot 3**
* **Spring Security** + **JWT** (Autenticación y Autorización basada en roles)
* **Spring Data JPA** / **Hibernate**
* **MySQL** (Persistencia de datos)
* **JUnit 5** + **Mockito** (Pruebas unitarias de servicios)
* **Maven** (Gestión de dependencias)

## 📌 Funcionalidades Principales
* Autenticación segura mediante Tokens JWT.
* Gestión del ciclo de vida de órdenes de trabajo (Creación, Asignación, Cambio de Estado).
* Registro de trazabilidad e historial (`HistorialOrden`) para auditoría de cambios.
* Manejo global de excepciones (`GlobalExceptionHandler`).

## 🛠️ Ejecución Local
```bash
# Clonar el repositorio
git clone [https://github.com/Juancalcifer66/sistemaconcyssa.git](https://github.com/Juancalcifer66/sistemaconcyssa.git)

# Ejecutar pruebas unitarias
./mvnw test

# Iniciar la aplicación
./mvnw spring-boot:run
