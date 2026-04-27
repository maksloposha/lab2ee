package org.example.lab2ee.rest.provider;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.lab2ee.dto.ApiError;

/**
 * Catch-all ExceptionMapper.
 * <p>
 * - WebApplicationException (e.g. 404 from Response.status(404).build()) is
 * passed through as-is, but re-wrapped with a JSON body.
 * - Any other Throwable becomes a 500 Internal Server Error.
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        if (ex instanceof WebApplicationException wae) {
            Response original = wae.getResponse();
            int status = original.getStatus();

            ApiError error = new ApiError(
                    status,
                    Response.Status.fromStatusCode(status) != null
                            ? Response.Status.fromStatusCode(status).getReasonPhrase()
                            : "Error",
                    ex.getMessage() != null ? ex.getMessage() : "Помилка"
            );

            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error)
                    .build();
        }

        // Unexpected server error
        ApiError error = new ApiError(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Internal Server Error",
                "Внутрішня помилка сервера. Спробуйте пізніше."
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
