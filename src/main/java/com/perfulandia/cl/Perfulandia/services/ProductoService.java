package com.perfulandia.cl.Perfulandia.services;


import com.perfulandia.cl.Perfulandia.model.Producto;
import com.perfulandia.cl.Perfulandia.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productorepository;

    public List<Producto> buscarTodo() {
        return productorepository.findAll();

    }


    public Producto BuscarUnProducto(String id_producto){
        return productorepository.getReferenceById(id_producto);
    }

    public Producto buscarUnProducto2 (String id_producto){
        return productorepository.findById(id_producto).orElse(null);
    }



    public Producto GuardarProducto(Producto productos){
        return productorepository.save(productos);

    }

    public void EliminarProducto2(String id_producto) {
        Producto producto = productorepository.findById(id_producto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (producto.getFechaIngreso() == null || producto.getFechaVencimiento() == null) {
            throw new IllegalStateException("El producto tiene fechas inválidas (null)");
        }

        productorepository.deleteById(id_producto);
    }

   public void EliminarProducto(String id_producto){
        productorepository.deleteById(id_producto);
    }

}

