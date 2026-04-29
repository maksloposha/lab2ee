<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="food" uri="http://foodorder.com/tags" %>
<c:set var="currentPage" value="myorders" scope="request"/>
<c:set var="pageTitle" value="Відгуки на замовлення" scope="request"/>
<jsp:include page="../header.jsp"/>

<div class="page-header">
    <h1>Відгуки на замовлення &#8470;&nbsp;${order.id}</h1>
    <a href="${pageContext.request.contextPath}/my-orders" class="btn btn-outline">Мої замовлення</a>
</div>

<c:if test="${not empty param.msg}"><div class="alert alert-success">Відгук успішно додано.</div></c:if>
<c:if test="${not empty param.error}"><div class="alert alert-error"><c:out value="${param.error}"/></div></c:if>

<%-- Форма додавання відгуку --%>
<c:if test="${not alreadyReviewed and order.status.name() == 'DELIVERED'}">
    <div class="card" style="padding:1.5rem;margin-bottom:1.5rem;">
        <h2 style="font-family:'Playfair Display',serif;font-size:1.2rem;margin-bottom:1rem;">
            Залишити відгук
        </h2>
        <form method="post" action="${pageContext.request.contextPath}/reviews/add">
            <input type="hidden" name="orderId" value="${order.id}">
            <div class="form-group">
                <label>Рейтинг</label>
                <div style="display:flex;gap:.5rem;">
                    <c:forEach begin="1" end="5" var="star">
                        <label style="cursor:pointer;font-size:1.5rem;">
                            <input type="radio" name="rating" value="${star}" required
                                   style="display:none;">
                            &#9733;
                        </label>
                    </c:forEach>
                </div>
                <select name="rating" class="form-control" style="margin-top:.5rem;max-width:200px;" required>
                    <option value="">Оберіть рейтинг</option>
                    <option value="5">5 — Чудово</option>
                    <option value="4">4 — Добре</option>
                    <option value="3">3 — Нормально</option>
                    <option value="2">2 — Погано</option>
                    <option value="1">1 — Жахливо</option>
                </select>
            </div>
            <div class="form-group">
                <label for="comment">Коментар</label>
                <textarea id="comment" name="comment" class="form-control" rows="3"
                          maxlength="500" placeholder="Ваші враження від замовлення..."></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Надіслати відгук</button>
        </form>
    </div>
</c:if>
<c:if test="${alreadyReviewed}">
    <div class="alert alert-info">Ви вже залишили відгук на це замовлення.</div>
</c:if>
<c:if test="${order.status.name() != 'DELIVERED'}">
    <div class="alert alert-warn">
        Відгук можна залишити лише після доставки замовлення.
        Поточний статус: <strong>${order.status.displayName}</strong>
    </div>
</c:if>

<%-- Список відгуків --%>
<c:choose>
    <c:when test="${empty reviews}">
        <div class="card" style="padding:2rem;text-align:center;color:var(--muted);">
            Відгуків ще немає.
        </div>
    </c:when>
    <c:otherwise>
        <div style="display:flex;flex-direction:column;gap:1rem;">
            <c:forEach var="review" items="${reviews}">
                <div class="card" style="padding:1.25rem;">
                    <div style="display:flex;justify-content:space-between;margin-bottom:.5rem;">
                        <div>
                            <strong><c:out value="${review.user.fullName}"/></strong>
                            <span style="color:var(--accent);margin-left:.5rem;">
                                <c:forEach begin="1" end="${review.rating}" var="s">&#9733;</c:forEach>
                                <c:forEach begin="${review.rating+1}" end="5" var="s">&#9734;</c:forEach>
                            </span>
                        </div>
                        <span class="text-muted" style="font-size:.82rem;">
                            <food:formatDate value="${review.createdAt}" pattern="dd.MM.yyyy HH:mm"/>
                        </span>
                    </div>
                    <c:if test="${not empty review.comment}">
                        <p style="color:var(--dark);line-height:1.5;">
                            <c:out value="${review.comment}"/>
                        </p>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="../footer.jsp"/>
