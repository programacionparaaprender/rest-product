package org.programacionparaaprender.quarkus.starting.entities;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue
    private Long Id;
    private String name;
    private String description;
}
