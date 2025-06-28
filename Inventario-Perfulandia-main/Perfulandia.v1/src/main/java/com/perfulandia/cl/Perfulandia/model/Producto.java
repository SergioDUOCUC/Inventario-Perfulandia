package com.perfulandia.cl.Perfulandia.model;

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
    @Column(name = "id_producto") // El nombre de la columna en la base de datos
    private String id_producto;   // El nombre de la propiedad en la clase

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private int  valor_unitario; //Editarlo a INT WTF PORQUE QUEDO EN STRING XD

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String genero;

    private int ml;

    private String fechaIngreso;

    private String fechaVencimiento;

    @Column(nullable = false)
    private int codigo_barra;

    @Column(nullable = false)
    private String origen;



}
