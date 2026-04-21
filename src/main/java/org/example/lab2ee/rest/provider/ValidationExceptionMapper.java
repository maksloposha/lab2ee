package org.example.lab2ee.rest.provider;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.lab2ee.dto.ApiError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ExceptionMapper that converts Bean Validation ConstraintViolationException
 * into a structured JSON 400 Bad Request response.
 * <p>
 * Without this, Jersey would return a 400 with an HTML body.
 * With this, clients receive:
 * {
 * "status": 400,
 * "error": "Bad Request",
 * "message": "Помилка валідації даних",
 * "violations": [
 * "name: Назва страви не може бути порожньою",
 * "price: Ціна має бути більше 0"
 * ]
 * }
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        List<String> violations = ex.getConstraintViolations()
                .stream()
                .map(this::formatViolation)
                .sorted()
                .collect(Collectors.toList());

        ApiError error = new ApiError(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Bad Request",
                "Помилка валідації даних",
                violations
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }

    private String formatViolation(ConstraintViolation<?> v) {
        String path = v.getPropertyPath().toString();
        // Strip method/param prefix added by Jersey (e.g. "createMenuItem.arg0.name" → "name")
        String[] parts = path.split("\\.");
        String field = parts[parts.length - 1];
        return field + ": " + v.getMessage();
    }
}
