package org.programacionparaaprender.quarkus.starting;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.programacionparaaprender.quarkus.starting.entities.PageEvent;

import java.util.Date;
import java.util.Random;

@Path("/api")
public class PageEventController {

    @Inject
    @Channel("page-events-out")
    Emitter<PageEvent> pageEventEmitter;

    /**
     * Migración de Spring Cloud Stream Bridge a Quarkus Reactive Messaging.
     * 
     * En Quarkus, inyectamos un Emitter vinculado a un canal definido en application.properties.
     * Nota: Reactive Messaging no soporta canales dinámicos por defecto como StreamBridge;
     * se recomienda usar canales preconfigurados.
     */
    @GET
    @Path("/publish/{topic}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public PageEvent publish(@PathParam("topic") String topic, @PathParam("name") String name) {
        PageEvent pageEvent = new PageEvent(
            name, 
            Math.random() > 0.5 ? "A1" : "A3", 
            new Date(), 
            new Random().nextInt(9000)
        );

        // Enviamos el evento al canal de salida. 
        // Según tu configuración previa, este canal escribe en el tópico 'T4'.
        pageEventEmitter.send(pageEvent);
        
        return pageEvent;
    }
}
