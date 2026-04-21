<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Помилка" scope="request"/>
<jsp:include page="header.jsp"/>

<div style="max-width:520px;margin:4rem auto;text-align:center;">
    <div style="font-size:4rem;font-weight:900;font-family:'Playfair Display',serif;color:var(--border);margin-bottom:1rem;">
        <c:out value="${not empty errorCode ? errorCode : '404'}"/>
    </div>
    <h2 style="font-family:'Playfair Display',serif;font-size:1.5rem;margin-bottom:.75rem;">
        <c:choose>
            <c:when test="${errorCode == '403'}">Доступ заборонено</c:when>
            <c:when test="${errorCode == '404'}">Сторінку не знайдено</c:when>
            <c:otherwise>Щось пішло не так</c:otherwise>
        </c:choose>
    </h2>
    <p style="color:var(--muted);margin-bottom:2rem;line-height:1.6;">
        <c:out value="${not empty errorMessage ? errorMessage : 'Запитана сторінка не існує або була видалена.'}"/>
    </p>
    <div style="display:flex;gap:.75rem;justify-content:center;flex-wrap:wrap;">
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">На головну</a>
        <c:if test="${empty sessionScope.currentUser}">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Увійти</a>
        </c:if>
    </div>
</div>

<jsp:include page="footer.jsp"/>
