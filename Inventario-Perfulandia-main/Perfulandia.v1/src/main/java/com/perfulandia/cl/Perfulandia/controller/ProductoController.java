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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoservice;

    @Operation(summary = "Listar todos los productos", description = "Obtiene la lista completa de productos registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente"),
    })
    @GetMapping
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

    @Operation(summary = "Buscar producto por ID", description = "Obtiene un producto específico por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id_producto}")
    public ResponseEntity<?> BuscarPorSku(
            @Parameter(description = "ID del producto a buscar", required = true) @PathVariable String id_producto) {
        try {
            System.out.println("ID recibido: " + id_producto);
            Producto producto = productoservice.buscarUnProducto2(id_producto);
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encuentra el producto en el inventario.");
            }
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar producto por ID", description = "Elimina un producto específico por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id_producto}")
    public ResponseEntity<?> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar", required = true) @PathVariable String id_producto) {
        try {
            productoservice.EliminarProducto2(id_producto);
            return ResponseEntity.ok("Producto eliminado con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el producto: " + e.getMessage());
        }
    }

    @Operation(summary = "Crear un nuevo producto", description = "Guarda un nuevo producto en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto guardado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID del producto es obligatorio"),
            @ApiResponse(responseCode = "409", description = "Producto con ese ID ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<String> guardarProducto(
            @Parameter(description = "Datos del producto a guardar", required = true) @RequestBody Producto producto) {
        try {
            Producto productoExistente;
            try {
                productoExistente = this.productoservice.buscarUnProducto2(producto.getId_producto());
            } catch (Exception var4) {
                productoExistente = null;
            }

            if (productoExistente == null) {
                if (producto.getId_producto() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID del producto es obligatorio.");
                } else {
                    this.productoservice.GuardarProducto(producto);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Producto guardado exitosamente.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("El producto con ID " + producto.getId_producto() + " ya existe.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id_producto}")
    public ResponseEntity<String> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", required = true) @PathVariable String id_producto,
            @Parameter(description = "Datos actualizados del producto", required = true) @RequestBody Producto productoActualizado) {
        try {
            Producto productoExistente = productoservice.buscarUnProducto2(id_producto);
            if (productoExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Producto no encontrado con ID: " + id_producto);
            }

            if (productoActualizado.getNombre() != null)
                productoExistente.setNombre(productoActualizado.getNombre());
            if (productoActualizado.getMarca() != null)
                productoExistente.setMarca(productoActualizado.getMarca());
            if (productoActualizado.getValor_unitario() > 0)
                productoExistente.setValor_unitario(productoActualizado.getValor_unitario());
            if (productoActualizado.getSku() != null)
                productoExistente.setSku(productoActualizado.getSku());
            if (productoActualizado.getGenero() != null)
                productoExistente.setGenero(productoActualizado.getGenero());
            if (productoActualizado.getMl() > 0)
                productoExistente.setMl(productoActualizado.getMl());
            if (productoActualizado.getFechaIngreso() != null)
                productoExistente.setFechaIngreso(productoActualizado.getFechaIngreso());
            if (productoActualizado.getFechaVencimiento() != null)
                productoExistente.setFechaVencimiento(productoActualizado.getFechaVencimiento());
            if (productoActualizado.getCodigo_barra() > 0)
                productoExistente.setCodigo_barra(productoActualizado.getCodigo_barra());
            if (productoActualizado.getOrigen() != null)
                productoExistente.setOrigen(productoActualizado.getOrigen());

            productoservice.GuardarProducto(productoExistente);
            return ResponseEntity.ok("Producto actualizado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el producto: " + e.getMessage());
        }
    }
}
