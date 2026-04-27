package org.example.lab2ee.rest;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;


@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages(
                "org.example.lab2ee.rest",
                "org.example.lab2ee.rest.resource",
                "org.example.lab2ee.rest.provider"
        );

        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);

        register(org.glassfish.jersey.jackson.JacksonFeature.class);
    }
}
