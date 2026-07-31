package com.example.productos.services;

import com.example.productos.models.Producto;
import com.example.productos.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto con id " + id + " no fue encontrado"));
    }

    public Producto crear(Producto producto) {
        validar(producto);
        producto.setId(null); // aseguramos que el id lo asigne el repositorio
        return repository.save(producto);
    }

    public Producto actualizar(Long id, Producto datosActualizados) {
        Producto existente = obtenerPorId(id);
        datosActualizados.setId(existente.getId());
        validar(datosActualizados);
        return repository.save(datosActualizados);
    }

    public void eliminar(Long id) {
        boolean eliminado = repository.deleteById(id);
        if (!eliminado) {
            throw new NoSuchElementException("Producto con id " + id + " no fue encontrado");
        }
    }

    private void validar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getCategoria() == null || producto.getCategoria().isBlank()) {
            throw new IllegalArgumentException("La categoría del producto es obligatoria");
        }
        if (producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

    public static class NoSuchElementException extends RuntimeException {
        public NoSuchElementException(String mensaje) {
            super(mensaje);
        }
    }
}