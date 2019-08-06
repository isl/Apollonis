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
    • RDFFormat.N3		[text/n3, text/rdf+n3]
    • RDFFormat.NQUADS	[application/n-quads, text/x-nquads, text/nquads]
    • RDFFormat.NTRIPLES	[application/n-triples, text/plain]
    • RDFFormat.RDFA		[application/xhtml+xml,application/html, text/html]
    • RDFFormat.RDFJSON	[application/rdf+json]
    • RDFFormat.RDFXML	[application/rdf+xml, application/xml, text/xml]
    • RDFFormat.TRIG		[application/trig, application/x-trig]
    • RDFFormat.TRIX		[application/trix]
    • RDFFormat.TURTLE	[text/turtle, application/x-turtle]

