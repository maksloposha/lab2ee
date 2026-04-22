<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentPage" value="admin" scope="request"/>
<c:set var="pageTitle"   value="Управління меню" scope="request"/>
<jsp:include page="../header.jsp"/>

<div class="page-header">
    <h1>Управління меню</h1>
    <a href="${pageContext.request.contextPath}/admin/menu/new" class="btn btn-success">Додати страву</a>
</div>

<c:if test="${param.success=='created'}"><div class="alert alert-success">Страву успішно додано до меню.</div></c:if>
<c:if test="${param.success=='updated'}"><div class="alert alert-success">Страву успішно оновлено.</div></c:if>
<c:if test="${param.success=='deleted'}"><div class="alert alert-success">Страву видалено з меню.</div></c:if>
<c:if test="${param.error=='not_found'}"><div class="alert alert-error">Страву не знайдено.</div></c:if>

<div class="admin-layout">
    <aside class="admin-sidebar">
        <div class="sidebar-head">Панель адміністратора</div>
        <a href="${pageContext.request.contextPath}/admin/menu" class="active">Елементи меню</a>
        <a href="${pageContext.request.contextPath}/admin/menu/new">Додати страву</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Замовлення</a>
    </aside>

    <div class="admin-content">
        <c:forEach var="cat" items="${categories}">
            <c:set var="hasItems" value="false"/>
            <c:forEach var="item" items="${menuItems}">
                <c:if test="${item.category==cat}"><c:set var="hasItems" value="true"/></c:if>
            </c:forEach>
            <c:if test="${hasItems}">
                <div class="card card-mb">
                    <div class="card-cat-header">
                        <strong class="cat-title">${cat.displayName}</strong>
                    </div>
                    <table>
                        <thead>
                            <tr><th>ID</th><th>Назва</th><th>Опис</th><th>Ціна</th><th>Ккал</th><th>Статус</th><th>Дії</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${menuItems}">
                                <c:if test="${item.category==cat}">
                                    <tr>
                                        <td class="text-muted cell-id">#${item.id}</td>
                                        <td class="fw-bold"><c:out value="${item.name}"/></td>
                                        <td class="text-muted cell-desc"><c:out value="${item.description}"/></td>
                                        <td class="fw-bold text-accent"><fmt:formatNumber value="${item.price}" pattern="#,##0.00"/>&nbsp;&#8372;</td>
                                        <td>${item.calories}</td>
                                        <td>
                                            <c:if test="${item.available}"><span class="available">Доступно</span></c:if>
                                            <c:if test="${!item.available}"><span class="unavailable">Недоступно</span></c:if>
                                        </td>
                                        <td>
                                            <div class="cell-actions">
                                                <a href="${pageContext.request.contextPath}/admin/menu/edit?id=${item.id}" class="btn btn-outline btn-sm">Редагувати</a>
                                                <form method="post" action="${pageContext.request.contextPath}/admin/menu/delete"
                                                      onsubmit="return confirm('Видалити страву «${item.name}»?')">
                                                    <input type="hidden" name="id" value="${item.id}">
                                                    <button type="submit" class="btn btn-danger btn-sm">Видалити</button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
