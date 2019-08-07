Implementation of Java Maven RESTful web services for importing, searching, updating and exporting digital content 
to/from RDF triplestore database.

The Web services are implemented using the Jersey 2.27 API and RDF4J 2.4.0 library to connect to the triplestore database.
The web server is Apache Tomcat 8.0.53 and Blazegraph 2.1.4 has been used as a triplestore. But, it is possible to use any 
other RDF databases as well, since its URL is specified in the properties file (config.properties). 
In addition, all the necessary web parameters - such as namespace, graph, content types, e.t.c. - can be set in this 
property file.

The operations that are provided to RDF data are:

1)  Insert new RDF data into triplestore (import): The RDF data is in a file that is in RDF format. 
Within the triplestore the data is inserted into a specific namespace and graph that are passed as parameters 
to the web service.
The allowed formats of data are:
    • RDFFormat.BINARY	    [application/x-binary-rdf]
    • RDFFormat.JSONLD		[application/ld+json]
    • RDFFormat.N3		    [text/n3, text/rdf+n3]
    • RDFFormat.NQUADS	    [application/n-quads, text/x-nquads, text/nquads]
    • RDFFormat.NTRIPLES	[application/n-triples, text/plain]
    • RDFFormat.RDFA		[application/xhtml+xml,application/html, text/html]
    • RDFFormat.RDFJSON	    [application/rdf+json]
    • RDFFormat.RDFXML	    [application/rdf+xml, application/xml, text/xml]
    • RDFFormat.TRIG		[application/trig, application/x-trig]
    • RDFFormat.TRIX		[application/trix]
    • RDFFormat.TURTLE	    [text/turtle, application/x-turtle]

For successful import, the web service returns a success message, otherwise, an error message is returned.

2) Query content to triplestore (query): This web service executes a SPARQL query to retrieve information from the RDF
triplestore database in a particular format. The parameters of the web service are: a string  with the  SPARQL query. 
Another parameter is the format of the returned results (content-type) is: CSV, JSON, SPARQL / XML, and TSV. 
Additional parameters are the namespace from which we want the content to be retrieved and the maximum timeout that allowed 
in order to complete the whole process and return the data. 

For successful querying, the web service returns the data from the triplestore in the desired format or an error message 
is returned.
