<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${not empty pageTitle ? pageTitle : 'SmakUA'}"/> — Система замовлення їжі</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<nav>
    <a class="brand" href="${pageContext.request.contextPath}/">SmakUA</a>
    <div class="nav-group">
        <a href="${pageContext.request.contextPath}/"     class="nav-link ${currentPage=='home'?'active':''}">Головна</a>
        <a href="${pageContext.request.contextPath}/menu" class="nav-link ${currentPage=='menu'?'active':''}">Меню</a>
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser}">
                <c:if test="${not sessionScope.currentUser.admin}">
                    <a href="${pageContext.request.contextPath}/order/cart" class="nav-link ${currentPage=='cart'?'active':''}">
                        Кошик
                        <c:if test="${not empty sessionScope.cart and sessionScope.cart.size() > 0}">
                            <span class="nav-badge">${sessionScope.cart.size()}</span>
                        </c:if>
                    </a>
                    <a href="${pageContext.request.contextPath}/my-orders" class="nav-link ${currentPage=='myorders'?'active':''}">Мої замовлення</a>
                </c:if>
                <c:if test="${sessionScope.currentUser.admin}">
                    <div class="nav-sep"></div>
                    <a href="${pageContext.request.contextPath}/admin/menu"   class="nav-link ${currentPage=='admin'?'active':''}">Панель адміна</a>
                </c:if>
                <div class="nav-sep"></div>
                <span class="nav-user"><c:out value="${sessionScope.currentUser.fullName}"/> (${sessionScope.currentUser.role.displayName})</span>
                <a href="${pageContext.request.contextPath}/logout" class="nav-link">Вийти</a>
            </c:when>
            <c:otherwise>
                <div class="nav-sep"></div>
                <a href="${pageContext.request.contextPath}/login" class="nav-link ${currentPage=='login'?'active':''}">Увійти</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>
<main>
