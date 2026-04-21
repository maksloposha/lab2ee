<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentPage" value="cart" scope="request"/>
<c:set var="pageTitle"   value="Оформлення замовлення" scope="request"/>
<jsp:include page="../header.jsp"/>
<style>
.checkout-layout{display:grid;grid-template-columns:1fr 320px;gap:1.75rem}
.form-card{background:#fff;border-radius:var(--radius);border:1px solid var(--border);padding:1.75rem}
.form-card h2{font-family:'Playfair Display',serif;font-size:1.3rem;margin-bottom:1.5rem;padding-bottom:.85rem;border-bottom:1px solid var(--border)}
.form-row{display:grid;grid-template-columns:1fr 1fr;gap:1rem}
.order-review{background:#fff;border-radius:var(--radius);border:1px solid var(--border);overflow:hidden;position:sticky;top:72px}
.review-head{background:var(--dark);color:var(--gold);padding:1rem 1.25rem;font-family:'Playfair Display',serif;font-size:1rem;font-weight:700}
.review-body{padding:1.1rem 1.25rem}
.review-item{display:flex;justify-content:space-between;align-items:flex-start;padding:.55rem 0;border-bottom:1px solid var(--border);font-size:.88rem}
.review-item:last-child{border-bottom:none}
.review-item-name{color:var(--dark);font-weight:500}
.review-item-qty{font-size:.78rem;color:var(--muted)}
.review-item-price{font-weight:700;color:var(--accent);white-space:nowrap}
.review-total{display:flex;justify-content:space-between;font-size:1.05rem;font-weight:700;padding-top:.85rem;border-top:2px solid var(--border);margin-top:.5rem}
.review-total .amt{color:var(--accent)}
</style>

<div class="page-header"><h1>Оформлення замовлення</h1></div>
<c:if test="${not empty error}"><div class="alert alert-error"><c:out value="${error}"/></div></c:if>

<div class="checkout-layout">
    <div class="form-card">
        <h2>Контактні дані та доставка</h2>
        <form method="post" action="${pageContext.request.contextPath}/order/place">
            <div class="form-row">
                <div class="form-group">
                    <label for="customerName">Ім&apos;я та прізвище <span style="color:var(--red)">*</span></label>
                    <input type="text" id="customerName" name="customerName" class="form-control"
                           value="<c:out value='${sessionScope.currentUser.fullName}'/>"
                           required maxlength="100" placeholder="Іван Іваненко">
                </div>
                <div class="form-group">
                    <label for="customerPhone">Номер телефону <span style="color:var(--red)">*</span></label>
                    <input type="tel" id="customerPhone" name="customerPhone" class="form-control"
                           value="<c:out value='${sessionScope.currentUser.phone}'/>"
                           required maxlength="20" placeholder="+380501234567">
                </div>
            </div>
            <div class="form-group">
                <label for="deliveryAddress">Адреса доставки <span style="color:var(--red)">*</span></label>
                <input type="text" id="deliveryAddress" name="deliveryAddress" class="form-control"
                       required maxlength="300" placeholder="вул. Хрещатик, 1, кв. 5, Київ">
            </div>
            <div class="form-group">
                <label for="notes">Додаткові побажання</label>
                <textarea id="notes" name="notes" class="form-control" rows="3" maxlength="500"
                          placeholder="Особливі побажання щодо приготування або доставки..."></textarea>
            </div>
            <div style="display:flex;gap:.75rem;margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary" style="flex:1;justify-content:center;font-size:.95rem;padding:.7rem;">Підтвердити замовлення</button>
                <a href="${pageContext.request.contextPath}/order/cart" class="btn btn-outline">Назад</a>
            </div>
        </form>
    </div>

    <div class="order-review">
        <div class="review-head">Ваше замовлення</div>
        <div class="review-body">
            <c:set var="total" value="0"/>
            <c:forEach var="item" items="${cart}">
                <div class="review-item">
                    <div>
                        <div class="review-item-name"><c:out value="${item.menuItem.name}"/></div>
                        <div class="review-item-qty">x ${item.quantity}</div>
                    </div>
                    <div class="review-item-price"><fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>&nbsp;&#8372;</div>
                </div>
                <c:set var="total" value="${total + item.subtotal.doubleValue()}"/>
            </c:forEach>
            <div class="review-total">
                <span>Разом:</span>
                <span class="amt"><fmt:formatNumber value="${total}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
            </div>
            <p style="font-size:.75rem;color:var(--muted);margin-top:.85rem;line-height:1.5;">
                Натискаючи «Підтвердити», ви погоджуєтесь з умовами замовлення та доставки.
            </p>
        </div>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
