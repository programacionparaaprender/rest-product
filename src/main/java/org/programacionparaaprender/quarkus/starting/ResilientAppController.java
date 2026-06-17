package org.programacionparaaprender.quarkus.starting;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Asynchronous;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/resilient")
public class ResilientAppController {

    @Inject
    ExternalAPICaller externalAPICaller;

    @GET
    @Path("/test-circuit")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return externalAPICaller.callApi();
    }

    @GET
    @Path("/circuit-breaker")
    @CircuitBreaker(requestVolumeThreshold = 4)
    @Fallback(fallbackMethod = "circuitBreakerFallback")
    @Produces(MediaType.APPLICATION_JSON)
    public Response circuitBreaker() {
        return Response.ok(externalAPICaller.callApi()).build();
    }

    public Response circuitBreakerFallback(Exception ex) {
        return Response.status(503)
                .entity("Circuit Breaker Fallback: El servicio externo no está disponible actualmente.")
                .build();
    }

    @GET
    @Path("/bulkhead")
    @Bulkhead(value = 5)
    @Fallback(fallbackMethod = "bulkheadFallback")
    @Produces(MediaType.APPLICATION_JSON)
    public Response bulkheadApi() {
        return Response.ok(externalAPICaller.callApiWithDelay()).build();
    }

    public Response bulkheadFallback() {
        return Response.status(400).entity("bulkhead full exception").build();
    }

    @GET
    @Path("/rate-limiter")
    @Asynchronous
    @Bulkhead(value = 10) // MicroProfile no tiene @RateLimiter estándar; @Bulkhead limita concurrencia.
    @Fallback(fallbackMethod = "rateLimiterFallback")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> rateLimitApi() {
        return CompletableFuture.supplyAsync(() -> 
            Response.ok(externalAPICaller.callApiWithDelay2()).build()
        );
    }

    public CompletionStage<Response> rateLimiterFallback(Exception ex) {
        return CompletableFuture.completedFuture(
            Response.status(400).entity("rate limiter fallback").build()
        );
    }

    @GET
    @Path("/retryApi")
    @Retry(maxRetries = 3)
    @Fallback(fallbackMethod = "fallbackAfterRetry")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retryApi() {
        return Response.ok(externalAPICaller.callApiWithDelay()).build();
    }

    public Response fallbackAfterRetry(Exception ex) {
        return Response.status(400).entity("fallback after retry").build();
    }
    
}
