<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="404 — Сторінку не знайдено" scope="request"/>
<jsp:include page="header.jsp"/>
<div style="max-width:500px;margin:4rem auto;text-align:center;">
    <div style="font-size:5rem;font-weight:900;font-family:'Playfair Display',serif;color:var(--border);margin-bottom:.75rem;">404</div>
    <h2 style="font-family:'Playfair Display',serif;font-size:1.5rem;margin-bottom:.75rem;">Сторінку не знайдено</h2>
    <p style="color:var(--muted);margin-bottom:2rem;line-height:1.6;">Запитана сторінка не існує або була видалена.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary" style="font-size:.95rem;padding:.7rem 1.75rem;">На головну</a>
</div>
<jsp:include page="footer.jsp"/>
