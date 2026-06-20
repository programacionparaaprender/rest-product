package org.programacionparaaprender.quarkus.starting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la entidad MyEntity.
 */
public class MyEntityTest {

    @Test
    @DisplayName("Debería crear una instancia de MyEntity")
    public void testEntityCreation() {
        MyEntity entity = new MyEntity();
        assertNotNull(entity, "La instancia de MyEntity no debería ser nula");
    }

    @Test
    @DisplayName("Debería asignar y recuperar valores de los campos correctamente")
    public void testFieldsAccess() {
        // Arrange
        MyEntity entity = new MyEntity();
        Long expectedId = 1L;
        String expectedField = "Valor de prueba";

        // Act
        entity.id = expectedId;
        entity.field = expectedField;

        // Assert
        assertEquals(expectedId, entity.id, "El ID debe ser igual al asignado");
        assertEquals(expectedField, entity.field, "El campo field debe ser igual al asignado");
    }
}
