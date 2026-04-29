package org.example.lab2ee.rest.resource;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.example.lab2ee.model.Review;
import org.example.lab2ee.model.User;
import org.example.lab2ee.service.ReviewService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Path("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource {

    @EJB
    private ReviewService reviewService;

    @Context
    private ContainerRequestContext requestContext;

    @GET
    public Response getReviews(
            @QueryParam("orderId")    Integer orderId,
            @QueryParam("minRating")  Integer minRating) {

        List<Review> reviews;

        if (orderId != null) {
            reviews = reviewService.getReviewsByOrder(orderId);
        } else if (minRating != null) {
            reviews = reviewService.getReviewsByMinRating(minRating);
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Вкажіть параметр orderId або minRating\"}")
                    .build();
        }

        return Response.ok(reviews.stream()
                .map(this::toDTO)
                .collect(Collectors.toList()))
                .build();
    }

    @POST
    public Response addReview(Map<String, Object> body,
                              @Context jakarta.servlet.http.HttpServletRequest httpRequest) {

        User user = (User) httpRequest.getSession(false) != null
                ? (User) httpRequest.getSession(false)
                          .getAttribute(org.example.lab2ee.controller.AuthServlet.SESSION_USER_KEY)
                : (User) requestContext.getProperty("currentUser");

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Потрібна авторизація\"}")
                    .build();
        }

        try {
            int orderId = ((Number) body.get("orderId")).intValue();
            int rating  = ((Number) body.get("rating")).intValue();
            String comment = (String) body.getOrDefault("comment", "");

            Review review = reviewService.addReview(orderId, user, rating, comment);
            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(review))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (IllegalStateException e) {
            // Rollback — замовлення не доставлено або відгук вже є
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"" + e.getMessage() + "\"," +
                            "\"note\":\"Транзакція відкочена — дані не збережено\"}")
                    .build();
        }
    }


    @DELETE
    @Path("/{id}")
    public Response deleteReview(@PathParam("id") int id,
                                 @Context jakarta.servlet.http.HttpServletRequest httpRequest) {

        User user = (User) httpRequest.getSession(false) != null
                ? (User) httpRequest.getSession(false)
                          .getAttribute(org.example.lab2ee.controller.AuthServlet.SESSION_USER_KEY)
                : (User) requestContext.getProperty("currentUser");

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Потрібна авторизація\"}")
                    .build();
        }

        try {
            boolean deleted = reviewService.deleteReview(id, user);
            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Відгук не знайдено\"}")
                        .build();
            }
            return Response.noContent().build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }


    private Map<String, Object> toDTO(Review r) {
        return Map.of(
            "id",        r.getId(),
            "orderId",   r.getOrder().getId(),
            "userId",    r.getUser().getId(),
            "userName",  r.getUser().getFullName(),
            "rating",    r.getRating(),
            "comment",   r.getComment() != null ? r.getComment() : "",
            "createdAt", r.getCreatedAt().toString()
        );
    }
}