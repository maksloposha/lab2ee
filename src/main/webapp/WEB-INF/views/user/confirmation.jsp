<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Замовлення прийнято" scope="request"/>
<jsp:include page="../header.jsp"/>
<style>
.confirm-wrap{max-width:660px;margin:1.5rem auto}
.confirm-hero{background:linear-gradient(135deg,#1a3d28,#2a6040);border-radius:14px;padding:2.5rem 2rem;text-align:center;color:#fff;margin-bottom:1.75rem}
.confirm-hero h1{font-family:'Playfair Display',serif;font-size:2rem;margin-bottom:.6rem}
.confirm-hero p{color:#90c8a8;font-size:.95rem}
.info-card{background:#fff;border-radius:var(--radius);border:1px solid var(--border);padding:1.5rem;margin-bottom:1.25rem}
.info-card h2{font-family:'Playfair Display',serif;font-size:1.15rem;margin-bottom:1.1rem;padding-bottom:.75rem;border-bottom:1px solid var(--border)}
.info-grid{display:grid;grid-template-columns:1fr 1fr;gap:.85rem}
.info-item label{display:block;font-size:.75rem;font-weight:700;text-transform:uppercase;letter-spacing:.5px;color:var(--muted);margin-bottom:.25rem}
.info-item p{font-weight:600}
.items-list{list-style:none}
.items-list li{display:flex;justify-content:space-between;padding:.6rem 0;border-bottom:1px solid var(--border);font-size:.9rem}
.items-list li:last-child{border-bottom:none}
.item-subtotal{font-weight:700;color:var(--accent)}
.order-total{display:flex;justify-content:space-between;font-size:1.1rem;font-weight:700;padding-top:.85rem;border-top:2px solid var(--border)}
.order-total .amt{color:var(--accent)}
</style>

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
                    <div class="info-item" style="grid-column:1/-1"><label>Адреса доставки</label><p><c:out value="${order.deliveryAddress}"/></p></div>
                    <c:if test="${not empty order.notes}">
                        <div class="info-item" style="grid-column:1/-1"><label>Побажання</label><p style="font-style:italic;color:var(--muted);"><c:out value="${order.notes}"/></p></div>
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

            <div style="display:flex;gap:.75rem;flex-wrap:wrap;">
                <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary" style="font-size:.95rem;padding:.7rem 1.75rem;">Зробити ще замовлення</a>
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
