<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentPage" value="cart" scope="request"/>
<c:set var="pageTitle"   value="Кошик" scope="request"/>
<jsp:include page="../header.jsp"/>

<div class="page-header"><h1>Кошик</h1></div>

<c:choose>
    <c:when test="${empty cart}">
        <div class="empty-cart">
            <h2>Кошик порожній</h2>
            <p>Додайте страви з нашого меню, щоб зробити замовлення.</p>
            <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Перейти до меню</a>
        </div>
    </c:when>
    <c:otherwise>
        <c:set var="cartTotal" value="0"/>
        <c:set var="totalItems" value="0"/>
        <c:forEach var="item" items="${cart}">
            <c:set var="cartTotal" value="${cartTotal + item.subtotal.doubleValue()}"/>
            <c:set var="totalItems" value="${totalItems + item.quantity}"/>
        </c:forEach>

        <div class="cart-layout">
            <div class="cart-table-wrap">
                <table>
                    <thead>
                        <tr><th>Страва</th><th>Категорія</th><th>Ціна</th><th>К-сть</th><th>Сума</th><th></th></tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${cart}">
                            <tr>
                                <td class="fw-bold"><c:out value="${item.menuItem.name}"/></td>
                                <td class="text-muted"><c:out value="${item.menuItem.category.displayName}"/></td>
                                <td><fmt:formatNumber value="${item.menuItem.price}" pattern="#,##0.00"/>&nbsp;&#8372;</td>
                                <td class="fw-bold">${item.quantity}</td>
                                <td class="fw-bold text-accent"><fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>&nbsp;&#8372;</td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/order/remove">
                                        <input type="hidden" name="menuItemId" value="${item.menuItem.id}">
                                        <button type="submit" class="btn btn-danger btn-sm">Видалити</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="summary-card">
                <h2>Підсумок</h2>
                <div class="sum-line"><span>Позицій:</span><span>${totalItems} шт.</span></div>
                <div class="sum-line">
                    <span>Доставка:</span>
                    <c:if test="${cartTotal >= 500}"><span class="text-success">Безкоштовно</span></c:if>
                    <c:if test="${cartTotal < 500}"><span>50,00&nbsp;&#8372;</span></c:if>
                </div>
                <div class="sum-total">
                    <span>До сплати:</span>
                    <c:choose>
                        <c:when test="${cartTotal >= 500}">
                            <span class="amt"><fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
                        </c:when>
                        <c:otherwise>
                            <span class="amt"><fmt:formatNumber value="${cartTotal + 50}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${cartTotal >= 500}"><p class="free-delivery">Ви отримали безкоштовну доставку!</p></c:if>
                <c:if test="${cartTotal < 500}"><p class="no-free">Ще <fmt:formatNumber value="${500 - cartTotal}" pattern="#,##0.00"/>&nbsp;&#8372; до безкоштовної доставки</p></c:if>
                <a href="${pageContext.request.contextPath}/order/checkout" class="btn btn-primary btn-full">Оформити замовлення</a>
                <a href="${pageContext.request.contextPath}/menu" class="btn btn-outline btn-full btn-mt">Продовжити покупки</a>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<jsp:include page="../footer.jsp"/>
