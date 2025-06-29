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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.Perfulandia.model.Producto;
import com.perfulandia.cl.Perfulandia.model.StockSucursal;
import com.perfulandia.cl.Perfulandia.model.StockSucursalDTO;
import com.perfulandia.cl.Perfulandia.services.StockSucursalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/stock")
public class StockSucursalController {

    @Autowired
    private StockSucursalService stockService;

    @GetMapping
    @Operation(summary = "Obtener todos los registros de stock", description = "Retorna todos los registros de stock disponibles en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de stock obtenida correctamente", content = @Content(mediaType = "application/json"))
    public List<StockSucursal> getAllStock() {
        return stockService.getAll();
    }

    @GetMapping("/por-id-producto")
    @Operation(summary = "Buscar stock por ID de producto", description = "Obtiene el stock de un producto específico según su ID.")
    public StockSucursal buscarPorIdProducto(@RequestParam String idProducto) {
        return stockService.buscarPorIdProducto(idProducto);
    }

    @GetMapping("/producto/{id}")
    @Operation(summary = "Obtener stock por ID de producto", description = "Retorna el stock asociado a un producto específico.")
    public ResponseEntity<StockSucursal> getStockByProducto(@PathVariable("id") String idProducto) {
        StockSucursal stock = stockService.obtenerStockPorProducto(idProducto);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener stock por ID de stock", description = "Busca un registro de stock según su ID único.")
    public ResponseEntity<StockSucursal> getStockById(@PathVariable("id") String id) {
        StockSucursal stock = stockService.getById(id);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/bajo/{umbral}")
    @Operation(summary = "Obtener stock bajo umbral", description = "Retorna todos los productos cuyo stock sea menor o igual al umbral especificado.")
    public List<StockSucursal> getStockBajoUmbral(@PathVariable Integer umbral) {
        return stockService.getStocksBajoUmbral(umbral);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo registro de stock", description = "Agrega un nuevo registro de stock con información del producto y sucursal.")
    public StockSucursal createStock(@RequestBody StockSucursalDTO dto) {
        StockSucursal nuevoStock = new StockSucursal();
        nuevoStock.setCantidad(dto.getCantidad());
        nuevoStock.setId_sucursal(dto.getId_sucursal());

        Producto producto = new Producto();
        producto.setId_producto(dto.getProducto().getId_producto());
        nuevoStock.setProducto(producto);

        StockSucursal creado = stockService.create(nuevoStock);
        return creado;
    }

    @PutMapping("/{id}/cantidad")
    @Operation(summary = "Restar cantidad por venta", description = "Descuenta unidades vendidas del stock actual para un producto dado.")
    public ResponseEntity<?> restaCantidad_venta(
            @PathVariable String id,
            @RequestParam Integer cantidadVendida) {
        try {
            StockSucursal stockActual = stockService.getById(id);
            if (stockActual == null) {
                return ResponseEntity.notFound().build();
            }
            if (cantidadVendida > stockActual.getCantidad()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se puede vender más de lo disponible. Stock actual: " + stockActual.getCantidad());
            }
            int nuevaCantidad = stockActual.getCantidad() - cantidadVendida;
            stockActual.setCantidad(nuevaCantidad);
            StockSucursal actualizado = stockService.create(stockActual);
            if (nuevaCantidad <= 5) {
                String alerta = "¡Atención! Solo quedan " + nuevaCantidad + " unidades del producto.";
                return ResponseEntity.ok().body(new StringBuilder()
                        .append("Stock actualizado:\n")
                        .append("ID: ").append(actualizado.getId_stock()).append("\n")
                        .append("Producto: ").append(actualizado.getProducto().getNombre()).append(" (").append(actualizado.getProducto().getId_producto()).append(")\n")
                        .append("Sucursal: ").append(actualizado.getId_sucursal()).append("\n")
                        .append("Cantidad: ").append(actualizado.getCantidad()).append("\n\n")
                        .append(alerta)
                        .toString());
            }
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/agregar")
    @Operation(summary = "Agregar cantidad al stock", description = "Aumenta el stock de un producto dado agregando nuevas unidades.")
    public ResponseEntity<?> agregarCantidad(
            @PathVariable String id,
            @RequestParam Integer cantidadAgregada) {
        try {
            StockSucursal stockActual = stockService.getById(id);
            if (stockActual == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el stock con ID: " + id);
            }
            int nuevaCantidad = stockActual.getCantidad() + cantidadAgregada;
            stockActual.setCantidad(nuevaCantidad);
            StockSucursal actualizado = stockService.create(stockActual);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro de stock", description = "Elimina un registro de stock a partir de su ID.")
    public ResponseEntity<String> deleteStock(@PathVariable String id) {
        try {
            StockSucursal stock = stockService.getById(id);
            if (stock == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró el stock con ID: " + id);
            }
            stockService.delete(id);
            return ResponseEntity.ok("Stock con ID " + id + " eliminado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al intentar eliminar el stock con ID: " + id);
        }
    }

}
