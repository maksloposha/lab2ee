package org.example.lab2ee.rest.provider;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * JAX-RS response filter that adds CORS headers to every API response.
 * This allows the REST API to be called from browser-based clients or tools
 * like Postman and Swagger UI running on different origins.
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext resp)
            throws IOException {
        resp.getHeaders().add("Access-Control-Allow-Origin", "*");
        resp.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        resp.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
    }
}
