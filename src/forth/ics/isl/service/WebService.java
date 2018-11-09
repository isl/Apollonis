package forth.ics.isl.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import forth.ics.isl.blazegraph.*;
import forth.ics.isl.utils.PropertiesManager;
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


/**
 *
 * @author mhalkiad
 */

@Path("/webServices")
public class WebService {
    
    private PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
    
    @GET
    @Path("/query")
    @Produces({"text/csv", "application/json", "application/sparql-results+json", "application/sparql-results+xml", "application/xml", "text/tab-separated-values"})
    //TODO changes to HeaderParam
    public Response query(@QueryParam("queryString") String queryString,
                          @DefaultValue("application/json") @QueryParam("content-type") String contentType,
                          @QueryParam("namespace") String namespace,
                          @DefaultValue("0") @QueryParam("timeout") int timeout) {
        
        BlazegraphManager manager = new BlazegraphManager();
        
        String serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace == null)
            namespace = propertiesManager.getTripleStoreNamespace();
      
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
        
        String output = manager.query(queryString, contentType, timeout);
        
        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity(output).build();
    }
    
    
    @POST
    @Path("/import")
    @Consumes({"text/plain", "application/rdf+xml", "application/x-turtle", "text/rdf+n3"})
    //TODO changes to HeaderParam
    public Response importToBlazegraph(InputStream file, 
                                       @DefaultValue("text/plain") @QueryParam("Content-Type") String contentType,
                                       @QueryParam("namespace") String namespace,
                                       @DefaultValue("") @QueryParam("graph") String graph) {

        BlazegraphManager manager = new BlazegraphManager();

        String serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace == null)
            namespace = propertiesManager.getTripleStoreNamespace();
      
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
        
        RDFFormat format = Rio.getParserFormatForMIMEType(contentType).get();
         
        //manager.importFile(System.getProperty("user.dir") + File.separator +"input.json", RDFFormat.RDFJSON);
        manager.importFile(file, format, graph);

        manager.closeConnectionToBlazeGraph();

        return Response.status(200).entity("Imported successfully!").build();
    }
    
    
    @POST
    @Path("/update")
    //TODO testing
    public Response update(@QueryParam("update") String updateMsg,
                           @QueryParam("namespace") String namespace) {
        
        BlazegraphManager manager = new BlazegraphManager();

        String serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace == null)
            namespace = propertiesManager.getTripleStoreNamespace();
      
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
     
        manager.updateQuery(updateMsg);

        manager.closeConnectionToBlazeGraph();
        
        return Response.status(200).entity("Updated!!").build();
    }
    
    
    @GET
    @Path("/export")
    @Produces({"text/n3", "application/n-quads", "text/nquads", 
               "application/n-triples", "text/plain",
               "application/trig", "application/x-trig", "application/trix",
               "text/turtle", "application/x-turtle" })
    //TODO changes to HeaderParam
    public Response export(@QueryParam("filename") String filename, 
                                       @DefaultValue("text/plain") @QueryParam("format") String format,
                                       @QueryParam("namespace") String namespace,
                                       @DefaultValue("") @QueryParam("graph") String graph) 
    {
        BlazegraphManager manager = new BlazegraphManager();

        String serviceURL = propertiesManager.getTripleStoreUrl();
        
        if(namespace == null)
            namespace = propertiesManager.getTripleStoreNamespace();
      
        manager.openConnectionToBlazegraph(serviceURL + "/namespace/" + namespace + "/sparql");
        
        RDFFormat rdfFormat = Rio.getParserFormatForMIMEType(format).get();
         
        String f = manager.exportFile(filename, namespace, graph, rdfFormat);
    
        manager.closeConnectionToBlazeGraph();
        
        //TODO changes not hardcoded & remove from tomcat
        String filepath = "/opt/tomcat/apache-tomcat-8.0.53/bin/" + f;
        File file = new File(filepath);
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition","attachment; filename=" + f);
        
        
        return response.build();

        //return Response.status(200).entity("Exported successfully!").build();
    }
   	
}
