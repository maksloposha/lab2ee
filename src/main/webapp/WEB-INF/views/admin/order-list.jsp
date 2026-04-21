<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="food" uri="http://foodorder.com/tags" %>
<c:set var="currentPage" value="admin" scope="request"/>
<c:set var="pageTitle"   value="Замовлення" scope="request"/>
<jsp:include page="../header.jsp"/>

<style>
    .filter-bar { display: flex; gap: .5rem; flex-wrap: wrap; margin-bottom: 1.5rem; align-items: center; }
    .filter-bar span { font-size: .82rem; color: var(--muted); font-weight: 600; margin-right: .25rem; }
    .filter-btn {
        padding: .35rem .9rem; border-radius: 99px;
        border: 1.5px solid var(--border); color: var(--dark);
        text-decoration: none; font-size: .82rem; font-weight: 600;
        background: #fff; transition: all .15s;
    }
    .filter-btn:hover  { border-color: var(--accent); color: var(--accent); }
    .filter-btn.active { background: var(--accent); border-color: var(--accent); color: #fff; }
    .orders-meta { font-size: .85rem; color: var(--muted); margin-bottom: 1rem; }
    .cell-name  { font-weight: 600; }
    .cell-phone { font-size: .8rem; color: var(--muted); }
    .cell-addr  { max-width: 180px; font-size: .82rem; color: var(--muted);
                  overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>

<div class="page-header"><h1>Управління замовленнями</h1></div>

<c:if test="${param.error == 'not_found'}">   <div class="alert alert-error">Замовлення не знайдено.</div></c:if>
<c:if test="${param.error == 'update_failed'}"><div class="alert alert-error">Помилка оновлення статусу.</div></c:if>

<div class="admin-layout">
    <aside class="admin-sidebar">
        <div class="sidebar-head">Панель адміністратора</div>
        <a href="${pageContext.request.contextPath}/admin/menu">Елементи меню</a>
        <a href="${pageContext.request.contextPath}/admin/menu/new">Додати страву</a>
        <a href="${pageContext.request.contextPath}/admin/orders" class="active">Замовлення</a>
    </aside>

    <div class="admin-content">

        <%-- Status filter bar --%>
        <div class="filter-bar">
            <span>Статус:</span>
            <a href="${pageContext.request.contextPath}/admin/orders"
               class="filter-btn ${empty filterStatus ? 'active' : ''}">Всі</a>
            <c:forEach var="status" items="${allStatuses}">
                <a href="${pageContext.request.contextPath}/admin/orders?status=${status.name()}"
                   class="filter-btn ${filterStatus == status ? 'active' : ''}">
                    <c:out value="${status.displayName}"/>
                </a>
            </c:forEach>
        </div>

        <p class="orders-meta">Знайдено: <strong>${orders.size()}</strong> замовлень</p>

        <div class="card">
            <c:choose>
                <c:when test="${empty orders}">
                    <div style="text-align:center;padding:3rem;color:var(--muted);">
                        <p style="font-size:1.05rem;">Замовлень у цій категорії немає.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>&#8470;</th>
                                <th>Клієнт</th>
                                <th>Адреса</th>
                                <th>Позицій</th>
                                <th>Сума</th>
                                <th>Статус</th>
                                <th>Дата</th>
                                <th>Дії</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td class="fw-bold text-accent">#${order.id}</td>
                                    <td>
                                        <div class="cell-name"><c:out value="${order.customerName}"/></div>
                                        <div class="cell-phone"><c:out value="${order.customerPhone}"/></div>
                                    </td>
                                    <td>
                                        <div class="cell-addr" title="<c:out value='${order.deliveryAddress}'/>">
                                            <c:out value="${order.deliveryAddress}"/>
                                        </div>
                                    </td>
                                    <td>${order.totalItems}&nbsp;шт.</td>
                                    <td class="fw-bold text-accent">
                                        <fmt:formatNumber value="${order.total}" pattern="#,##0.00"/>&nbsp;&#8372;
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${order.status.name() == 'PENDING'}">
                                                <span class="badge badge-pending"><c:out value="${order.status.displayName}"/></span>
                                            </c:when>
                                            <c:when test="${order.status.name() == 'CONFIRMED'}">
                                                <span class="badge badge-confirmed"><c:out value="${order.status.displayName}"/></span>
                                            </c:when>
                                            <c:when test="${order.status.name() == 'PREPARING'}">
                                                <span class="badge badge-preparing"><c:out value="${order.status.displayName}"/></span>
                                            </c:when>
                                            <c:when test="${order.status.name() == 'READY'}">
                                                <span class="badge badge-ready"><c:out value="${order.status.displayName}"/></span>
                                            </c:when>
                                            <c:when test="${order.status.name() == 'DELIVERED'}">
                                                <span class="badge badge-delivered"><c:out value="${order.status.displayName}"/></span>
                                            </c:when>
                                            <c:when test="${order.status.name() == 'CANCELLED'}">
                                                <span class="badge badge-cancelled"><c:out value="${order.status.displayName}"/></span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td style="font-size:.82rem;" class="text-muted">
                                        <food:formatDate value="${order.createdAt}" pattern="dd.MM.yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/orders/view?id=${order.id}"
                                           class="btn btn-outline btn-sm">Деталі</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</div>

<jsp:include page="../footer.jsp"/>
