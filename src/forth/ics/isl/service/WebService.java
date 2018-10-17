package forth.ics.isl.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import forth.ics.isl.blazegraph.*;

import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;


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
	
}
