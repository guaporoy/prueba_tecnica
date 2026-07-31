package com.example.productos.repositories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Repository;

import com.example.productos.models.Producto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Repository
public class ProductoRepository {

    private static final Path DATA_FILE = Path.of("data", "productos.json");
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final ReentrantLock lock = new ReentrantLock();

    public ProductoRepository() {
        inicializarArchivoSiNoExiste();
    }

    private void inicializarArchivoSiNoExiste() {
        try {
            File file = DATA_FILE.toFile();
            if (!file.exists()) {
                file.getParentFile().mkdirs();

                List<Producto> semilla = new ArrayList<>();
                semilla.add(new Producto(1L, "Queso Paipa", 12500.0, 30, "Lácteos"));

                mapper.writeValue(file, semilla);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el archivo de datos de productos", e);
        }
    }

    private List<Producto> leerTodosDesdeDisco() {
        try {
            Producto[] arreglo = mapper.readValue(DATA_FILE.toFile(), Producto[].class);
            List<Producto> lista = new ArrayList<>();
            for (Producto p : arreglo) {
                lista.add(p);
            }
            return lista;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo de productos", e);
        }
    }

    private void guardarTodosEnDisco(List<Producto> productos) {
        try {
            mapper.writeValue(DATA_FILE.toFile(), productos);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo de productos", e);
        }
    }

    public List<Producto> findAll() {
        lock.lock();
        try {
            return leerTodosDesdeDisco();
        } finally {
            lock.unlock();
        }
    }

    public Optional<Producto> findById(Long id) {
        lock.lock();
        try {
            return leerTodosDesdeDisco().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst();
        } finally {
            lock.unlock();
        }
    }

    public Producto save(Producto producto) {
        lock.lock();
        try {
            List<Producto> productos = leerTodosDesdeDisco();

            if (producto.getId() == null) {
                long siguienteId = productos.stream()
                        .mapToLong(Producto::getId)
                        .max()
                        .orElse(0L) + 1;
                producto.setId(siguienteId);
                productos.add(producto);
            } else {
                boolean actualizado = false;
                for (int i = 0; i < productos.size(); i++) {
                    if (productos.get(i).getId().equals(producto.getId())) {
                        productos.set(i, producto);
                        actualizado = true;
                        break;
                    }
                }
                if (!actualizado) {
                    productos.add(producto);
                }
            }

            guardarTodosEnDisco(productos);
            return producto;
        } finally {
            lock.unlock();
        }
    }

    public boolean deleteById(Long id) {
        lock.lock();
        try {
            List<Producto> productos = leerTodosDesdeDisco();
            boolean eliminado = productos.removeIf(p -> p.getId().equals(id));
            if (eliminado) {
                guardarTodosEnDisco(productos);
            }
            return eliminado;
        } finally {
            lock.unlock();
        }
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
}