<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentPage" value="menu" scope="request"/>
<c:set var="pageTitle"   value="Меню" scope="request"/>
<jsp:include page="../header.jsp"/>

<div class="page-header">
    <h1>Меню ресторану</h1>
    <c:if test="${not empty param.error}">
        <span class="alert alert-error alert-inline">Товар недоступний</span>
    </c:if>
</div>

<div class="menu-layout">
    <nav class="cat-sidebar">
        <div class="sidebar-head">Категорії</div>
        <a href="${pageContext.request.contextPath}/menu" class="${empty selectedCategory?'active':''}">Всі страви</a>
        <c:forEach var="cat" items="${categories}">
            <a href="${pageContext.request.contextPath}/menu?category=${cat.name()}"
               class="${selectedCategory==cat?'active':''}"><c:out value="${cat.displayName}"/></a>
        </c:forEach>
    </nav>

    <div class="menu-grid">
        <c:choose>
            <c:when test="${empty menuItems}">
                <div class="empty-state">
                    <h3>Страви не знайдено</h3>
                    <p>У цій категорії поки немає доступних страв.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="item" items="${menuItems}">
                    <div class="menu-card ${!item.available?'unavail':''}">
                        <div class="menu-card-header">
                            <div class="cat-label"><c:out value="${item.category.displayName}"/></div>
                            <h3><c:out value="${item.name}"/></h3>
                        </div>
                        <div class="menu-card-body">
                            <p class="desc"><c:out value="${item.description}"/></p>
                            <div class="menu-card-meta">
                                <span class="meta-pill">${item.calories} ккал</span>
                                <c:if test="${item.available}"><span class="meta-pill avail">Доступно</span></c:if>
                                <c:if test="${!item.available}"><span class="meta-pill unavail">Немає</span></c:if>
                            </div>
                            <div class="menu-card-foot">
                                <span class="price"><fmt:formatNumber value="${item.price}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
                                <c:if test="${item.available}">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin}">
                                            <form method="post" action="${pageContext.request.contextPath}/order/add" class="qty-form">
                                                <input type="hidden" name="menuItemId" value="${item.id}">
                                                <input type="number" name="quantity" value="1" min="1" max="20" class="qty-input">
                                                <button type="submit" class="btn btn-primary btn-sm">Додати</button>
                                            </form>
                                        </c:when>
                                        <c:when test="${empty sessionScope.currentUser}">
                                            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Увійти</a>
                                        </c:when>
                                    </c:choose>
                                </c:if>
                                <c:if test="${!item.available}"><span class="badge badge-cancelled">Недоступно</span></c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
