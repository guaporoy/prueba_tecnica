CRUD de Productos

Sistema simple de gestión de productos (nombre, precio, stock y categoría), construido en **Java 17 + Spring Boot**, con persistencia en un archivo JSON (`data/productos.json`) para mantener el proyecto liviano y fácil de correr sin necesidad de instalar una base de datos.

Incluye backend (API REST) y un frontend en HTML/CSS/JS servido por el propio servidor de Spring Boot.

Estructura del proyecto

```
producto-crud/
├── pom.xml
├── data/
│   └── productos.json          # archivo donde se guardan los productos
└── src/main/
    ├── java/com/example/productos/
    │   ├── ProductosApplication.java
    │   ├── models/
    │   │   └── Producto.java
    │   ├── repositories/
    │   │   └── ProductoRepository.java   # persistencia en JSON
    │   ├── services/
    │   │   └── ProductoService.java      # validaciones y reglas de negocio
    │   └── controllers/
    │       └── ProductoController.java   # endpoints REST
    └── resources/
        ├── application.properties
        └── static/
            ├── index.html
            ├── style.css
            └── app.js
```

requisistos 

 Java 17 o superior
 Maven 3.8+

intalacion del proyecto

1.Clona o descarga el proyecto y entra a la carpeta:
   ```bash
   cd producto-crud
   ```

2.Ejecuta la aplicación con Maven:
   ```bash
   mvn spring-boot:run
   ```

   (Si prefieres empaquetar primero: `mvn clean package` y luego `java -jar target/producto-crud-1.0.0.jar`)

3.Abre tu navegador en:
   ```
   http://localhost:8080
   ```

Al iniciar por primera vez, el sistema crea automáticamente `data/productos.json` con un producto de ejemplo (un Queso Paipa, para que veas cómo luce un registro real desde el primer momento).

Endpoints de la API

| Método | Ruta                  | Descripción                  |
|--------|-----------------------|-------------------------------|
| GET    | /api/productos        | Lista todos los productos     |
| GET    | /api/productos/{id}   | Obtiene un producto por id    |
| POST   | /api/productos        | Crea un producto nuevo        |
| PUT    | /api/productos/{id}   | Actualiza un producto existente |
| DELETE | /api/productos/{id}   | Elimina un producto           |


Validaciones incluidas

 El nombre y la categoría no pueden estar vacíos.
-El precio no puede ser negativo.
-El stock no puede ser negativo.
-Si intentas leer, actualizar o eliminar un producto que no existe, la API responde `404` con un mensaje de error.

Dificultad
 pues esta un poco complejo ya que habia cosas que se me olvidaron "como un postre a sus medidas perfectas queda sabroso" 
