package forth.ics.isl.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;

import eu.delving.x3ml.X3MLEngineFactory;
import forth.ics.isl.blazegraph.BlazegraphManager;
import forth.ics.isl.utils.ResponseStatus;

/**
*
* @author Vangelis Kritsotakis
*/
@Path("/transform")
public class X3mlToRDFTransformService {

	@POST
    @Path("/x3mltoRdf")
    public Response x3mltoRdftransform(@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
    								   @FormDataParam("inputFileStream") List<FormDataBodyPart> inputFormDataBodyPartList,
    								   @FormDataParam("x3mlFileStream") List<FormDataBodyPart> x3mlFormDataBodyPartList,
    								   @FormDataParam("generatorPolicyFileStream") InputStream  generatorPolicyFileStream,
                                       @QueryParam("inputFilePath") List<String> inputFilePathList,
    								   @QueryParam("x3mlFilePath") List<String> x3mlFilePathList,
    								   @QueryParam("generatorPolicyFilePath") String generatorPolicyFilePath,
    								   @QueryParam("inputFileUrl") List<String> inputFileUrlList,
    								   @QueryParam("x3mlFileUrl") List<String> x3mlFileUrlList,
    								   @QueryParam("generatorPolicyFileUrl") String generatorPolicyFileUrl,
    								   @DefaultValue("rdf-xml") @QueryParam("outputContentType") String outputContentType) {
		
		JSONObject message = new JSONObject();
		int status = 0;
		boolean proceed = true;
		
		try {
			// Been Optimistic in advance
			message.put("message", "Completed Succesfully");
			message.put("status", "SUCCEED");
			status = 200;
			
			X3MLEngineFactory x3MLEngineFactory = X3MLEngineFactory.create();
			
			// Initialize the FormDataBodyPart lists in case of not been set
			if(inputFormDataBodyPartList == null)
				inputFormDataBodyPartList = new ArrayList<FormDataBodyPart>();
			if(x3mlFormDataBodyPartList == null)
				x3mlFormDataBodyPartList = new ArrayList<FormDataBodyPart>();
			
			// Source Input Files
			if(proceed) {
				if(inputFormDataBodyPartList.size() > 0 || inputFilePathList.size() > 0 || inputFileUrlList.size() > 0) {
					// InputStreams
					inputFormDataBodyPartList.forEach(inputFormDataBodyPart -> {
						InputStream inputFileStream = inputFormDataBodyPart.getEntityAs(InputStream.class);
						x3MLEngineFactory.withInput(inputFileStream);
					});
					// FilePaths
					inputFilePathList.forEach(filePath -> {
					    x3MLEngineFactory.withInputFiles(new File(filePath));
					});
					// FileURLs
					inputFileUrlList.forEach(fileUrlStr -> {
					    System.out.println(fileUrlStr);
						try {
							URL fileUrl = new URL(fileUrlStr);
							x3MLEngineFactory.withInput(fileUrl);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					});
				}
				else {
					message.put("message", "Please use at least one \"inputFileStream\" \"inputFilePath\" or \"inputFileUrl\" param.");
					message.put("status", "FAILED");
					status = 406;
					proceed = false;
				}
			}
			
			// X3ML Files
			if(proceed) {
				if(x3mlFormDataBodyPartList.size() > 0 || x3mlFilePathList.size() > 0 || x3mlFileUrlList.size() > 0) {
					// InputStreams
					x3mlFormDataBodyPartList.forEach(x3mlFormDataBodyPart -> {
						InputStream x3mlFileStream = x3mlFormDataBodyPart.getEntityAs(InputStream.class);
						System.out.println(x3mlFileStream);
						x3MLEngineFactory.withMappings(x3mlFileStream);
					});

					// FilePaths
					x3mlFilePathList.forEach(filePath -> {
					    System.out.println(filePath);
					    x3MLEngineFactory.withMappings(new File(filePath));
					});

					// FileURLs
					x3mlFileUrlList.forEach(fileUrlStr -> {
					    System.out.println(fileUrlStr);
						try {
							URL fileUrl = new URL(fileUrlStr);
							x3MLEngineFactory.withMappings(fileUrl);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					});
				}
				else {
					message.put("message", "Please use at least one \"x3mlFileStream\" \"x3mlFilePath\" or \"x3mlFileUrl\" param.");
					message.put("status", "FAILED");
					status = 406;
					proceed = false;
				}
			}
			
			// Generator Policy File
			if(proceed) {
				// Only one generator policy is allowed 
				// (priority Order: i. InputStream, ii. FilePath iii.FileURL)
				
				// InputStream
				//if(generatorPolicyFormDataBodyPart != null) {
				if(generatorPolicyFileStream != null) {
					//InputStream generatorPolicyFileStream = generatorPolicyFormDataBodyPart.getEntityAs(InputStream.class);
					System.out.println(generatorPolicyFileStream);
					x3MLEngineFactory.withGeneratorPolicy(generatorPolicyFileStream);
				}
				// FilePath
				else if(generatorPolicyFilePath != null) {
					System.out.println(generatorPolicyFilePath);
					x3MLEngineFactory.withGeneratorPolicy(new File(generatorPolicyFilePath));
				}
				// FileURL
				else if(generatorPolicyFileUrl != null) { // generatorPolicyFileUrl is a String
					System.out.println(generatorPolicyFileUrl);
					try {
						// generatorPolicyFileUrl is a String
						URL fileUrl = new URL(generatorPolicyFileUrl);
						x3MLEngineFactory.withGeneratorPolicy(fileUrl);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
				
				else {
					message.put("message", "Please use either the \"generatorPolicyFilePath\" or \"generatorPolicyFileUrl\" param.");
					message.put("status", "FAILED");
					status = 406;
					proceed = false;
				}
			}
			
			if(proceed) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				if(outputContentType.equals("rdf-xml"))
					x3MLEngineFactory.withOutput(byteArrayOutputStream, X3MLEngineFactory.OutputFormat.RDF_XML);
				else if(outputContentType.equals("turtle"))
					x3MLEngineFactory.withOutput(byteArrayOutputStream, X3MLEngineFactory.OutputFormat.TURTLE);
				else
					x3MLEngineFactory.withOutput(byteArrayOutputStream, X3MLEngineFactory.OutputFormat.RDF_XML);
				x3MLEngineFactory.execute();
				String output = byteArrayOutputStream.toString();
				message.put("output", output.replace("\n", "").replace("\r", ""));
				//message.put("output", output);
				
				String errors = "";
				if(!eu.delving.x3ml.X3MLEngine.exceptionMessagesList.equals("")) {
					errors = eu.delving.x3ml.X3MLEngine.exceptionMessagesList;
					message.put("errorMessage", errors);
				}
				System.out.println();
				System.out.println("output:");
				System.out.println(output);
			}

		} catch (Exception ex) {
			message.put("message", "Failed to complete");
			message.put("errorMessage", ex.getMessage());
			message.put("status", "FAILED");
			status = 500;
		}
				
        return Response.status(status).entity(message.toString()).header("Access-Control-Allow-Origin", "*").build();
    }
	
}
