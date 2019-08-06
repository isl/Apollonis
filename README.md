Implementation of Java Maven RESTful web services for importing, searching, updating and exporting digital content 
to/from RDF triplestore database.

The Web services are implemented using the Jersey 2.27 API and RDF4J 2.4.0 library to connect to the triplestore database.
The web server is Apache Tomcat 8.0.53 and Blazegraph 2.1.4 has been used as a triplestore. But, it is possible to use any 
other RDF databases as well, since its URL is specified in the properties file (config.properties). 
In addition, all the necessary web parameters - such as namespace, graph, content types, e.t.c. - can be set in this 
property file.
