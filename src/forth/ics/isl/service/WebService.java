package forth.ics.isl.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import forth.ics.isl.blazegraph.*;
import java.io.InputStream;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;


/**
 *
 * @author mhalkiad
 */

@Path("/webServices")
public class WebService {

    @GET
    @Path("/selectAll")
        //TODO delete
    public Response selectAll(@PathParam("name") String msg) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");

     
        List<BindingSet> results = manager.selectAllQuery();

        
        String output = "Results size: " + results.size();
        

        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity(output).build();
    }
    
    
    @GET
    @Path("/query")
    public Response query(@QueryParam("queryString") String queryString) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");

     
        List<BindingSet> results = manager.query(queryString);

        
        String output = "Results size: " + results.size();
        
        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity(output).build();
    }
    
    
    @POST
    @Path("/import")
    @Consumes({"text/plain", "application/rdf+xml", "application/x-turtle", "ext/rdf+n3", "application/json"})
    public Response importToBlazegraph(InputStream file, 
                                       @HeaderParam("Content-Type") String contentType,
                                       @QueryParam("namespace") String namespace,
                                       @QueryParam("graph") String graph) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/" + namespace + "/sparql");
        
        RDFFormat format = Rio.getParserFormatForMIMEType(contentType).get();

        //manager.importFile(System.getProperty("user.dir") + File.separator +"input.json", RDFFormat.RDFJSON);
        manager.importFile(file, format, graph);

        manager.closeConnectionToBlazeGraph();

        return Response.status(200).entity("Imported successfully!").build();
    }
    
    
    @GET
    @Path("/update")
    public Response update(@QueryParam("update") String updateMsg) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");
     
        manager.updateQuery(updateMsg);

        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity("Updated!!").build();
    }
   	
}
