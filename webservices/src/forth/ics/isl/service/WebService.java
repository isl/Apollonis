package forth.ics.isl.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import forth.ics.isl.blazegraph.*;
import forth.ics.isl.utils.PropertiesManager;
import forth.ics.isl.utils.ResponseStatus;
import java.io.File;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.glassfish.jersey.media.multipart.FormDataParam;


/**
 *
 * @author mhalkiad
 */

@Path("/webServices")
public class WebService {
    
    private PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
    
    @GET
    @Path("/query")
    public Response query(@QueryParam("queryString") String queryString,
                          @QueryParam("service-url") String serviceURL,
                          @HeaderParam("Content-Type") String contentType,
                          @QueryParam("namespace") String namespace,
                          @DefaultValue("0") @QueryParam("timeout") int timeout) {
        
        BlazegraphManager manager = new BlazegraphManager();
        
        if(serviceURL == null)
            serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace == null)
            namespace = propertiesManager.getTripleStoreNamespace();
      
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
        
        ResponseStatus responseStatus = manager.query(queryString, contentType, timeout);
        
        manager.closeConnectionToBlazeGraph();
        
        // Adding Access-Control-Allow-Origin to the header in order to resolve the CORS issue between modern browsers and server
        return Response.status(responseStatus.getStatus()).entity(responseStatus.getResponse()).header("Access-Control-Allow-Origin", "*").build();
    }
    
    
    @POST
    @Path("/import")
    public Response importToBlazegraph(@QueryParam ("file") String file,
                                       @QueryParam("service-url") String serviceURL,
                                       @HeaderParam("Content-Type") String contentType,
                                       @QueryParam("namespace") String namespace,
                                       @DefaultValue("") @QueryParam("graph") String graph) {

        BlazegraphManager manager = new BlazegraphManager();

        if(serviceURL == null)
            serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace == null)
            namespace = propertiesManager.getTripleStoreNamespace();
              
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
        
        RDFFormat format = Rio.getParserFormatForMIMEType(contentType).get();
        
        ResponseStatus responseStatus = manager.importFile(file, format, graph);

        manager.closeConnectionToBlazeGraph();

        return Response.status(responseStatus.getStatus()).entity(responseStatus.getResponse()).header("Access-Control-Allow-Origin", "*").build();
       // return Response.status(responseStatus.getStatus()).entity(responseStatus.getResponse()).build();
    }
    
    
    @POST
    @Path("/update")
    public Response update(@QueryParam("update") String updateMsg,
                           @QueryParam("namespace") String namespace,
                           @QueryParam("service-url") String serviceURL) {
        
        BlazegraphManager manager = new BlazegraphManager();

        if(serviceURL == null)
            serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace == null)
            namespace = propertiesManager.getTripleStoreNamespace();
      
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
     
        manager.updateQuery(updateMsg);

        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity("Successfully updated").header("Access-Control-Allow-Origin", "*").build();
        //return Response.status(200).entity("Updated!!").build();
    }
    
    
    @GET
    @Path("/export")
    public Response export(@QueryParam("filename") String filename, 
                                       @QueryParam("service-url") String serviceURL,
                                       @HeaderParam("Accept") String format,
                                       @QueryParam("namespace") String namespace,
                                       @DefaultValue("") @QueryParam("graph") String graph) 
    {
        
        
        BlazegraphManager manager = new BlazegraphManager();

        if(serviceURL == null)
            serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace == null)
            namespace = propertiesManager.getTripleStoreNamespace();
      
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
        
        RDFFormat rdfFormat = Rio.getParserFormatForMIMEType(format).get();
         
        ResponseStatus responseStatus = manager.exportFile(filename, namespace, graph, rdfFormat);
    
        manager.closeConnectionToBlazeGraph();
        
        if(responseStatus.getStatus() == 200) {
            String filepath = "/opt/tomcat/apache-tomcat-8.0.53/bin/" + responseStatus.getResponse();
            File file = new File(filepath);
            ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition","attachment; filename=" + responseStatus.getResponse());
            return response.build();
        }
        
        return Response.status(responseStatus.getStatus()).entity(responseStatus.getResponse()).header("Access-Control-Allow-Origin", "*").build();
        //return Response.status(responseStatus.getStatus()).entity(responseStatus.getResponse()).build();
    }  	
}






