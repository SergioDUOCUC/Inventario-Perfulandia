package com.perfulandia.cl.Perfulandia.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.Perfulandia.model.StockSucursal;
import com.perfulandia.cl.Perfulandia.repository.ProductoRepository;
import com.perfulandia.cl.Perfulandia.repository.StockSucursalRepository;

@Service
public class StockSucursalService {

    @Autowired
    private StockSucursalRepository stockRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todos los registros
    public List<StockSucursal> getAll() {
        return stockRepository.findAll();
    }

    //// Buscar por sucursal
    //public List<StockSucursal> getBySucursal(String id_sucursal) {
    //    return stockRepository.findBySucursalId(id_sucursal);
    //}


    // Buscar por ID de producto (forma 1)
    public StockSucursal buscarPorIdProducto(String id_producto) {
        return stockRepository.findByProductoId(id_producto);
    }

    public StockSucursal obtenerStockPorProducto(String idProducto) {
        return stockRepository.findByProductoId(idProducto);
    }


//     Agregar nuevo stock
    public StockSucursal create(StockSucursal stock) {
        return stockRepository.save(stock);
    }

    public StockSucursal getById(String id) {
        return stockRepository.findById(id).orElse(null);
    }

    // Actualizar cantidad de stock
    public StockSucursal updateCantidad(String id_stock, Integer nuevaCantidad) {
        Optional<StockSucursal> optional = stockRepository.findById(id_stock);
        if (optional.isPresent()) {
            StockSucursal stock = optional.get();
            stock.setCantidad(nuevaCantidad);
            return stockRepository.save(stock);
        } else {
            throw new RuntimeException("Stock no encontrado con ID: " + id_stock);
        }
    }

    // Eliminar registro de stock
   public void delete(String id) {
    StockSucursal stock = getById(id);
    if (stock == null) {
        throw new IllegalArgumentException("No se encontró el stock con ID: " + id);
    }
    stockRepository.deleteById(id);
}




    //// Buscar stocks bajos
    public List<StockSucursal> getStocksBajoUmbral(Integer umbral) {
        return stockRepository.findByCantidadLessThanEqual(umbral);
    }


}