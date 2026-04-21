package org.example.lab2ee.service;

import org.example.lab2ee.service.impl.MenuServiceStub;
import org.example.lab2ee.service.impl.OrderServiceStub;
import org.example.lab2ee.service.impl.UserServiceStub;

public class ServiceFactory {
    private static final MenuService menuService = new MenuServiceStub();
    private static final OrderService orderService = new OrderServiceStub();
    private static final UserService userService = new UserServiceStub();

    private ServiceFactory() {
    }

    public static MenuService getMenuService() {
        return menuService;
    }

    public static OrderService getOrderService() {
        return orderService;
    }

    public static UserService getUserService() {
        return userService;
    }
}
