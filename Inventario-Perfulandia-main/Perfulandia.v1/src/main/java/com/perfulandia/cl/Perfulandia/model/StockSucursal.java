package com.perfulandia.cl.Perfulandia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Stock_Sucursal")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa el stock de un producto en una sucursal específica")
public class StockSucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_STOCK_SUCURSAL")
    @Schema(description = "ID único del registro de stock", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private String id_stock;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    @Schema(description = "Producto asociado al stock")
    private Producto producto;

    @JoinColumn(name = "id_sucursal", nullable = false)
    @Schema(description = "ID de la sucursal asociada al stock", example = "SUC123")
    private String id_sucursal;

    @Column(nullable = false)
    @Schema(description = "Cantidad actual del producto disponible en la sucursal", example = "20")
    private Integer cantidad;
}
