package com.perfulandia.cl.Perfulandia.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor


public class StockSucursalDTO {
    private Integer cantidad;
    private String id_sucursal;
    private Producto producto;
}