package com.perfulandia.cl.Perfulandia.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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

import com.perfulandia.cl.Perfulandia.assemblers.ProductoModelAssembler;
import com.perfulandia.cl.Perfulandia.model.Producto;
import com.perfulandia.cl.Perfulandia.services.ProductoService;

@RestController
@RequestMapping("/api/v2/productos")
public class ProductoControllerV2 {

    @Autowired
    private ProductoService productoservice;

    @Autowired
    private ProductoModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listarProductos() {
        List<Producto> productos = productoservice.buscarTodo();

        if (productos.isEmpty()) {
            return ResponseEntity.ok(
                    CollectionModel.of(List.of(),
                            linkTo(methodOn(ProductoControllerV2.class).listarProductos()).withSelfRel()));
        } else {
            List<EntityModel<Producto>> productosModel = productos.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(
                    CollectionModel.of(productosModel,
                            linkTo(methodOn(ProductoControllerV2.class).listarProductos()).withSelfRel()));
        }
    }

    @GetMapping("/{id_producto}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id_producto) {
        try {
            Producto producto = productoservice.buscarUnProducto2(id_producto);
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encuentra el producto en el inventario.");
            }

            EntityModel<Producto> productoModel = assembler.toModel(producto);
            return ResponseEntity.ok(productoModel);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id_producto}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String id_producto) {
        try {
            productoservice.EliminarProducto2(id_producto);

            EntityModel<String> respuesta = EntityModel.of(
                    "Producto eliminado con éxito.",
                    linkTo(methodOn(ProductoControllerV2.class).listarProductos()).withRel("productos"));

            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el producto: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> guardarProducto(@RequestBody Producto producto) {
        try {
            Producto productoExistente;
            try {
                productoExistente = productoservice.buscarUnProducto2(producto.getId_producto());
            } catch (Exception ex) {
                productoExistente = null;
            }

            if (productoExistente == null) {
                if (producto.getId_producto() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("El ID del producto es obligatorio.");
                }

                productoservice.GuardarProducto(producto);

                EntityModel<Producto> productoModel = assembler.toModel(producto);
                return ResponseEntity.created(
                        linkTo(methodOn(ProductoControllerV2.class).buscarPorId(producto.getId_producto())).toUri())
                        .body(productoModel);

            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("El producto con ID " + producto.getId_producto() + " ya existe.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @PutMapping("/{id_producto}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String id_producto,
            @RequestBody Producto productoActualizado) {
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

            EntityModel<Producto> productoModel = assembler.toModel(productoExistente);
            return ResponseEntity.ok(productoModel);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el producto: " + e.getMessage());
        }
    }

}
