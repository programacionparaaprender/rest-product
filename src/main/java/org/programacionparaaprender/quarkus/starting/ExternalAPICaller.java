package org.programacionparaaprender.quarkus.starting;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;

@ApplicationScoped
public class ExternalAPICaller {
    
    // Nota: RestTemplate es de Spring. En Quarkus se recomienda usar MicroProfile REST Client.
    // Se elimina el constructor de RestTemplate para permitir que el proyecto compile.

    public String callApi2() {
        // En Quarkus usarías una interfaz con @RegisterRestClient para este llamado
        return "Response from external API (Migrate to RestClient for real calls)";
    }
    
    public String callApiWithDelay() {
        return "call api with delay";
    }
    
    public String callApiWithDelay2() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
        return "call api with delay";
    }

    /**
     * En MicroProfile, la configuración del CircuitBreaker se define mediante 
     * parámetros en la anotación o en application.properties.
     */
    @CircuitBreaker(
        requestVolumeThreshold = 4, 
        failureRatio = 0.5, 
        delay = 5000
    )
    @Fallback(fallbackMethod = "fallback")
    public String callApi() {
        throw new RuntimeException("External service failure");
    }

    public String fallback(Exception e) {
        return "Fallback response: The external service is currently unavailable.";
    }
}
