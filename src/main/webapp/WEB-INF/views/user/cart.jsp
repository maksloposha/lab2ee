<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentPage" value="cart" scope="request"/>
<c:set var="pageTitle"   value="Кошик" scope="request"/>
<jsp:include page="../header.jsp"/>
<style>
.cart-layout{display:grid;grid-template-columns:1fr 300px;gap:1.75rem;align-items:flex-start}
.cart-table-wrap{background:#fff;border-radius:var(--radius);border:1px solid var(--border);overflow:hidden}
.summary-card{background:#fff;border-radius:var(--radius);border:1px solid var(--border);padding:1.5rem;position:sticky;top:72px}
.summary-card h2{font-family:'Playfair Display',serif;font-size:1.25rem;margin-bottom:1.25rem;padding-bottom:.85rem;border-bottom:1px solid var(--border)}
.sum-line{display:flex;justify-content:space-between;margin-bottom:.65rem;font-size:.9rem;color:var(--muted)}
.sum-total{display:flex;justify-content:space-between;font-size:1.1rem;font-weight:700;padding-top:.85rem;border-top:2px solid var(--border);margin-top:.85rem}
.sum-total .amt{color:var(--accent)}
.free-delivery{font-size:.78rem;color:var(--green);font-weight:600;text-align:center;margin:.6rem 0 1.1rem}
.no-free{font-size:.78rem;color:var(--muted);text-align:center;margin:.6rem 0 1.1rem}
.empty-cart{text-align:center;padding:4rem 2rem}
.empty-cart h2{font-family:'Playfair Display',serif;font-size:1.5rem;margin-bottom:.75rem}
.empty-cart p{color:var(--muted);margin-bottom:1.75rem}
</style>

<div class="page-header"><h1>Кошик</h1></div>

<c:choose>
    <c:when test="${empty cart}">
        <div class="empty-cart">
            <h2>Кошик порожній</h2>
            <p>Додайте страви з нашого меню, щоб зробити замовлення.</p>
            <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary" style="font-size:.95rem;padding:.7rem 1.75rem;">Перейти до меню</a>
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
                    <thead><tr><th>Страва</th><th>Категорія</th><th>Ціна</th><th>К-сть</th><th>Сума</th><th></th></tr></thead>
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
                <c:if test="${cartTotal >= 500}">
                    <p class="free-delivery">Ви отримали безкоштовну доставку!</p>
                </c:if>
                <c:if test="${cartTotal < 500}">
                    <p class="no-free">Ще <fmt:formatNumber value="${500 - cartTotal}" pattern="#,##0.00"/>&nbsp;&#8372; до безкоштовної доставки</p>
                </c:if>
                <a href="${pageContext.request.contextPath}/order/checkout" class="btn btn-primary" style="width:100%;justify-content:center;">Оформити замовлення</a>
                <a href="${pageContext.request.contextPath}/menu" class="btn btn-outline" style="width:100%;justify-content:center;margin-top:.6rem;">Продовжити покупки</a>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<jsp:include page="../footer.jsp"/>
