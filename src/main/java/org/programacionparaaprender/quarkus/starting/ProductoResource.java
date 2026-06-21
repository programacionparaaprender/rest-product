package org.programacionparaaprender.quarkus.starting;

import org.programacionparaaprender.quarkus.starting.entities.Producto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/productos")
public class ProductoResource {

    // Lista simulada de productos
    private final List<Producto> inventario = List.of(
        new Producto("1", "Camiseta de España", "deportes", 25.99),
        new Producto("2", "Balón de Fútbol USA", "deportes", 29.99),
        new Producto("3", "Zapatillas Running", "calzado", 89.95),
        new Producto("4", "Gorra Oficial", "deportes", 15.00)
    );

    @GET
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Producto> filtrarProductos(
            @QueryParam("categoria") String categoria,
            @QueryParam("precioMax") Double precioMax) {

        // Aquí aplicamos Lambdas mediante la API Stream de Java
        return inventario.stream()
            // Lambda 1: Filtra por categoría si el parámetro está presente
            .filter(p -> categoria == null || p.getCategoria().equalsIgnoreCase(categoria))
            // Lambda 2: Filtra por precio máximo si el parámetro está presente
            .filter(p -> precioMax == null || p.getPrecio() <= precioMax)
            // Colectamos el resultado en una nueva lista
            .collect(Collectors.toList());
    }
}