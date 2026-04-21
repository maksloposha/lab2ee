package org.example.lab2ee.rest.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.example.lab2ee.dto.ApiError;
import org.example.lab2ee.dto.CreateMenuItemRequest;
import org.example.lab2ee.dto.MenuItemDTO;
import org.example.lab2ee.dto.PagedResponse;
import org.example.lab2ee.model.MenuItem;
import org.example.lab2ee.service.MenuService;
import org.example.lab2ee.service.ServiceFactory;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST resource for MenuItem.
 * <p>
 * Base path: /api/menu-items
 * <p>
 * ┌──────────────────────────────────────────────────────────────────────┐
 * │ Method  │ Path                    │ Description                      │
 * ├──────────────────────────────────────────────────────────────────────┤
 * │ GET     │ /api/menu-items         │ List all (filter + pagination)   │
 * │ GET     │ /api/menu-items/{id}    │ Get single item                  │
 * │ POST    │ /api/menu-items         │ Create new item          [CRUD C]│
 * │ PUT     │ /api/menu-items/{id}    │ Full update              [CRUD U]│
 * │ PATCH   │ /api/menu-items/{id}/availability │ Toggle availability   │
 * │ DELETE  │ /api/menu-items/{id}    │ Delete item              [CRUD D]│
 * │ GET     │ /api/menu-items/categories │ List all categories          │
 * └──────────────────────────────────────────────────────────────────────┘
 * <p>
 * Filtering params (GET /api/menu-items):
 * ?category=MAIN_COURSE   — filter by category
 * ?available=true         — only available items
 * ?search=паста           — name/description contains (case-insensitive)
 * ?page=1&pageSize=5      — pagination
 * ?sortBy=price&sortDir=asc — sorting
 */
