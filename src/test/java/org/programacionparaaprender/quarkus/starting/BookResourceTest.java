package org.programacionparaaprender.quarkus.starting;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BookResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/product/custom/Ejemplo")
          .then()
             .statusCode(200)
             .body(is("Hello Dear: Ejemplo how are you doing?"));
    }

}