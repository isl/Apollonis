package forth.ics.isl.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import forth.ics.isl.blazegraph.*;
import java.io.File;

import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.rio.RDFFormat;


/**
 *
 * @author mhalkiad
 */

@Path("/webServices")
public class WebService {

    @GET
    @Path("/selectAll")
    public Response selectAll(@PathParam("name") String msg) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");

     
        List<BindingSet> results = manager.selectAllQuery();

        
        String output = "Results size: " + results.size();
        

        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity(output).build();
    }
    
    
    @GET
    @Path("/{name}")
    public Response query(@PathParam("name") String msg) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");

     
        List<BindingSet> results = manager.query(msg);

        
        String output = "Results size: " + results.size();
        

        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity(output).build();
    }
    
    @GET
    @Path("/importRDF")
    public Response importRDF(@PathParam("name") String msg) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");

     
        manager.importFile(System.getProperty("user.dir") + File.separator +"input.rdf", RDFFormat.RDFXML);


        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity("File imported successfully").build();
    }
    
    @GET
    @Path("/importJSON")
    public Response importJSON(@PathParam("name") String msg) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");

     
        manager.importFile(System.getProperty("user.dir") + File.separator +"input.json", RDFFormat.RDFJSON);


        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity("File imported successfully").build();
    }
    
    @GET
    @Path("/importTURTLE")
    public Response importTURTLE(@PathParam("name") String msg) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");

     
        manager.importFile(System.getProperty("user.dir") + File.separator +"input.ttl", RDFFormat.TURTLE);


        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity("File imported successfully").build();
    }
	
}
