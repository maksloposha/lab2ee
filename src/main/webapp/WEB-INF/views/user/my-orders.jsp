<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="food" uri="http://foodorder.com/tags" %>
<c:set var="currentPage" value="myorders" scope="request"/>
<c:set var="pageTitle"   value="Мої замовлення" scope="request"/>
<jsp:include page="../header.jsp"/>

<div class="page-header">
    <h1>Мої замовлення</h1>
    <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Зробити нове замовлення</a>
</div>

<c:if test="${param.msg == 'cancelled'}">
    <div class="alert alert-info">Замовлення успішно скасовано.</div>
</c:if>

<c:choose>
    <c:when test="${empty myOrders}">
        <div class="empty-orders">
            <h2>У вас ще немає замовлень</h2>
            <p>Перегляньте наше меню та зробіть перше замовлення!</p>
            <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Перейти до меню</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="orders-grid">
            <c:forEach var="order" items="${myOrders}">
                <div class="order-card">
                    <div class="order-card-head">
                        <div>
                            <div class="order-id">Замовлення &#8470;&nbsp;${order.id}</div>
                            <div class="order-date"><food:formatDate value="${order.createdAt}" pattern="dd.MM.yyyy HH:mm"/></div>
                        </div>
                        <c:choose>
                            <c:when test="${order.status.name() == 'PENDING'}"><span class="badge badge-pending">${order.status.displayName}</span></c:when>
                            <c:when test="${order.status.name() == 'CONFIRMED'}"><span class="badge badge-confirmed">${order.status.displayName}</span></c:when>
                            <c:when test="${order.status.name() == 'PREPARING'}"><span class="badge badge-preparing">${order.status.displayName}</span></c:when>
                            <c:when test="${order.status.name() == 'READY'}"><span class="badge badge-ready">${order.status.displayName}</span></c:when>
                            <c:when test="${order.status.name() == 'DELIVERED'}"><span class="badge badge-delivered">${order.status.displayName}</span></c:when>
                            <c:when test="${order.status.name() == 'CANCELLED'}"><span class="badge badge-cancelled">${order.status.displayName}</span></c:when>
                        </c:choose>
                    </div>

                    <div class="order-card-body">
                        <div class="order-addr">Доставка: <c:out value="${order.deliveryAddress}"/></div>
                        <ul class="order-items-list">
                            <c:forEach var="item" items="${order.items}">
                                <li>
                                    <span><c:out value="${item.menuItem.name}"/> &times;&nbsp;${item.quantity}</span>
                                    <span class="fw-bold text-accent"><fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

                    <div class="order-card-foot">
                        <div class="order-total-amt">
                            Разом:&nbsp;<fmt:formatNumber value="${order.total}" pattern="#,##0.00"/>&nbsp;&#8372;
                        </div>
                        <c:if test="${order.status.name() != 'CANCELLED' and order.status.name() != 'DELIVERED' and order.status.name() != 'READY'}">
                            <form method="post" action="${pageContext.request.contextPath}/my-orders/cancel"
                                  onsubmit="return confirm('Скасувати замовлення №${order.id}?')">
                                <input type="hidden" name="orderId" value="${order.id}">
                                <button type="submit" class="btn btn-danger btn-sm">Скасувати замовлення</button>
                            </form>
                        </c:if>
                        <c:if test="${order.status.name() == 'CANCELLED'}">
                            <span class="text-muted">Скасовано</span>
                        </c:if>
                        <c:if test="${order.status.name() == 'DELIVERED' or order.status.name() == 'READY'}">
                            <span class="text-muted">Скасування неможливе</span>
                        </c:if>
                        <c:if test="${order.status.name() == 'DELIVERED'}">
                            <a href="${pageContext.request.contextPath}/reviews?orderId=${order.id}"
                               class="btn btn-outline btn-sm">
                                Відгук
                            </a>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../footer.jsp"/>
