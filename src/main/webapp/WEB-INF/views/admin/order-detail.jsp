<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="food" uri="http://foodorder.com/tags" %>
<c:set var="currentPage" value="admin" scope="request"/>
<c:set var="pageTitle"   value="Деталі замовлення" scope="request"/>
<jsp:include page="../header.jsp"/>

<div class="page-header">
    <h1>Замовлення &#8470;&nbsp;${order.id}</h1>
    <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline">До списку</a>
</div>

<c:if test="${param.success == 'status_updated'}">
    <div class="alert alert-success">Статус замовлення успішно оновлено.</div>
</c:if>

<div class="admin-layout">
    <aside class="admin-sidebar">
        <div class="sidebar-head">Панель адміністратора</div>
        <a href="${pageContext.request.contextPath}/admin/menu">Елементи меню</a>
        <a href="${pageContext.request.contextPath}/admin/menu/new">Додати страву</a>
        <a href="${pageContext.request.contextPath}/admin/orders" class="active">Замовлення</a>
    </aside>

    <div class="admin-content">
        <div class="detail-layout">
            <div>
                <div class="info-section">
                    <h3>Дані клієнта</h3>
                    <div class="detail-row"><span class="detail-label">Ім&apos;я</span><span class="detail-value"><c:out value="${order.customerName}"/></span></div>
                    <div class="detail-row"><span class="detail-label">Телефон</span><span class="detail-value"><c:out value="${order.customerPhone}"/></span></div>
                    <div class="detail-row"><span class="detail-label">Адреса</span><span class="detail-value"><c:out value="${order.deliveryAddress}"/></span></div>
                    <c:if test="${not empty order.notes}">
                        <div class="detail-row">
                            <span class="detail-label">Нотатки</span>
                            <span class="detail-value detail-italic"><c:out value="${order.notes}"/></span>
                        </div>
                    </c:if>
                    <div class="detail-row">
                        <span class="detail-label">Дата</span>
                        <span class="detail-value"><food:formatDate value="${order.createdAt}" pattern="dd.MM.yyyy HH:mm:ss"/></span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Статус</span>
                        <span class="detail-value">
                            <c:choose>
                                <c:when test="${order.status.name() == 'PENDING'}"><span class="badge badge-pending"><c:out value="${order.status.displayName}"/></span></c:when>
                                <c:when test="${order.status.name() == 'CONFIRMED'}"><span class="badge badge-confirmed"><c:out value="${order.status.displayName}"/></span></c:when>
                                <c:when test="${order.status.name() == 'PREPARING'}"><span class="badge badge-preparing"><c:out value="${order.status.displayName}"/></span></c:when>
                                <c:when test="${order.status.name() == 'READY'}"><span class="badge badge-ready"><c:out value="${order.status.displayName}"/></span></c:when>
                                <c:when test="${order.status.name() == 'DELIVERED'}"><span class="badge badge-delivered"><c:out value="${order.status.displayName}"/></span></c:when>
                                <c:when test="${order.status.name() == 'CANCELLED'}"><span class="badge badge-cancelled"><c:out value="${order.status.displayName}"/></span></c:when>
                            </c:choose>
                        </span>
                    </div>
                </div>

                <div class="info-section">
                    <h3>Склад замовлення</h3>
                    <table>
                        <thead><tr><th>Страва</th><th>Ціна</th><th>К-сть</th><th>Сума</th></tr></thead>
                        <tbody>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td>
                                        <div class="fw-bold"><c:out value="${item.menuItem.name}"/></div>
                                        <c:if test="${not empty item.specialInstructions}">
                                            <div class="text-muted detail-italic cell-sm"><c:out value="${item.specialInstructions}"/></div>
                                        </c:if>
                                    </td>
                                    <td><fmt:formatNumber value="${item.menuItem.price}" pattern="#,##0.00"/>&nbsp;&#8372;</td>
                                    <td>&times;&nbsp;${item.quantity}</td>
                                    <td class="fw-bold text-accent"><fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>&nbsp;&#8372;</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="3" class="tfoot-label">Разом:</td>
                                <td class="fw-bold text-accent tfoot-total"><fmt:formatNumber value="${order.total}" pattern="#,##0.00"/>&nbsp;&#8372;</td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>

            <div class="status-panel">
                <div class="status-panel-head">Змінити статус</div>
                <div class="status-panel-body">
                    <c:choose>
                        <c:when test="${order.status.name() == 'CANCELLED' or order.status.name() == 'DELIVERED'}">
                            <div class="alert alert-info alert-nomargin">Замовлення завершено. Зміна статусу недоступна.</div>
                        </c:when>
                        <c:otherwise>
                            <form method="post" action="${pageContext.request.contextPath}/admin/orders/status">
                                <input type="hidden" name="orderId" value="${order.id}">
                                <div class="form-group">
                                    <label for="status">Новий статус</label>
                                    <select id="status" name="status" class="form-control">
                                        <c:forEach var="s" items="${allStatuses}">
                                            <c:choose>
                                                <c:when test="${order.status == s}">
                                                    <option value="${s.name()}" selected><c:out value="${s.displayName}"/> (поточний)</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${s.name()}"><c:out value="${s.displayName}"/></option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-success btn-full">Зберегти статус</button>
                            </form>
                        </c:otherwise>
                    </c:choose>

                    <div class="status-flow-wrap">
                        <div class="flow-label">Хід виконання</div>
                        <ul class="status-flow">
                            <c:forEach var="s" items="${allStatuses}">
                                <c:if test="${s.name() != 'CANCELLED'}">
                                    <c:choose>
                                        <c:when test="${order.status == s}">
                                            <li class="current-step"><span class="status-dot current"></span><c:out value="${s.displayName}"/></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li><span class="status-dot"></span><c:out value="${s.displayName}"/></li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
