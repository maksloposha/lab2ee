package org.example.lab2ee.rest.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.example.lab2ee.dto.*;
import org.example.lab2ee.model.MenuItem;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.OrderItem;
import org.example.lab2ee.service.MenuService;
import org.example.lab2ee.service.OrderService;
import org.example.lab2ee.service.ServiceFactory;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST resource for Order.
 * <p>
 * Base path: /api/orders
 * <p>
 * ┌──────────────────────────────────────────────────────────────────────────┐
 * │ Method │ Path                       │ Description                        │
 * ├──────────────────────────────────────────────────────────────────────────┤
 * │ GET    │ /api/orders                │ List all (filter + pagination)      │
 * │ GET    │ /api/orders/{id}           │ Get single order                   │
 * │ POST   │ /api/orders                │ Create order (validated)   [CRUD C]│
 * │ PUT    │ /api/orders/{id}           │ Full replace               [CRUD U]│
 * │ PATCH  │ /api/orders/{id}/status    │ Update status only                 │
 * │ DELETE │ /api/orders/{id}           │ Cancel/delete order        [CRUD D]│
 * └──────────────────────────────────────────────────────────────────────────┘
 * <p>
 * Filtering (GET /api/orders):
 * ?status=PENDING        — filter by status
 * ?customerName=Олена    — filter by customer name (partial, case-insensitive)
 * ?page=1&pageSize=5     — pagination
 * ?sortBy=createdAt&sortDir=desc
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final OrderService orderService = ServiceFactory.getOrderService();
    private final MenuService menuService = ServiceFactory.getMenuService();

    // ── READ all (filter + pagination) ───────────────────────────────────────

    /**
     * GET /api/orders
     */
    @GET
    public Response getAll(
            @QueryParam("status") String status,
            @QueryParam("customerName") String customerName,
            @QueryParam("page") @DefaultValue("1") @Min(1) int page,
            @QueryParam("pageSize") @DefaultValue("10") @Min(1) @Max(100) int pageSize,
            @QueryParam("sortBy") @DefaultValue("createdAt") String sortBy,
            @QueryParam("sortDir") @DefaultValue("desc") String sortDir
    ) {
        List<Order> orders = orderService.getAllOrders();

        // ── Filtering ─────────────────────────────────────────────────────────
        if (status != null && !status.isBlank()) {
            try {
                Order.Status s = Order.Status.valueOf(status.toUpperCase());
                orders = orders.stream()
                        .filter(o -> o.getStatus() == s)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                ApiError err = new ApiError(400, "Bad Request",
                        "Невідомий статус: " + status +
                                ". Допустимі: " + Arrays.toString(Order.Status.values()));
                return Response.status(400).entity(err).build();
            }
        }

        if (customerName != null && !customerName.isBlank()) {
            String q = customerName.toLowerCase();
            orders = orders.stream()
                    .filter(o -> o.getCustomerName() != null &&
                            o.getCustomerName().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        // ── Sorting ───────────────────────────────────────────────────────────
        Comparator<Order> comparator;
        switch (sortBy.toLowerCase()) {
            case "total":
                comparator = Comparator.comparing(Order::getTotal,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "customername":
                comparator = Comparator.comparing(Order::getCustomerName,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
                break;
            default:
                comparator = Comparator.comparing(Order::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder()));
        }
        if ("asc".equalsIgnoreCase(sortDir)) {
            // default for createdAt is desc, so "asc" means natural order
        } else {
            comparator = comparator.reversed();
        }
        orders.sort(comparator);

        // ── Pagination ────────────────────────────────────────────────────────
        long total = orders.size();
        int fromIndex = (page - 1) * pageSize;
        int toIndex = (int) Math.min(fromIndex + pageSize, total);

        List<Order> pageItems = (fromIndex >= total)
                ? List.of()
                : orders.subList(fromIndex, toIndex);

        List<OrderDTO> dtos = pageItems.stream()
                .map(OrderDTO::fromModel)
                .collect(Collectors.toList());

        return Response.ok(new PagedResponse<>(dtos, page, pageSize, total)).build();
    }

    // ── READ single ──────────────────────────────────────────────────────────

    /**
     * GET /api/orders/{id}
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        return orderService.getOrderById(id)
                .map(o -> Response.ok(OrderDTO.fromModel(o)).build())
                .orElse(notFound("Замовлення з ID " + id + " не знайдено"));
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    /**
     * POST /api/orders
     * Validates body with Bean Validation (@Valid) and resolves MenuItem references.
     * Returns 201 Created or 422 Unprocessable Entity (unknown menu item IDs).
     */
    @POST
    public Response create(@Valid CreateOrderRequest req,
                           @Context UriInfo uriInfo) {

        Order order = new Order();
        order.setCustomerName(req.getCustomerName());
        order.setCustomerPhone(req.getCustomerPhone());
        order.setDeliveryAddress(req.getDeliveryAddress());
        order.setNotes(req.getNotes());

        // Resolve menu items — fail fast if any ID is unknown
        List<String> unknownIds = new ArrayList<>();
        for (CreateOrderRequest.OrderItemRequest itemReq : req.getItems()) {
            Optional<MenuItem> menuItem = menuService.getMenuItemById(itemReq.getMenuItemId());
            if (menuItem.isEmpty()) {
                unknownIds.add(String.valueOf(itemReq.getMenuItemId()));
                continue;
            }
            if (!menuItem.get().isAvailable()) {
                ApiError err = new ApiError(422, "Unprocessable Entity",
                        "Страва '" + menuItem.get().getName() + "' (ID: " +
                                itemReq.getMenuItemId() + ") зараз недоступна для замовлення");
                return Response.status(422).entity(err).build();
            }
            OrderItem oi = new OrderItem(
                    order.getItems().size() + 1,
                    menuItem.get(),
                    itemReq.getQuantity(),
                    itemReq.getSpecialInstructions() != null ? itemReq.getSpecialInstructions() : ""
            );
            order.addItem(oi);
        }

        if (!unknownIds.isEmpty()) {
            ApiError err = new ApiError(422, "Unprocessable Entity",
                    "Невідомі ID елементів меню: " + String.join(", ", unknownIds));
            return Response.status(422).entity(err).build();
        }

        Order saved = orderService.createOrder(order);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(saved.getId()))
                .build();

        return Response.created(location).entity(OrderDTO.fromModel(saved)).build();
    }

    // ── UPDATE (full replace) ─────────────────────────────────────────────────

    /**
     * PUT /api/orders/{id}
     * Replaces customer data and items. Status is NOT changed here — use PATCH /status.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id,
                           @Valid CreateOrderRequest req) {

        Optional<Order> existing = orderService.getOrderById(id);
        if (existing.isEmpty()) return notFound("Замовлення з ID " + id + " не знайдено");

        Order order = existing.get();

        // Cancelled/Delivered orders cannot be edited
        if (order.getStatus() == Order.Status.CANCELLED ||
                order.getStatus() == Order.Status.DELIVERED) {
            ApiError err = new ApiError(409, "Conflict",
                    "Замовлення у статусі '" + order.getStatus().getDisplayName() +
                            "' не може бути змінено");
            return Response.status(409).entity(err).build();
        }

        order.setCustomerName(req.getCustomerName());
        order.setCustomerPhone(req.getCustomerPhone());
        order.setDeliveryAddress(req.getDeliveryAddress());
        order.setNotes(req.getNotes());

        // Replace items
        List<OrderItem> newItems = new ArrayList<>();
        for (CreateOrderRequest.OrderItemRequest itemReq : req.getItems()) {
            Optional<MenuItem> menuItem = menuService.getMenuItemById(itemReq.getMenuItemId());
            if (menuItem.isEmpty()) {
                ApiError err = new ApiError(422, "Unprocessable Entity",
                        "Невідомий ID елемента меню: " + itemReq.getMenuItemId());
                return Response.status(422).entity(err).build();
            }
            newItems.add(new OrderItem(
                    newItems.size() + 1,
                    menuItem.get(),
                    itemReq.getQuantity(),
                    itemReq.getSpecialInstructions() != null ? itemReq.getSpecialInstructions() : ""
            ));
        }
        order.setItems(newItems);

        // We update via status update (reuses existing stub storage)
        orderService.updateOrderStatus(id, order.getStatus());
        return Response.ok(OrderDTO.fromModel(order)).build();
    }

    // ── PATCH status ──────────────────────────────────────────────────────────

    /**
     * PATCH /api/orders/{id}/status
     * Body: { "status": "PREPARING" }
     * Validates with @ValidOrderStatus custom annotation.
     */
    @PATCH
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") int id,
                                 @Valid UpdateOrderStatusRequest req) {

        Optional<Order> existing = orderService.getOrderById(id);
        if (existing.isEmpty()) return notFound("Замовлення з ID " + id + " не знайдено");

        Order order = existing.get();

        // Business rule: cannot revert from terminal states
        if (order.getStatus() == Order.Status.DELIVERED) {
            ApiError err = new ApiError(409, "Conflict",
                    "Замовлення вже доставлено. Зміна статусу неможлива.");
            return Response.status(409).entity(err).build();
        }

        Order.Status newStatus = Order.Status.valueOf(req.getStatus().toUpperCase());
        Order updated = orderService.updateOrderStatus(id, newStatus);

        return Response.ok(OrderDTO.fromModel(updated)).build();
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    /**
     * DELETE /api/orders/{id}
     * Cancels the order (sets status to CANCELLED).
     * Returns 204 No Content, or 404/409 on conflict.
     */
    @DELETE
    @Path("/{id}")
    public Response cancel(@PathParam("id") int id) {
        Optional<Order> existing = orderService.getOrderById(id);
        if (existing.isEmpty()) return notFound("Замовлення з ID " + id + " не знайдено");

        Order order = existing.get();
        if (order.getStatus() == Order.Status.DELIVERED) {
            ApiError err = new ApiError(409, "Conflict",
                    "Неможливо скасувати вже доставлене замовлення");
            return Response.status(409).entity(err).build();
        }
        if (order.getStatus() == Order.Status.CANCELLED) {
            ApiError err = new ApiError(409, "Conflict", "Замовлення вже скасовано");
            return Response.status(409).entity(err).build();
        }

        orderService.cancelOrder(id);
        return Response.noContent().build(); // 204
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private Response notFound(String message) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ApiError(404, "Not Found", message))
                .build();
    }
}
