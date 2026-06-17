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
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
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

    @PUT
    @Path("{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Product p) {
        Product entity = pr.getProduct(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.setName(p.getName());
        entity.setDescription(p.getDescription());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        Product p = pr.getProduct(id);
        if (p != null) {
            pr.deleteProduct(p);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
