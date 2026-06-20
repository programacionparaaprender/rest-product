package org.programacionparaaprender.quarkus.starting.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
public class PayPalService {

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final String PAYPAL_API = "https://api-m.sandbox.paypal.com";

    @Inject
    ObjectMapper mapper;

    public String obtenerAccessToken() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(PAYPAL_API + "/v1/oauth2/token");
            String auth = CLIENT_ID + ":" + CLIENT_SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            post.addHeader("Authorization", "Basic " + encodedAuth);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.addHeader("Accept", "application/json");
            post.setEntity(new StringEntity("grant_type=client_credentials", ContentType.APPLICATION_FORM_URLENCODED));

            var response = client.execute(post);
            String responseBody = new String(response.getEntity().getContent().readAllBytes());
            JsonNode json = mapper.readTree(responseBody);
            return json.get("access_token").asText();
        }
    }

    public JsonNode crearOrden(String accessToken) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(PAYPAL_API + "/v2/checkout/orders");
            post.addHeader("Authorization", "Bearer " + accessToken);
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "application/json");

            String jsonBodyAngular = """
                    {
                      "intent": "CAPTURE",
                      "purchase_units": [{
                        "amount": {
                          "currency_code": "USD",
                          "value": "10.00"
                        }
                      }],
                      "application_context": {
                        "return_url": "http://localhost:4200/paypal/success",
                        "cancel_url": "http://localhost:4200/paypal/cancel"
                      }
                    }
                    """;
            String jsonBodySpringMVC = """
                    {
                      "intent": "CAPTURE",
                      "purchase_units": [{
                        "amount": {
                          "currency_code": "USD",
                          "value": "10.00"
                        }
                      }],
                      "application_context": {
                        "return_url": "http://localhost:8080/spring-mvc-6/paypal/success",
                        "cancel_url": "http://localhost:8080/spring-mvc-6/paypal/cancel"
                      }
                    }
                    """;
            String jsonBodyReact = """
                    {
                      "intent": "CAPTURE",
                      "purchase_units": [{
                        "amount": {
                          "currency_code": "USD",
                          "value": "10.00"
                        }
                      }],
                      "application_context": {
                        "return_url": "http://localhost:8080/paypal/success",
                        "cancel_url": "http://localhost:8080/paypal/cancel"
                      }
                    }
                    """;
            
            String jsonBodyVue = """
                    {
                      "intent": "CAPTURE",
                      "purchase_units": [{
                        "amount": {
                          "currency_code": "USD",
                          "value": "10.00"
                        }
                      }],
                      "application_context": {
                        "return_url": "http://localhost:3000/paypal/success",
                        "cancel_url": "http://localhost:3000/paypal/cancel"
                      }
                    }
                    """;
            
            String jsonBody = jsonBodyAngular;

            post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            var response = client.execute(post);
            String responseBody = new String(response.getEntity().getContent().readAllBytes());
            return mapper.readTree(responseBody);
        }
    }

    public JsonNode capturarPago(String accessToken, String orderId) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(PAYPAL_API + "/v2/checkout/orders/" + orderId + "/capture");
            post.addHeader("Authorization", "Bearer " + accessToken);
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "application/json");

            var response = client.execute(post);
            String responseBody = new String(response.getEntity().getContent().readAllBytes());
            return mapper.readTree(responseBody);
        }
    }
}
