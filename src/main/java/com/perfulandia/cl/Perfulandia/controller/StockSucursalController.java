package com.perfulandia.cl.Perfulandia.controller;


import com.perfulandia.cl.Perfulandia.model.Producto;
import com.perfulandia.cl.Perfulandia.model.StockSucursal;
import com.perfulandia.cl.Perfulandia.model.StockSucursalDTO;
import com.perfulandia.cl.Perfulandia.services.StockSucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockSucursalController {

    @Autowired
    private StockSucursalService stockService;

    // GET /api/stock → obtener todos
    @GetMapping
    public List<StockSucursal> getAllStock() {
        return stockService.getAll();
    }


    // Buscar stock por ID de producto 	http://localhost:8080/api/stock/por-id-producto?idProducto=P1
    @GetMapping("/por-id-producto")
    public StockSucursal buscarPorIdProducto(@RequestParam String idProducto) {
        return stockService.buscarPorIdProducto(idProducto);
    }

    // http://localhost:8080/api/stock/producto/P1
    @GetMapping("/producto/{id}")
    public ResponseEntity<StockSucursal> getStockByProducto(@PathVariable("id") String idProducto) {
        StockSucursal stock = stockService.obtenerStockPorProducto(idProducto);
        return ResponseEntity.ok(stock);
    }

    // http://localhost:8080/api/stock/{id}
    @GetMapping("/{id}")
    public ResponseEntity<StockSucursal> getStockById(@PathVariable("id") String id) {
        StockSucursal stock = stockService.getById(id);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stock);
    }

    // GET /api/stock/sucursal/{id}
    //@GetMapping("/sucursal/{id}")
    //public List<StockSucursal> getStockBySucursal(@PathVariable String id_sucursal) {
    //    return stockService.getBySucursal(id_sucursal);
    //}
    // GET /api/stock/bajo/{umbral}
    @GetMapping("/bajo/{umbral}")
    public List<StockSucursal> getStockBajoUmbral(@PathVariable Integer umbral) {
        return stockService.getStocksBajoUmbral(umbral);
    }


//POST /api/stock → crear nuevo stock

    @PostMapping
    public StockSucursal createStock(@RequestBody StockSucursalDTO dto) {
        StockSucursal nuevoStock = new StockSucursal();
        nuevoStock.setCantidad(dto.getCantidad());
        nuevoStock.setId_sucursal(dto.getId_sucursal());

        Producto producto = new Producto();
        producto.setId_producto(dto.getProducto().getId_producto());
        nuevoStock.setProducto(producto);

        StockSucursal creado = stockService.create(nuevoStock);
        return creado;  // Este objeto tendrá el ID generado automáticamente
    }

    //Aqui pensando en el futuro sera la resta que trae la venta,
    //pero tendria que obtener la cantidad actual y restarle lo que necesito

    //// PUT /api/stock/{id}/cantidad → actualizar cantidad
    @PutMapping("/{id}/cantidad")
    public ResponseEntity<?> restaCantidad_venta (
            @PathVariable String id,
            @RequestParam Integer cantidadVendida) {
        try {
            StockSucursal stockActual = stockService.getById(id); // método que debe retornar el stock por ID
            if (stockActual == null) {
                return ResponseEntity.notFound().build();
            }
            if (cantidadVendida > stockActual.getCantidad()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("No se puede vender más de lo disponible. Stock actual: " + stockActual.getCantidad()));
            }
            int nuevaCantidad = stockActual.getCantidad() - cantidadVendida;
            stockActual.setCantidad(nuevaCantidad);
            StockSucursal actualizado = stockService.create(stockActual); // reutiliza método de guardado
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

    // PUT /api/stock/{id}/agregar → agregar stock recibido
    @PutMapping("/{id}/agregar")
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



    //// DELETE /api/stock/{id} → eliminar
    @DeleteMapping("/{id}")
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