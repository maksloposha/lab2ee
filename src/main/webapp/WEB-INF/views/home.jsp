<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentPage" value="home" scope="request"/>
<c:set var="pageTitle"   value="Головна" scope="request"/>
<jsp:include page="header.jsp"/>

<section class="hero">
    <h1>Смачна їжа &mdash; <em>швидко і зручно</em></h1>
    <p>Замовляйте улюблені страви прямо з нашого ресторану. Свіже, смачне, гаряче.</p>
    <div class="hero-actions">
        <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Переглянути меню</a>
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin}">
                <a href="${pageContext.request.contextPath}/order/cart" class="btn btn-outline hero-btn-outline">Мій кошик</a>
            </c:when>
            <c:when test="${empty sessionScope.currentUser}">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline hero-btn-outline">Увійти та замовити</a>
            </c:when>
        </c:choose>
    </div>
</section>

<div class="stats-row">
    <div class="stat-card"><div class="stat-label">Позицій у меню</div><div class="stat-value">12</div></div>
    <div class="stat-card"><div class="stat-label">Хвилин на доставку</div><div class="stat-value">30</div></div>
    <div class="stat-card"><div class="stat-label">Рейтинг закладу</div><div class="stat-value">4.9</div></div>
</div>

<h2 class="section-title">Популярні страви</h2>
<div class="featured-grid">
    <c:forEach var="item" items="${featuredItems}">
        <div class="food-card">
            <div class="food-card-img">
                <div class="food-card-img-text"><c:out value="${item.category.displayName}"/></div>
            </div>
            <div class="food-card-body">
                <h3><c:out value="${item.name}"/></h3>
                <p class="desc"><c:out value="${item.description}"/></p>
                <div class="food-card-foot">
                    <span class="price"><fmt:formatNumber value="${item.price}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
                    <c:if test="${item.available}">
                        <c:choose>
                            <c:when test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin}">
                                <form method="post" action="${pageContext.request.contextPath}/order/add" style="display:inline">
                                    <input type="hidden" name="menuItemId" value="${item.id}">
                                    <input type="hidden" name="quantity" value="1">
                                    <button type="submit" class="btn btn-primary btn-sm">Додати</button>
                                </form>
                            </c:when>
                            <c:when test="${empty sessionScope.currentUser}">
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Увійти</a>
                            </c:when>
                        </c:choose>
                    </c:if>
                    <c:if test="${!item.available}"><span class="badge badge-cancelled">Немає</span></c:if>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<div class="center-link">
    <a href="${pageContext.request.contextPath}/menu" class="btn btn-outline">Переглянути все меню</a>
</div>

<jsp:include page="footer.jsp"/>
