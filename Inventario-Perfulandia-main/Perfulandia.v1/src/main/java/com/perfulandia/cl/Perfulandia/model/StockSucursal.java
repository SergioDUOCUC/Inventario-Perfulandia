package com.perfulandia.cl.Perfulandia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name ="Stock_Sucursal")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class StockSucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_STOCK_SUCURSAL")
    private String id_stock;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @JoinColumn(name = "id_sucursal", nullable = false)
    private String id_sucursal;


    @Column(nullable = false)
    private Integer cantidad;

}
