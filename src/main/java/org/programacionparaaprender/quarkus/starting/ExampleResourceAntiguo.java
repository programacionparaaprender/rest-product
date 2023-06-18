package org.programacionparaaprender.quarkus.starting;



import java.util.LinkedHashMap;
import java.util.Set;

import java.util.*;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.*;


@Path("/product2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExampleResourceAntiguo {

	private final Set<Quark> quarks = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
	
	public ExampleResourceAntiguo() {
		quarks.add(new Quark("Up", "The up quark or u quark"));
		quarks.add(new Quark("Strange", "The strange quark or u quark"));
		quarks.add(new Quark("Charm", "The charm quark or u quark"));
		quarks.add(new Quark("???", null));
	}
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }
    
    @GET
    public Set<Quark> list(){
    	return quarks;
    }
    
    @POST
    public Set<Quark> add(Quark quark){
    	quarks.add(quark);
    	return quarks;
    }
    
    @DELETE
    public Set<Quark> delete(Quark quark){
    	quarks.removeIf(existingQuark -> existingQuark.name.contentEquals(quark.name));
    	return quarks;
    }
    
    public static class Quark {
    	public String name;
    	public String descripcion;
    	public Quark() {
    		
    	}
    	public Quark(String name, String descripcion) {
    		this.name = name;
    		this.descripcion = descripcion;
    	}
    }
}

