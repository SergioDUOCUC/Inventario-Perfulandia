package com.perfulandia.cl.Perfulandia.assemblers;


import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.Perfulandia.controller.ProductoController;
import com.perfulandia.cl.Perfulandia.controller.ProductoControllerV2;
import com.perfulandia.cl.Perfulandia.model.Producto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;






@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
            
            linkTo(methodOn(ProductoControllerV2.class).ListarProductos()).withRel("productos"),

            linkTo(methodOn(ProductoControllerV2.class).eliminarProducto(producto.getId_producto())).withRel("eliminar"),
            
            linkTo(methodOn(ProductoControllerV2.class).actualizarProducto(producto.getId_producto(), null)).withRel("actualizar")
        );
        
    }
}
