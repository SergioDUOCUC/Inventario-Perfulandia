package com.perfulandia.cl.Perfulandia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="PRODUCTO")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Producto {

     @Id
    @Column(name = "id_producto")
    @Schema(description = "Identificador único del producto", example = "P123")
    private String id_producto;

    @Column(nullable = false)
    @Schema(description = "Nombre del producto", example = "Perfume XYZ")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Marca del producto", example = "Marca ABC")
    private String marca;

    @Column(nullable = false)
    @Schema(description = "Valor unitario del producto", example = "15990", type = "integer", format = "int")
    private int valor_unitario; // Cambiado a int

    @Column(nullable = false)
    @Schema(description = "SKU del producto", example = "SKU123456")
    private String sku;

    @Column(nullable = false)
    @Schema(description = "Género al que está dirigido el producto", example = "Unisex")
    private String genero;

    @Schema(description = "Mililitros del producto", example = "100")
    private int ml;

    @Schema(description = "Fecha de ingreso del producto (formato yyyy-MM-dd)", example = "2025-06-29")
    private String fechaIngreso;

    @Schema(description = "Fecha de vencimiento del producto (formato yyyy-MM-dd)", example = "2027-06-29")
    private String fechaVencimiento;

    @Column(nullable = false)
    @Schema(description = "Código de barras del producto", example = "789456123", type = "integer", format = "int")
    private int codigo_barra;

    @Column(nullable = false)
    @Schema(description = "Origen del producto", example = "Chile")
    private String origen;



}
