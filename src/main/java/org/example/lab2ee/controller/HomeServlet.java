package org.example.lab2ee.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lab2ee.service.MenuService;
import org.example.lab2ee.service.ServiceFactory;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/")
public class HomeServlet extends HttpServlet {

    @EJB
    private MenuService menuService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("featuredItems",
                menuService.getAvailableMenuItems().stream().limit(4).collect(Collectors.toList()));
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }
}
