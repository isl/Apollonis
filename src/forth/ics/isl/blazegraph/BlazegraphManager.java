/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forth.ics.isl.blazegraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.eclipse.rdf4j.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;


/**
 *
 * @author mhalkiad
 */

public class BlazegraphManager {
    
    private Repository repo;

    
    public void openConnectionToBlazegraph(String sparqlEndPoint) {
        
        repo = new SPARQLRepository(sparqlEndPoint);
        repo.initialize();
        
    }
    
    
    public void closeConnectionToBlazeGraph() {
        
        repo.getConnection().close();

    }
    
    
    public List<BindingSet> selectAllQuery() {
        
        List<BindingSet> retList= new ArrayList<>();
        TupleQuery tupleQuery;
        
        try (RepositoryConnection conn = repo.getConnection()) {
            
            String queryString = "Select * where {?s ?p ?o}";
            tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            
            try(TupleQueryResult tupleQueryResult = tupleQuery.evaluate()) {
                while(tupleQueryResult.hasNext()) {
                    retList.add(tupleQueryResult.next());
                }
            }
        } 
        exportToFile(tupleQuery, RDFFormat.RDFJSON);
   
        return retList;
          
    }
    
    public void exportToFile(TupleQuery tupleQuery, RDFFormat dataFormat)
    {
        if(dataFormat.equals(RDFFormat.RDFJSON)) {
            try( OutputStream out = new FileOutputStream(System.getProperty("user.dir") + File.separator +"output.json")) {
                    tupleQuery.evaluate(new SPARQLResultsJSONWriter(out));

                } catch(QueryEvaluationException e) {
                    System.out.println("QueryEvaluationException!");

            }
            catch (FileNotFoundException fnfe) {
                System.out.println("FileNotFoundException!!");

            } catch (IOException ex) {
                Logger.getLogger(BlazegraphManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(dataFormat.equals(RDFFormat.RDFXML)) {
             try( OutputStream out = new FileOutputStream(System.getProperty("user.dir") + File.separator +"output.xml")) {
                    tupleQuery.evaluate(new SPARQLResultsXMLWriter(out));

                } catch(QueryEvaluationException e) {
                    System.out.println("QueryEvaluationException!");

            }
            catch (FileNotFoundException fnfe) {
                System.out.println("FileNotFoundException!!");

            } catch (IOException ex) {
                Logger.getLogger(BlazegraphManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
    }
    
    
    public List<BindingSet> query(String queryString) {
        
         List<BindingSet> retList= new ArrayList<>();
        TupleQuery tupleQuery;
        
        try (RepositoryConnection conn = repo.getConnection()) {
            
         //   String queryString = "Select * where {?s ?p ?o}";
            tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            
            try(TupleQueryResult tupleQueryResult = tupleQuery.evaluate()) {
                while(tupleQueryResult.hasNext()) {
                    retList.add(tupleQueryResult.next());
                }
            }
        } 
        exportToFile(tupleQuery, RDFFormat.RDFJSON);

        return retList;
    }
    
    
    public void importFile(InputStream file, RDFFormat format, String graph) {
      
        try (RepositoryConnection con = repo.getConnection()) {

        con.begin();
        try {
            ValueFactory factory = SimpleValueFactory.getInstance();
            IRI graphIRI = factory.createIRI(graph);
            
            con.add(file, graph, format, graphIRI);
            con.commit();
        }
        catch (RepositoryException e) {
      
            con.rollback();
            }catch (IOException ex) {
                Logger.getLogger(BlazegraphManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RDFParseException ex) {
                Logger.getLogger(BlazegraphManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    
    private RDFFormat getRDFFormat(String filename)
    {
       String extension = FilenameUtils.getExtension(filename);
       if(extension.equals("rdf"))
           return RDFFormat.RDFXML;
       else if(extension.equals("nt"))
           return RDFFormat.NTRIPLES;
       else if(extension.equals("ttl"))
           return RDFFormat.TURTLE;
       else if(extension.endsWith("n3"))
           return RDFFormat.N3;
       else
           return null;
    }
    
    
    
    public void updateQuery(String queryString)
    {
        Update update = repo.getConnection().prepareUpdate(QueryLanguage.SPARQL, queryString);
        update.execute();
    }
}