package org.programacionparaaprender.quarkus.starting;

import org.programacionparaaprender.quarkus.starting.entities.Product;
import org.programacionparaaprender.quarkus.starting.repositories.ProductRepository;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.util.List;

@Path("/product")
public class BookResource {

    @ConfigProperty(name="greeting")
	private String greeting;

	@Inject
    ProductRepository pr;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("custom/{name}")
    public String customHello(@PathParam("name") String name) {
    	return greeting + " " + name + " how are you doing?";
    }

    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("obtener/{id}")
    public Product obtener(@PathParam("id") final String id){
        return pr.getProduct(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> list() {
        return pr.listProduct();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Product p) {
        pr.createdProduct(p);
        return Response.ok().build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(Product p) {
       pr.deleteProduct(p);
        return Response.ok().build();
    }
}
