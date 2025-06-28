package com.perfulandia.cl.Perfulandia;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.perfulandia.cl.Perfulandia.model.Producto;
import com.perfulandia.cl.Perfulandia.model.StockSucursal;
import com.perfulandia.cl.Perfulandia.services.ProductoService;
import com.perfulandia.cl.Perfulandia.services.StockSucursalService;

@SpringBootTest
public class ServicesTest {

    @Autowired
    private ProductoService productoservice;

    @Autowired
    private StockSucursalService stocksucursalservice;

    @Test
    // Test de buscar todos los productos
    public void testObtenerProductos() {
        List<Producto> productos = productoservice.buscarTodo();
        Assertions.assertNotNull(productos);
        Assertions.assertFalse(productos.isEmpty(), "No se encontraron productos en la base de datos.");
        Producto producto = productos.get(1);
        System.out.println("El primer producto encontrado es: " + producto);

    }

    // Buscar un producto
    @Test
    public void buscarUnProductoTest() {
        Producto productoBuscado = productoservice.buscarUnProducto2("P13");
        assertEquals("Perfume TEST", productoBuscado.getNombre());
    }

    // Registrar un producto
    @Test
    public void registrarUnProductoTest() {
        Producto p = new Producto();
        p.setId_producto("PERF123456");
        p.setNombre("Brisa Nocturna");
        p.setMarca("Essenza");
        p.setValor_unitario(2500);
        p.setSku("ESS-BRISA-50ML");
        p.setGenero("Unisex");
        p.setMl(50);
        p.setFechaIngreso("2025-06-07");
        p.setFechaVencimiento("2027-06-07");
        p.setCodigo_barra(789456123);
        p.setOrigen("Francia");

        productoservice.GuardarProducto(p);

        Assertions.assertNotNull(p);
        Assertions.assertEquals("Brisa Nocturna", p.getNombre());
    }

    @Test
    public void testActualizarProducto() {
        Producto p = new Producto();
        p.setId_producto("PERF654321");
        p.setNombre("Brisa Diurna");
        p.setMarca("EssenA");
        p.setValor_unitario(5990);
        p.setSku("DIU-BRISA-50ML");
        p.setGenero("Masculino");
        p.setMl(50);
        p.setFechaIngreso("2025-06-07");
        p.setFechaVencimiento("2027-06-07");
        p.setCodigo_barra(789455412);
        p.setOrigen("India");

        Producto guardarP = productoservice.GuardarProducto(p);
        assertEquals(5990, guardarP.getValor_unitario(), "El resultado no es el esperado");

    }

    @Test
    public void testEliminarProducto() {

        // Verifica que se encuentre el producto en la base de datos
        Producto productoGuardado = productoservice.buscarUnProducto2("PERF654321");
        assertNotNull(productoGuardado, "El producto NO se encuentra en la base de datos.");

        // Elimina el producto
        productoservice.EliminarProducto2("PERF654321");

        // Verifica que se eliminó correctamente
        Producto productoEliminado = productoservice.buscarUnProducto2("PERF654321");
        assertNull(productoEliminado, "El producto aún existe en la base de datos.");

    }

    @Test
    public void testTodoStockSucursal() {
        // Obtener todos los registros de stock
        List<StockSucursal> stocks = stocksucursalservice.getAll();

        // Verificar que la lista no sea nula
        Assertions.assertNotNull(stocks, "La lista de stock no debe ser nula");

        // Verificar que la lista no esté vacía
        Assertions.assertFalse(stocks.isEmpty(), "No se encontraron productos en la base de datos.");

        // Tomar el primer stock y obtener su producto
        StockSucursal stock = stocks.get(6); // Primer elemento
        Producto producto = stock.getProducto(); // Suponiendo que existe getProducto()

        // Verificar que el producto no sea nulo
        Assertions.assertNotNull(producto, "El producto asociado al stock no debe ser nulo");

    }

    @Test
    public void testBuscarPorIdProducto() {
        // ID de producto conocido que ya debe existir en la base de datos
        String idProducto = "P1";

        // Llamar al servicio para obtener el stock por ID de producto
        StockSucursal stock = stocksucursalservice.buscarPorIdProducto(idProducto);

        // Validar que el resultado no sea nulo
        assertNotNull(stock, "El stock no debe ser nulo para un ID de producto existente");

        // Validar que el producto dentro del stock también exista
        Producto producto = stock.getProducto();
        assertNotNull(producto, "El producto dentro del stock no debe ser nulo");

        // Validar que el ID del producto coincide con el buscado
        assertNotNull(producto.getId_producto(), "El ID del producto no debe ser nulo");
        assertEquals(idProducto, producto.getId_producto(), "El ID del producto debe ser igual al buscado");
    }

