package org.programacionparaaprender.quarkus.starting;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Pruebas unitarias para ResilientAppController.
 * Se utiliza @QuarkusTest junto con @InjectMock para simular el comportamiento 
 * de ExternalAPICaller y verificar la lógica de Fault Tolerance.
 */
@QuarkusTest
public class ResilientAppControllerTest {

    @InjectMock
    ExternalAPICaller externalAPICaller;

    @Test
    @DisplayName("Debería retornar el valor esperado cuando la API externa responde correctamente")
    public void testCircuitEndpoint() {
        Mockito.when(externalAPICaller.callApi()).thenReturn("Success API Call");

        given()
          .when().get("/resilient/test-circuit")
          .then()
             .statusCode(200)
             .body(is("Success API Call"));
    }

    @Test
    @DisplayName("Debería retornar el fallback definido cuando ocurre un fallo en el Circuit Breaker")
    public void testCircuitBreakerFallback() {
        // Simulamos un error en la llamada a la API
        Mockito.when(externalAPICaller.callApi()).thenThrow(new RuntimeException("API Down"));

        given()
          .when().get("/resilient/circuit-breaker")
          .then()
             .statusCode(503)
             .body(is("Circuit Breaker Fallback: El servicio externo no está disponible actualmente."));
    }

    @Test
    @DisplayName("Debería retornar éxito en el endpoint de reintento (Retry)")
    public void testRetryApiSuccess() {
        Mockito.when(externalAPICaller.callApiWithDelay()).thenReturn("Success after retry");

        given()
          .when().get("/resilient/retryApi")
          .then()
             .statusCode(200)
             .body(is("Success after retry"));
    }

    @Test
    @DisplayName("Debería retornar el fallback de reintento si la API falla persistentemente")
    public void testRetryApiFallback() {
        Mockito.when(externalAPICaller.callApiWithDelay()).thenThrow(new RuntimeException("Persistent Failure"));

        given()
          .when().get("/resilient/retryApi")
          .then()
             .statusCode(400)
             .body(is("fallback after retry"));
    }
}
