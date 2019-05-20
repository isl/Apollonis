package forth.ics.isl.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
*
* @author Vangelis Kritsotakis
* 
* Here are a few Java Jersey Client implementation examples (one for each service)
*/

public class WebServiceClient {

	private static final String REST_URI = "http://localhost:8080/WebServices";
	
	public static void main(String... args) {
		
		// Executing SPARQL Query
		String stringResponse = getSparqlQueryResults(REST_URI + "/webServices/query", "select * where {?s ?p ?o} limit 100", 
				100, "test2", "application/json");
		System.out.println("Response:");
		System.out.println(stringResponse);
	}
	
	/**
	 * Returns a string representation of the output when calling a service that executes a SPARQL query on the triple store.
	 * 
	 * @param  relativeUrl  		A String representation of the absolute URL of the service.
	 * @param  timeout 				An int representing the timeout in milliseconds.
	 * @param  namespace 			The name of the namespace where the query will be executed
	 * @param  outputContentType	The content type of the output
	 * @return      the image at the specified URL
	 */
    public static String getSparqlQueryResults(String url, String query, int timeout, String namespace, String outputContentType) {
    	Client client = ClientBuilder.newClient();
    	Response response;
    	String strRes;
		try {
			response = client.target(url)
 			  .queryParam("queryString", URLEncoder.encode(query, StandardCharsets.UTF_8.name()).replace("+", "%20"))
 			  .queryParam("timeout", timeout)
 			  .queryParam("namespace", namespace)
 			  .request()
 			  .header("Content-Type", outputContentType)
 			  .get();
			strRes = response.readEntity(String.class);
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			strRes = "There were error while executing the SPARQL query";
		}
        client.close();
        return strRes;
    }
}
