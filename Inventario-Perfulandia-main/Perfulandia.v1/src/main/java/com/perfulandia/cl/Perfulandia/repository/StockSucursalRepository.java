package com.perfulandia.cl.Perfulandia.repository;

import com.perfulandia.cl.Perfulandia.model.Producto;
import com.perfulandia.cl.Perfulandia.model.StockSucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockSucursalRepository extends JpaRepository<StockSucursal, String> {
   // List<StockSucursal> findBySucursalId(String id_sucursal);

    List<StockSucursal> findByCantidadLessThanEqual(Integer umbral);

    // Buscar por objeto Producto completo
    StockSucursal findByProducto(Producto producto);

    @Query("SELECT s FROM StockSucursal s WHERE s.producto.id_producto = :id_producto")
    StockSucursal findByProductoId(@Param("id_producto") String idProducto);

}