@Path("/menu-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MenuItemResource {

    private final MenuService menuService = ServiceFactory.getMenuService();

    // ── READ all (with filter + pagination) ───────────────────────────────────

    /**
     * GET /api/menu-items
     * <p>
     * Query params:
     * category  - MenuItem.Category name (optional)
     * available - true/false (optional)
     * search    - text search in name+description (optional)
     * page      - page number, default 1
     * pageSize  - items per page, default 10, max 100
     * sortBy    - field to sort by: name | price | calories (default: name)
     * sortDir   - asc | desc (default: asc)
     */
    @GET
    public Response getAll(
            @QueryParam("category") String category,
            @QueryParam("available") Boolean available,
            @QueryParam("search") String search,
            @QueryParam("page") @DefaultValue("1") @Min(1) int page,
            @QueryParam("pageSize") @DefaultValue("10") @Min(1) @Max(100) int pageSize,
            @QueryParam("sortBy") @DefaultValue("name") String sortBy,
            @QueryParam("sortDir") @DefaultValue("asc") String sortDir
    ) {
        List<MenuItem> items = menuService.getAllMenuItems();

        // ── Filtering ─────────────────────────────────────────────────────────
        if (category != null && !category.isBlank()) {
            try {
                MenuItem.Category cat = MenuItem.Category.valueOf(category.toUpperCase());
                items = items.stream()
                        .filter(i -> i.getCategory() == cat)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                ApiError err = new ApiError(400, "Bad Request",
                        "Невідома категорія: " + category +
                                ". Допустимі: " + Arrays.toString(MenuItem.Category.values()));
                return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
            }
        }

        if (available != null) {
            boolean avail = available;
            items = items.stream()
                    .filter(i -> i.isAvailable() == avail)
                    .collect(Collectors.toList());
        }

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            items = items.stream()
                    .filter(i -> (i.getName() != null && i.getName().toLowerCase().contains(q))
                            || (i.getDescription() != null && i.getDescription().toLowerCase().contains(q)))
                    .collect(Collectors.toList());
        }

        // ── Sorting ───────────────────────────────────────────────────────────
        Comparator<MenuItem> comparator;
        switch (sortBy.toLowerCase()) {
            case "price":
                comparator = Comparator.comparing(MenuItem::getPrice,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "calories":
                comparator = Comparator.comparingInt(MenuItem::getCalories);
                break;
            default:
                comparator = Comparator.comparing(MenuItem::getName,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
        }
        if ("desc".equalsIgnoreCase(sortDir)) comparator = comparator.reversed();
        items.sort(comparator);

        // ── Pagination ────────────────────────────────────────────────────────
        long total = items.size();
        int fromIndex = (page - 1) * pageSize;
        if (fromIndex >= total && total > 0) {
            ApiError err = new ApiError(400, "Bad Request",
                    "Сторінка " + page + " не існує. Всього сторінок: " +
                            (int) Math.ceil((double) total / pageSize));
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }
        int toIndex = (int) Math.min(fromIndex + pageSize, total);
        List<MenuItem> pageItems = (fromIndex >= total) ? List.of() : items.subList(fromIndex, toIndex);

        List<MenuItemDTO> dtos = pageItems.stream()
                .map(MenuItemDTO::fromModel)
                .collect(Collectors.toList());

        PagedResponse<MenuItemDTO> response = new PagedResponse<>(dtos, page, pageSize, total);
        return Response.ok(response).build();
    }

    // ── READ single ──────────────────────────────────────────────────────────

    /**
     * GET /api/menu-items/{id}
     * Returns 200 OK with the item, or 404 Not Found.
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        return menuService.getMenuItemById(id)
                .map(item -> Response.ok(MenuItemDTO.fromModel(item)).build())
                .orElse(notFound("Елемент меню з ID " + id + " не знайдено"));
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    /**
     * POST /api/menu-items
     * Body: CreateMenuItemRequest (validated)
     * Returns 201 Created with Location header and the new item.
     */
    @POST
    public Response create(@Valid CreateMenuItemRequest req,
                           @Context UriInfo uriInfo) {
        MenuItem item = buildFromRequest(req, new MenuItem());
        MenuItem saved = menuService.createMenuItem(item);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(saved.getId()))
                .build();

        return Response.created(location)
                .entity(MenuItemDTO.fromModel(saved))
                .build();
    }

    // ── UPDATE (full replace) ─────────────────────────────────────────────────

    /**
     * PUT /api/menu-items/{id}
     * Body: CreateMenuItemRequest (validated)
     * Returns 200 OK with updated item, or 404.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id,
                           @Valid CreateMenuItemRequest req) {
        Optional<MenuItem> existing = menuService.getMenuItemById(id);
        if (existing.isEmpty()) return notFound("Елемент меню з ID " + id + " не знайдено");

        MenuItem item = buildFromRequest(req, existing.get());
        item.setId(id);
        MenuItem updated = menuService.updateMenuItem(item);

        return Response.ok(MenuItemDTO.fromModel(updated)).build();
    }

    // ── PATCH availability ────────────────────────────────────────────────────

    /**
     * PATCH /api/menu-items/{id}/availability
     * Body: { "available": true|false }
     * Returns 200 OK with updated item.
     */
    @PATCH
    @Path("/{id}/availability")
    public Response toggleAvailability(@PathParam("id") int id,
                                       Map<String, Boolean> body) {
        Optional<MenuItem> existing = menuService.getMenuItemById(id);
        if (existing.isEmpty()) return notFound("Елемент меню з ID " + id + " не знайдено");

        Boolean newAvailability = body.get("available");
        if (newAvailability == null) {
            ApiError err = new ApiError(400, "Bad Request",
                    "Тіло запиту має містити поле 'available' (true або false)");
            return Response.status(400).entity(err).build();
        }

        MenuItem item = existing.get();
        item.setAvailable(newAvailability);
        MenuItem updated = menuService.updateMenuItem(item);

        return Response.ok(MenuItemDTO.fromModel(updated)).build();
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    /**
     * DELETE /api/menu-items/{id}
     * Returns 204 No Content, or 404 if not found.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if (menuService.getMenuItemById(id).isEmpty()) {
            return notFound("Елемент меню з ID " + id + " не знайдено");
        }
        menuService.deleteMenuItem(id);
        return Response.noContent().build(); // 204
    }

    // ── Categories helper ─────────────────────────────────────────────────────

    /**
     * GET /api/menu-items/categories
     * Returns all possible category values.
     */
    @GET
    @Path("/categories")
    public Response getCategories() {
        List<Map<String, String>> cats = Arrays.stream(MenuItem.Category.values())
                .map(c -> {
                    Map<String, String> m = new LinkedHashMap<>();
                    m.put("name", c.name());
                    m.put("displayName", c.getDisplayName());
                    return m;
                })
                .collect(Collectors.toList());
        return Response.ok(cats).build();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private MenuItem buildFromRequest(CreateMenuItemRequest req, MenuItem item) {
        item.setName(req.getName());
        item.setDescription(req.getDescription());
        item.setPrice(req.getPrice());
        item.setCalories(req.getCalories() != null ? req.getCalories() : 0);
        item.setAvailable(req.getAvailable() != null ? req.getAvailable() : true);
        if (req.getCategory() != null) {
            item.setCategory(MenuItem.Category.valueOf(req.getCategory().toUpperCase()));
        }
        return item;
    }

    private Response notFound(String message) {
        ApiError err = new ApiError(404, "Not Found", message);
        return Response.status(Response.Status.NOT_FOUND).entity(err).build();
    }
}
