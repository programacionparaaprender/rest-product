package org.programacionparaaprender.quarkus.starting;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.time.Duration;

@Path("/reactivo")
public class RecursoReactivo {

    @GET
    @Path("/datos")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<String> obtenerDatosReactivos() {
        // Imprime el hilo actual antes de la pausa simulada (Verás que es un hilo tipo "vert.x-eventloop")
        System.out.println("Petición recibida en el hilo: " + Thread.currentThread().getName());

        return Uni.createFrom().item("¡Datos procesados de forma reactiva!")
                // Simulamos un retraso asíncrono de 2 segundos (como una consulta a BD o RabbitMQ)
                .onItem().delayIt().by(Duration.ofSeconds(2))
                // Cuando el resultado está listo, se ejecuta este paso
                .onItem().transform(item -> {
                    System.out.println("Respuesta enviada desde el hilo: " + Thread.currentThread().getName());
                    return item.toUpperCase();
                });
    }
}