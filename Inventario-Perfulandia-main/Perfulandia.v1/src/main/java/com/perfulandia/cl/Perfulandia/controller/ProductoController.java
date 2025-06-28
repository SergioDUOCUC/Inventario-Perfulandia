//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.perfulandia.cl.Perfulandia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.Perfulandia.model.Producto;
import com.perfulandia.cl.Perfulandia.services.ProductoService;


//FUNCIONA PARA BUSCAR LOS PRODUCTOS, ELIMINAR EL REGISTRO, BUSCAR POR ID Y CREAR UN NUEVO PRODUCTO

@RestController
@RequestMapping({"/api/v1/productos"})
public class ProductoController {
    @Autowired
    private ProductoService productoservice;
    

    //Listar Productos, [LISTO]
    @GetMapping //Funciona
    public ResponseEntity<List<Producto>> ListarProductos() {
        List<Producto> productos = this.productoservice.buscarTodo();
        if (productos.isEmpty()) {
            System.out.println("No se encontró ningún producto");
            return ResponseEntity.ok().body(List.of());
        } else {
            System.out.println("Se encontraron productos");
            return ResponseEntity.ok(productos);
        }
    }

    @GetMapping("/{id_producto}") //[LISTO]
    public ResponseEntity<?> BuscarPorSku(@PathVariable String id_producto) {
        try {
            System.out.println("ID recibido: " + id_producto); // Para debug
            Producto producto = productoservice.buscarUnProducto2(id_producto);
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra el producto en el inventario.");
            }
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id_producto}") //[LISTO]
    public ResponseEntity<?> eliminarProducto(@PathVariable String id_producto) {
        try {
            productoservice.EliminarProducto2(id_producto);
            return ResponseEntity.ok("Producto eliminado con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto: " + e.getMessage());
        }
    }

    //Copiar el body del GET e insertar nuevo producto
    @PostMapping //[LISTO]
    public ResponseEntity<String> guardarProducto(@RequestBody Producto producto) {
        try {
            Producto productoExistente;
            Producto sky_producto;
            try {
                productoExistente = this.productoservice.buscarUnProducto2(producto.getId_producto());

            } catch (Exception var4) {
                productoExistente = null;
            }

            if (productoExistente == null) {
                if (producto.getId_producto() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID del producto es obligatorio.");
                }
                else {
                    this.productoservice.GuardarProducto(producto);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Producto guardado exitosamente.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El producto con ID " + producto.getId_producto() + " ya existe.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    //http://localhost:8081/api/v1/productos/{ID_PRODUCTO} + body para actualizar lo que se necesite.
    @PutMapping("/{id_producto}") //[LISTO]
    public ResponseEntity<String> actualizarProducto(@PathVariable String id_producto, @RequestBody Producto productoActualizado) {
        try {
            Producto productoExistente = productoservice.buscarUnProducto2(id_producto);
            if (productoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado con ID: " + id_producto);
            }

            // Solo actualiza los atributos si fueron enviados (no nulos o válidos)
            if (productoActualizado.getNombre() != null) productoExistente.setNombre(productoActualizado.getNombre());
            if (productoActualizado.getMarca() != null) productoExistente.setMarca(productoActualizado.getMarca());
            if (productoActualizado.getValor_unitario() > 0) productoExistente.setValor_unitario(productoActualizado.getValor_unitario());
            if (productoActualizado.getSku() != null) productoExistente.setSku(productoActualizado.getSku());
            if (productoActualizado.getGenero() != null) productoExistente.setGenero(productoActualizado.getGenero());
            if (productoActualizado.getMl() > 0) productoExistente.setMl(productoActualizado.getMl());
            if (productoActualizado.getFechaIngreso() != null) productoExistente.setFechaIngreso(productoActualizado.getFechaIngreso());
            if (productoActualizado.getFechaVencimiento() != null) productoExistente.setFechaVencimiento(productoActualizado.getFechaVencimiento());
            if (productoActualizado.getCodigo_barra() > 0) productoExistente.setCodigo_barra(productoActualizado.getCodigo_barra());
            if (productoActualizado.getOrigen() != null) productoExistente.setOrigen(productoActualizado.getOrigen());

            productoservice.GuardarProducto(productoExistente);
            return ResponseEntity.ok("Producto actualizado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el producto: " + e.getMessage());
        }
    }



}
