<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Помилка" scope="request"/>
<jsp:include page="header.jsp"/>
<div class="error-page">
    <div class="error-code"><c:out value="${not empty errorCode ? errorCode : '404'}"/></div>
    <h2>
        <c:choose>
            <c:when test="${errorCode == '403'}">Доступ заборонено</c:when>
            <c:when test="${errorCode == '404'}">Сторінку не знайдено</c:when>
            <c:otherwise>Щось пішло не так</c:otherwise>
        </c:choose>
    </h2>
    <p><c:out value="${not empty errorMessage ? errorMessage : 'Запитана сторінка не існує або була видалена.'}"/></p>
    <div class="error-actions">
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">На головну</a>
        <c:if test="${empty sessionScope.currentUser}">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Увійти</a>
        </c:if>
    </div>
</div>
<jsp:include page="footer.jsp"/>
