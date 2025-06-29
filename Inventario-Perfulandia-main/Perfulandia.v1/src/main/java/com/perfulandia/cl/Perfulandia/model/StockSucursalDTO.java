package com.perfulandia.cl.Perfulandia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para la creación o modificación de registros de stock en una sucursal")
public class StockSucursalDTO {

    @Schema(description = "Cantidad de producto disponible", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;

    @Schema(description = "ID de la sucursal donde se almacena el producto", example = "SUC456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id_sucursal;

    @Schema(description = "Información del producto asociado", requiredMode = Schema.RequiredMode.REQUIRED)
    private Producto producto;
}
