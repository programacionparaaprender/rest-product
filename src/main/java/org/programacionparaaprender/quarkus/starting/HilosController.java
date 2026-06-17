package org.programacionparaaprender.quarkus.starting;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Path("/api/hilos")
@ApplicationScoped // Make it a CDI bean to manage lifecycle
@Produces(MediaType.APPLICATION_JSON) // Default for all methods unless overridden
public class HilosController {
    
    // ExecutorService para tareas asíncronas generales
    // Java 21: Usando Virtual Threads para escalabilidad masiva
    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    // ThreadPoolExecutor para un control más granular sobre los parámetros del pool de hilos
    private final ExecutorService fixedThreadPoolExecutor = new ThreadPoolExecutor(
            2, // corePoolSize: número de hilos a mantener en el pool, incluso si están inactivos
            5, // maximumPoolSize: número máximo de hilos permitidos en el pool
            60, // keepAliveTime: cuando el número de hilos es mayor que el core, es el tiempo máximo que los hilos inactivos en exceso esperarán nuevas tareas antes de terminar
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10) // workQueue: cola para mantener las tareas antes de que se ejecuten
    );

    @PreDestroy
    void shutdownExecutors() {
        // Shut down virtual thread executor
        virtualThreadExecutor.shutdown();
        try {
            if (!virtualThreadExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                virtualThreadExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            virtualThreadExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Shut down fixed thread pool executor
        fixedThreadPoolExecutor.shutdown();
        try {
            if (!fixedThreadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                fixedThreadPoolExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            fixedThreadPoolExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Endpoint que utiliza un ExecutorService estándar (Virtual Threads) para realizar una tarea asíncrona.
     * La tarea simula un retardo y devuelve un mensaje.
     *
     * @return Un CompletionStage que contiene un Response con el resultado de la tarea.
     */
    @GET
    @Path("/executor-service")
    public CompletionStage<Response> useExecutorService() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simular una tarea de larga duración
                Thread.sleep(java.time.Duration.ofSeconds(2)); // Sintaxis moderna de Java
                return Response.ok("Tarea completada usando ExecutorService (Virtual Threads) en el hilo: " + Thread.currentThread().getName()).build();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Response.status(500).entity("Tarea interrumpida usando ExecutorService (Virtual Threads)").build();
            }
        }, virtualThreadExecutor); // Ejecutar en nuestro executorService personalizado
    }

    /**
     * Endpoint que utiliza un ThreadPoolExecutor para realizar una tarea asíncrona.
     * Esto demuestra un mayor control sobre la configuración del pool de hilos.
     *
     * @return Un CompletionStage que contiene un Response con el resultado de la tarea.
     */
    @GET
    @Path("/thread-pool-executor")
    public CompletionStage<Response> useThreadPoolExecutor() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simular otra tarea de larga duración
                Thread.sleep(3000); // Retardo de 3 segundos
                return Response.ok("Tarea completada usando ThreadPoolExecutor en el hilo: " + Thread.currentThread().getName()).build();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Response.status(500).entity("Tarea interrumpida usando ThreadPoolExecutor").build();
            }
        }, fixedThreadPoolExecutor); // Ejecutar en nuestro threadPoolExecutor personalizado
    }

    /**
     * Endpoint para demostrar el funcionamiento de RxJava 3.
     * Basado en el ejemplo proporcionado, adaptado para ver la salida en la respuesta HTTP.
     *
     * @return Una cadena con los eventos recibidos del Observable.
     */
    @GET
    @Path("/rxjava")
    @Produces(MediaType.TEXT_PLAIN) // Override default to TEXT_PLAIN for this method
    public String useRxJava() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Iniciando flujo RxJava ---\n");

        Observable<String> saludoObservable = Observable.create(emitter -> {
            try {
                emitter.onNext("Hola");
                Thread.sleep(500);
                emitter.onNext("mundo");
                Thread.sleep(500);
                emitter.onNext("RxJava con Maven");
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        // Usamos blockingSubscribe para capturar los eventos y devolverlos en la respuesta HTTP
        saludoObservable.blockingSubscribe(
            item -> {
                System.out.println("Recibido: " + item);
                sb.append("Recibido: ").append(item).append("\n");
            },
            error -> {
                System.err.println("Error: " + error);
                sb.append("Error: ").append(error.getMessage()).append("\n");
            },
            () -> {
                System.out.println("¡Completado!");
                sb.append("¡Completado!");
            }
        );

        return sb.toString();
    }
}
