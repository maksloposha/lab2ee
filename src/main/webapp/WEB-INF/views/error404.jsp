<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="404 — Сторінку не знайдено" scope="request"/>
<jsp:include page="header.jsp"/>
<div class="error-page">
    <div class="error-code">404</div>
    <h2>Сторінку не знайдено</h2>
    <p>Запитана сторінка не існує або була видалена.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">На головну</a>
</div>
<jsp:include page="footer.jsp"/>
