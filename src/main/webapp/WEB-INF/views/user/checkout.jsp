<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentPage" value="cart" scope="request"/>
<c:set var="pageTitle"   value="Оформлення замовлення" scope="request"/>
<jsp:include page="../header.jsp"/>

<div class="page-header"><h1>Оформлення замовлення</h1></div>
<c:if test="${not empty error}"><div class="alert alert-error"><c:out value="${error}"/></div></c:if>

<div class="checkout-layout">
    <div class="form-card">
        <h2>Контактні дані та доставка</h2>
        <form method="post" action="${pageContext.request.contextPath}/order/place">
            <div class="form-row">
                <div class="form-group">
                    <label for="customerName">Ім&apos;я та прізвище <span class="required">*</span></label>
                    <input type="text" id="customerName" name="customerName" class="form-control"
                           value="<c:out value='${sessionScope.currentUser.fullName}'/>"
                           required maxlength="100" placeholder="Іван Іваненко">
                </div>
                <div class="form-group">
                    <label for="customerPhone">Номер телефону <span class="required">*</span></label>
                    <input type="tel" id="customerPhone" name="customerPhone" class="form-control"
                           value="<c:out value='${sessionScope.currentUser.phone}'/>"
                           required maxlength="20" placeholder="+380501234567">
                </div>
            </div>
            <div class="form-group">
                <label for="deliveryAddress">Адреса доставки <span class="required">*</span></label>
                <input type="text" id="deliveryAddress" name="deliveryAddress" class="form-control"
                       required maxlength="300" placeholder="вул. Хрещатик, 1, кв. 5, Київ">
            </div>
            <div class="form-group">
                <label for="notes">Додаткові побажання</label>
                <textarea id="notes" name="notes" class="form-control" rows="3" maxlength="500"
                          placeholder="Особливі побажання щодо приготування або доставки..."></textarea>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary btn-lg">Підтвердити замовлення</button>
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
            <p class="review-hint">Натискаючи «Підтвердити», ви погоджуєтесь з умовами замовлення та доставки.</p>
        </div>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
