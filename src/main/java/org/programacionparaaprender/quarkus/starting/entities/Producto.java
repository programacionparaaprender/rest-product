package org.programacionparaaprender.quarkus.starting.entities;


public class Producto {
    private String id;
    private String nombre;
    private String categoria;
    private double precio;

    public Producto(String id, String nombre, String categoria, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    // Getters y Setters (O usa Lombok si prefieres @Data)
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
}
