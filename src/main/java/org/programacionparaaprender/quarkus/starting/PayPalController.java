package org.programacionparaaprender.quarkus.starting;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.programacionparaaprender.quarkus.starting.services.PayPalService;

@Path("/paypal")
public class PayPalController {

    @Inject
    PayPalService payPalService;

    @POST
    @Path("/create-order")
    @Produces(MediaType.TEXT_PLAIN)
    public String createOrder() throws Exception {
        String token = payPalService.obtenerAccessToken();
        JsonNode order = payPalService.crearOrden(token);

        String orderId = order.get("id").asText();
        String approveUrl = null;
        if (order.has("links")) {
            for (JsonNode link : order.get("links")) {
                if (link.get("rel").asText().equals("approve")) {
                    approveUrl = link.get("href").asText();
                    break;
                }
            }
        }

        return "Orden creada con ID: " + orderId + "\nAprobar en: " + approveUrl;
    }

    @GET
    @Path("/success")
    @Produces(MediaType.TEXT_PLAIN)
    public String success(@QueryParam("token") String token) throws Exception {
        if (token == null) {
            return "Error: Token no proporcionado.";
        }
        String accessToken = payPalService.obtenerAccessToken();
        JsonNode capture = payPalService.capturarPago(accessToken, token);
        return "Pago completado: " + capture.toPrettyString();
    }

    @GET
    @Path("/cancel")
    @Produces(MediaType.TEXT_PLAIN)
    public String cancel() {
        return "Pago cancelado por el usuario.";
    }
}