    @Test
    public void testObtenerStockPorProducto() {
        String idProducto = "P2132121";

        StockSucursal stock = stocksucursalservice.obtenerStockPorProducto(idProducto);

        assertNotNull(stock, "No se encontró stock para el ID de producto: " + idProducto);
        assertNotNull(stock.getProducto(), "El producto asociado al stock no debe ser nulo");
    }

    @Test
    public void testObtenerStock() {

        StockSucursal stockBuscado = stocksucursalservice.obtenerStockPorProducto("P1");
        assertEquals("SS32", stockBuscado.getId_stock(), "El stock del producto es " + stockBuscado.getCantidad());

    }

    @Test
    public void TestStockBajoUmbral() {
        int umbral = 5;

        List<StockSucursal> resultado = stocksucursalservice.getStocksBajoUmbral(umbral);
        assertNotNull(resultado, "La lista no debe ser nula");
        assertFalse(resultado.isEmpty(), "Debe haber un producto bajo stock");

        for (StockSucursal stock : resultado) {
            assertTrue(stock.getCantidad() <= umbral, "El stock con ID " + stock.getId_stock()
                    + " Tiene una cantidad de: " + stock.getCantidad() + "que no es menor que el umbral");

        }

    }

    @Test
    public void testCrearStockSucursal() {
    
    String idProductoExistente = "PERF123456";   
    String idSucursalExistente = "S2";   

    StockSucursal nuevoStock = new StockSucursal();
    nuevoStock.setCantidad(5);
    nuevoStock.setId_sucursal(idSucursalExistente);

    Producto producto = new Producto();
    producto.setId_producto(idProductoExistente);
    nuevoStock.setProducto(producto);

    
    StockSucursal creado = stocksucursalservice.create(nuevoStock);

    // Verificaciones
    assertNotNull(creado, "El stock creado no debe ser nulo");
    assertNotNull(creado.getId_stock(), "El ID generado del stock no debe ser nulo");
    assertEquals(idSucursalExistente, creado.getId_sucursal(), "La sucursal no coincide");
    assertEquals(idProductoExistente, creado.getProducto().getId_producto(), "El producto no coincide");
    assertEquals(5, creado.getCantidad(), "La cantidad no coincide");

    
    System.out.println("Stock creado con ID: " + creado.getId_stock());
    }

    @Test
    public void testRestaCantidadVenta() {
    
    String idStock = "SS42"; 
    int cantidadVendida = 2;

    // Buscar stock actual
    StockSucursal stockAntes = stocksucursalservice.getById(idStock);
    assertNotNull(stockAntes, "El stock debe existir antes de la venta");

    int cantidadInicial = stockAntes.getCantidad();

    // Ejecutar la lógica de venta
    stockAntes.setCantidad(cantidadInicial - cantidadVendida);
    StockSucursal actualizado = stocksucursalservice.create(stockAntes);

    // Verificaciones
    assertNotNull(actualizado, "El stock actualizado no debe ser nulo");
    assertEquals(cantidadInicial - cantidadVendida, actualizado.getCantidad(),
            "La cantidad no fue restada correctamente");

    System.out.println("Cantidad antes: " + cantidadInicial);
    System.out.println("Cantidad vendida: " + cantidadVendida);
    System.out.println("Cantidad actualizada: " + actualizado.getCantidad());
}

@Test
public void testAgregarCantidadStockExistente() {
    //ID de stock real que debe estar en la base de datos
    String idStock = "SS42"; 
    int cantidadAgregada = 3;

    //Busca el stock actual
    StockSucursal stockAntes = stocksucursalservice.getById(idStock);
    assertNotNull(stockAntes, "El stock debe existir antes de agregar cantidad");

    int cantidadInicial = stockAntes.getCantidad();

    //Agregar la cantidad
    stockAntes.setCantidad(cantidadInicial + cantidadAgregada);
    StockSucursal actualizado = stocksucursalservice.create(stockAntes);

    //Verificar resultados
    assertNotNull(actualizado, "El stock actualizado no debe ser nulo");
    assertEquals(cantidadInicial + cantidadAgregada, actualizado.getCantidad(),
            "La cantidad no se sumó correctamente");

    
    System.out.println("ID del stock: " + idStock);
    System.out.println("Cantidad inicial: " + cantidadInicial);
    System.out.println("Cantidad agregada: " + cantidadAgregada);
    System.out.println("Cantidad final: " + actualizado.getCantidad());
}

@Test
public void testEliminarStockExistenteSinCrear() {
    
    String idStockExistente = "SS43";

    // Verificacion de id en la base de datos.
    StockSucursal stock = stocksucursalservice.getById(idStockExistente);
    assertNotNull(stock, "No existe la ID buscada en la base de datos.");

    
    stocksucursalservice.delete(idStockExistente);

    // Verificacion post eliminacion del ID
    StockSucursal eliminado = stocksucursalservice.getById(idStockExistente);
    assertNull(eliminado, "El stock debería haber sido eliminado");
}







    

}
