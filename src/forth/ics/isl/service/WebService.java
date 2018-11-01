package forth.ics.isl.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;



import forth.ics.isl.blazegraph.*;


import java.util.List;
import javax.ws.rs.POST;
import org.eclipse.rdf4j.query.BindingSet;



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
    
//    @GET
//    @Path("/importRDF")
//    public Response importRDF(@PathParam("name") String msg) {
//        
//        BlazegraphManager manager = new BlazegraphManager();
//
//        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");
//
//     
//        manager.importFile(System.getProperty("user.dir") + File.separator +"input.rdf", RDFFormat.RDFXML);
//
//
//        manager.closeConnectionToBlazeGraph();
//        
//        return Response.status(200).entity("File imported successfully").build();
//    }
//    
//    @GET
//    @Path("/importJSON")
//    public Response importJSON(@PathParam("name") String msg) {
//        
//        BlazegraphManager manager = new BlazegraphManager();
//
//        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");
//
//     
//        manager.importFile(System.getProperty("user.dir") + File.separator +"input.json", RDFFormat.RDFJSON);
//
//
//        manager.closeConnectionToBlazeGraph();
//        
//        return Response.status(200).entity("File imported successfully").build();
//    }
//    
//    @GET
//    @Path("/importTurtle")
//    public Response importTURTLE(@PathParam("name") String msg) {
//        
//        BlazegraphManager manager = new BlazegraphManager();
//
//        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");
//
//     
//        manager.importFile(System.getProperty("user.dir") + File.separator +"input.ttl", RDFFormat.TURTLE);
//
//
//        manager.closeConnectionToBlazeGraph();
//        
//        return Response.status(200).entity("File imported successfully").build();
//    }
    
    @POST
    @Path("/import")
    public Response importToBlazegraph(@QueryParam("file") String filename) {
        
        BlazegraphManager manager = new BlazegraphManager();

        manager.openConnectionToBlazegraph("http://139.91.183.72:8091/blazegraph/namespace/kb/sparql");

        //manager.importFile(System.getProperty("user.dir") + File.separator +"input.json", RDFFormat.RDFJSON);
        manager.importFile(filename);

        manager.closeConnectionToBlazeGraph();

        
        return Response.status(200).entity(filename).build();
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
