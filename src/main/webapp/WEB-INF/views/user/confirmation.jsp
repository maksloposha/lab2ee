<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Замовлення прийнято" scope="request"/>
<jsp:include page="../header.jsp"/>

<div class="confirm-wrap">
    <c:choose>
        <c:when test="${not empty order}">
            <div class="confirm-hero">
                <h1>Замовлення &#8470;&nbsp;${order.id} прийнято!</h1>
                <p>Дякуємо, <c:out value="${order.customerName}"/>! Ваше замовлення вже опрацьовується.</p>
            </div>

            <div class="info-card">
                <h2>Деталі замовлення</h2>
                <div class="info-grid">
                    <div class="info-item"><label>Ім&apos;я</label><p><c:out value="${order.customerName}"/></p></div>
                    <div class="info-item"><label>Телефон</label><p><c:out value="${order.customerPhone}"/></p></div>
                    <div class="info-item info-full"><label>Адреса доставки</label><p><c:out value="${order.deliveryAddress}"/></p></div>
                    <c:if test="${not empty order.notes}">
                        <div class="info-item info-full info-italic"><label>Побажання</label><p><c:out value="${order.notes}"/></p></div>
                    </c:if>
                </div>
            </div>

            <div class="info-card">
                <h2>Склад замовлення</h2>
                <ul class="items-list">
                    <c:forEach var="item" items="${order.items}">
                        <li>
                            <span><c:out value="${item.menuItem.name}"/> &times; ${item.quantity}</span>
                            <span class="item-subtotal"><fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
                        </li>
                    </c:forEach>
                </ul>
                <div class="order-total">
                    <span>Загальна сума:</span>
                    <span class="amt"><fmt:formatNumber value="${order.total}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
                </div>
            </div>

            <div class="action-row">
                <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Зробити ще замовлення</a>
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline">На головну</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">Дані замовлення не знайдено. Можливо, сесія закінчилась.</div>
            <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Повернутись до меню</a>
        </c:otherwise>
    </c:choose>
</div>
<jsp:include page="../footer.jsp"/>
