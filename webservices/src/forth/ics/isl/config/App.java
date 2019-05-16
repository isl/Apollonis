package forth.ics.isl.config;


import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author mhalkiad
 */

public class App extends ResourceConfig {
	public App() {
        // Define the package which contains the service classes and register MultiPartFeature class.
        packages("forth.ics.isl.service").register(MultiPartFeature.class);
    }
}
