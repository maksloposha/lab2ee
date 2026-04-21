package org.example.lab2ee.rest.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GET /api  →  Returns API meta-information and list of available endpoints.
 * Useful as a "health check" and discovery endpoint.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ApiInfoResource {

    @GET
    public Response info() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name", "Food Ordering REST API");
        info.put("version", "1.0");
        info.put("timestamp", LocalDateTime.now().toString());
        info.put("status", "UP");

        List<Map<String, String>> endpoints = new ArrayList<>();
        addEndpoint(endpoints, "GET", "/api/menu-items", "Список елементів меню (фільтр + пагінація)");
        addEndpoint(endpoints, "GET", "/api/menu-items/{id}", "Отримати елемент меню за ID");
        addEndpoint(endpoints, "POST", "/api/menu-items", "Створити елемент меню");
        addEndpoint(endpoints, "PUT", "/api/menu-items/{id}", "Оновити елемент меню (повна заміна)");
        addEndpoint(endpoints, "PATCH", "/api/menu-items/{id}/availability", "Змінити доступність елемента");
        addEndpoint(endpoints, "DELETE", "/api/menu-items/{id}", "Видалити елемент меню");
        addEndpoint(endpoints, "GET", "/api/menu-items/categories", "Список усіх категорій");
        addEndpoint(endpoints, "GET", "/api/orders", "Список замовлень (фільтр + пагінація)");
        addEndpoint(endpoints, "GET", "/api/orders/{id}", "Отримати замовлення за ID");
        addEndpoint(endpoints, "POST", "/api/orders", "Створити замовлення");
        addEndpoint(endpoints, "PUT", "/api/orders/{id}", "Оновити замовлення (повна заміна)");
        addEndpoint(endpoints, "PATCH", "/api/orders/{id}/status", "Оновити статус замовлення");
        addEndpoint(endpoints, "DELETE", "/api/orders/{id}", "Скасувати замовлення");

        info.put("endpoints", endpoints);

        Map<String, String> filterInfo = new LinkedHashMap<>();
        filterInfo.put("GET /api/menu-items params", "category, available, search, page, pageSize, sortBy, sortDir");
        filterInfo.put("GET /api/orders params", "status, customerName, page, pageSize, sortBy, sortDir");
        info.put("filteringAndPagination", filterInfo);

        return Response.ok(info).build();
    }

    private void addEndpoint(List<Map<String, String>> list,
                             String method, String path, String description) {
        Map<String, String> ep = new LinkedHashMap<>();
        ep.put("method", method);
        ep.put("path", path);
        ep.put("description", description);
        list.add(ep);
    }
}
