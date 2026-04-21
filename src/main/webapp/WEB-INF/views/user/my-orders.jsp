<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="food" uri="http://foodorder.com/tags" %>
<c:set var="currentPage" value="myorders" scope="request"/>
<c:set var="pageTitle"   value="Мої замовлення" scope="request"/>
<jsp:include page="../header.jsp"/>

<style>
  .orders-grid { display: flex; flex-direction: column; gap: 1.25rem; }

  .order-card {
    background: #fff;
    border-radius: var(--radius);
    border: 1px solid var(--border);
    overflow: hidden;
  }
  .order-card-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem 1.25rem;
    background: var(--warm);
    border-bottom: 1px solid var(--border);
    flex-wrap: wrap;
    gap: .5rem;
  }
  .order-card-head .order-id {
    font-family: 'Playfair Display', serif;
    font-weight: 700;
    font-size: 1rem;
    color: var(--dark);
  }
  .order-card-head .order-date {
    font-size: .82rem;
    color: var(--muted);
  }
  .order-card-body {
    padding: 1rem 1.25rem;
  }
  .order-items-list {
    list-style: none;
    margin-bottom: 1rem;
  }
  .order-items-list li {
    display: flex;
    justify-content: space-between;
    padding: .45rem 0;
    border-bottom: 1px solid var(--border);
    font-size: .9rem;
  }
  .order-items-list li:last-child { border-bottom: none; }
  .order-card-foot {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: .85rem 1.25rem;
    border-top: 1px solid var(--border);
    flex-wrap: wrap;
    gap: .75rem;
  }
  .order-total {
    font-size: 1.05rem;
    font-weight: 700;
    color: var(--accent);
  }
  .order-addr {
    font-size: .85rem;
    color: var(--muted);
    margin-bottom: .5rem;
  }
  .empty-orders {
    text-align: center;
    padding: 4rem 2rem;
  }
  .empty-orders h2 {
    font-family: 'Playfair Display', serif;
    font-size: 1.5rem;
    margin-bottom: .75rem;
  }
  .empty-orders p { color: var(--muted); margin-bottom: 1.75rem; }
</style>

<div class="page-header">
  <h1>Мої замовлення</h1>
  <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">
    Зробити нове замовлення
  </a>
</div>

<%-- Повідомлення про скасування --%>
<c:if test="${param.msg == 'cancelled'}">
  <div class="alert alert-info">Замовлення успішно скасовано.</div>
</c:if>

<c:choose>
  <c:when test="${empty myOrders}">
    <div class="empty-orders">
      <h2>У вас ще немає замовлень</h2>
      <p>Перегляньте наше меню та зробіть перше замовлення!</p>
      <a href="${pageContext.request.contextPath}/menu"
         class="btn btn-primary" style="font-size:.95rem;padding:.7rem 1.75rem;">
        Перейти до меню
      </a>
    </div>
  </c:when>
  <c:otherwise>
    <div class="orders-grid">
      <c:forEach var="order" items="${myOrders}">
        <div class="order-card">

            <%-- Заголовок картки --%>
          <div class="order-card-head">
            <div>
              <div class="order-id">Замовлення &#8470;&nbsp;${order.id}</div>
              <div class="order-date">
                <food:formatDate value="${order.createdAt}" pattern="dd.MM.yyyy HH:mm"/>
              </div>
            </div>
            <c:choose>
              <c:when test="${order.status.name() == 'PENDING'}">
                <span class="badge badge-pending">${order.status.displayName}</span>
              </c:when>
              <c:when test="${order.status.name() == 'CONFIRMED'}">
                <span class="badge badge-confirmed">${order.status.displayName}</span>
              </c:when>
              <c:when test="${order.status.name() == 'PREPARING'}">
                <span class="badge badge-preparing">${order.status.displayName}</span>
              </c:when>
              <c:when test="${order.status.name() == 'READY'}">
                <span class="badge badge-ready">${order.status.displayName}</span>
              </c:when>
              <c:when test="${order.status.name() == 'DELIVERED'}">
                <span class="badge badge-delivered">${order.status.displayName}</span>
              </c:when>
              <c:when test="${order.status.name() == 'CANCELLED'}">
                <span class="badge badge-cancelled">${order.status.displayName}</span>
              </c:when>
            </c:choose>
          </div>

            <%-- Тіло картки --%>
          <div class="order-card-body">
            <div class="order-addr">
              Доставка: <c:out value="${order.deliveryAddress}"/>
            </div>
            <ul class="order-items-list">
              <c:forEach var="item" items="${order.items}">
                <li>
                                    <span>
                                        <c:out value="${item.menuItem.name}"/>
                                        &times;&nbsp;${item.quantity}
                                    </span>
                  <span class="fw-bold text-accent">
                                        <fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>&nbsp;&#8372;
                                    </span>
                </li>
              </c:forEach>
            </ul>
          </div>

            <%-- Підвал картки --%>
          <div class="order-card-foot">
            <div class="order-total">
              Разом:&nbsp;
              <fmt:formatNumber value="${order.total}" pattern="#,##0.00"/>&nbsp;&#8372;
            </div>

            <c:if test="${order.status.name() != 'CANCELLED'
                                   and order.status.name() != 'DELIVERED'
                                   and order.status.name() != 'READY'}">
              <form method="post"
                    action="${pageContext.request.contextPath}/my-orders/cancel"
                    onsubmit="return confirm('Скасувати замовлення №${order.id}?')">
                <input type="hidden" name="orderId" value="${order.id}">
                <button type="submit" class="btn btn-danger btn-sm">
                  Скасувати замовлення
                </button>
              </form>
            </c:if>
            <c:if test="${order.status.name() == 'CANCELLED'}">
              <span class="text-muted" style="font-size:.85rem;">Скасовано</span>
            </c:if>
            <c:if test="${order.status.name() == 'DELIVERED'
                                   or order.status.name() == 'READY'}">
                            <span class="text-muted" style="font-size:.85rem;">
                                Скасування неможливе
                            </span>
            </c:if>
          </div>

        </div>
      </c:forEach>
    </div>
  </c:otherwise>
</c:choose>

<jsp:include page="../footer.jsp"/>