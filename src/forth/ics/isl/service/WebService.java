package forth.ics.isl.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import forth.ics.isl.blazegraph.*;
import forth.ics.isl.utils.PropertiesManager;
import java.io.InputStream;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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
    
    private PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
    
    @GET
    @Path("/query")
    //TODO produces
    public Response query(@QueryParam("queryString") String queryString,
                          @DefaultValue("application/json") @HeaderParam("Accept") String accept,
                          @QueryParam("namespace") String namespace,
                          @DefaultValue("0") @QueryParam("timeout") int timeout) {
        
        BlazegraphManager manager = new BlazegraphManager();
        
        String serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace.isEmpty())
            namespace = propertiesManager.getTripleStoreNamespace();
      
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
        
        List<BindingSet> results = manager.query(queryString, accept);

        String output = "Results size: " + results.size();
        
        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity(output).build();
    }
    
    
    @POST
    @Path("/import")
    @Consumes({"text/plain", "application/rdf+xml", "application/x-turtle", "text/rdf+n3"})
    public Response importToBlazegraph(InputStream file, 
                                       @DefaultValue("application/x-turtle") @HeaderParam("Content-Type") String contentType,
                                       @QueryParam("namespace") String namespace,
                                       @QueryParam("graph") String graph) {
        
        BlazegraphManager manager = new BlazegraphManager();

        String serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace.isEmpty())
            namespace = propertiesManager.getTripleStoreNamespace();
      
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
        
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
