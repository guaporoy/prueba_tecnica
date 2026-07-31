

¿Por qué elegí este stack?

Elegí **Java 17 + Spring Boot** porque es el stack que estoy usando en mi formación como Ingeniero de Sistemas 

Para la persistencia decidí usar un **archivo JSON** en lugar de una base de datos relacional (MySQL, PostgreSQL, etc.). La razón es puramente de alcance: el objetivo del ejercicio es demostrar el CRUD y la organización del proyecto. Un archivo JSON permite que cualquier persona clone el proyecto y lo ejecute con un solo comando (`mvn spring-boot:run`), sin instalar ni configurar nada adicional. Si el proyecto creciera o necesitara concurrencia real, el siguiente paso natural sería migrar el `ProductoRepository` a JPA + una base de datos, manteniendo intacto el resto de las capas (servicio y controlador), ya que la lógica de negocio no depende de cómo se guardan los datos.

Para el frontend usé **HTML, CSS y JavaScript puro**, servido como archivos estáticos por el propio Spring Boot. Esto evita añadir un segundo proceso de build (Node, npm) y mantiene el proyecto en un solo repositorio y un solo comando de arranque, lo cual es coherente con la simplicidad que pedía el ejercicio.

¿Cómo organicé las carpetas?

Separé el backend en capas siguiendo el patrón estándar de Spring Boot:

`models`**: contiene la clase `Producto`, que representa la entidad del dominio (nombre, precio, stock, categoría).

`repositories`**: contiene `ProductoRepository`, responsable únicamente de leer y escribir el archivo `productos.json`. Es la única capa que "sabe" que los datos viven en un archivo.

`services`**: contiene `ProductoService`, donde está la lógica de negocio y las validaciones (por ejemplo, que el precio y el stock no sean negativos). Esta capa no sabe nada sobre HTTP ni sobre archivos, solo aplica reglas.

`controllers`**: contiene `ProductoController`, que expone los endpoints REST (`GET`, `POST`, `PUT`, `DELETE`) y traduce las peticiones HTTP en llamadas al servicio.

Esta separación tiene una razón concreta: donde haga un cambio solo me voy al domulo y lo hago  y no hacer el cambio en todo

El frontend quedó aparte, en `src/main/resources/static`, que es la carpeta que Spring Boot sirve automáticamente como contenido estático, evitando tener que configurar un servidor web adicional.

 Retos que tuve que resolver

**Evitar condiciones de carrera al escribir el archivo JSON**: como varias peticiones podrían llegar casi al mismo tiempo.

**Generar IDs de forma consistente sin base de datos**: al no tener un motor que autogenere IDs (como haría una base de datos con `AUTO_INCREMENT`), tuve que calcular el siguiente ID manualmente, tomando el máximo ID existente y sumando uno, cada vez que se crea un producto nuevo.

**Separar validación de persistencia**: al principio tenía la tentación de validar los datos directamente en el controlador, pero eso mezclaba la lógica HTTP con las reglas de negocio. Moví toda la validación (`nombre` y `categoría` no vacíos, `precio` y `stock` no negativos) al `ProductoService`, y usé manejadores de excepciones (`@ExceptionHandler`) en el controlador para traducir esos errores de negocio en respuestas HTTP claras (`400` para datos inválidos, `404` para productos que no existen).

**Configuración de Git y control de versiones**: durante el desarrollo tuve que ajustar el `.gitignore` para que la carpeta `target/` (generada automáticamente por Maven en cada compilación) no quedara versionada en el repositorio, ya que no aporta nada al código fuente y ensucia el historial de cambios.
