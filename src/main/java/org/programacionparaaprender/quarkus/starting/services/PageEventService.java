package org.programacionparaaprender.quarkus.starting.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.programacionparaaprender.quarkus.starting.entities.PageEvent;



/**
 * Migración de PageEventService de Spring a Quarkus usando SmallRye Reactive Messaging.
 */
@ApplicationScoped
public class PageEventService {

    /**
     * En Quarkus, el equivalente a un bean de Consumer para mensajería 
     * es un método anotado con @Incoming.
     */
    @Incoming("page-events-in")
    public void pageEventConsumer(PageEvent input) {
        System.out.println("*****************");
        System.out.println(input.getName());
        System.out.println(input.getUser());
        System.out.println(input.getDuration());
        System.out.println("******************");
    }

    /*
     * Migración del Supplier. En Quarkus se usa @Outgoing con Mutiny Multi para flujos periódicos.
     * 
     * @org.eclipse.microprofile.reactive.messaging.Outgoing("page-events-out")
     * public io.smallrye.mutiny.Multi<PageEvent> pageEventSupplier() {
     *     return io.smallrye.mutiny.Multi.createFrom().ticks().every(java.time.Duration.ofSeconds(1))
     *             .map(tick -> new PageEvent(
     *                 Math.random() > 0.5 ? "X1" : "X3",
     *                 Math.random() > 0.5 ? "A1" : "A3",
     *                 new Date(),
     *                 new Random().nextInt(9000)));
     * }
     */
}
