<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="currentPage" value="login" scope="request"/>
<c:set var="pageTitle"   value="Вхід до системи" scope="request"/>
<jsp:include page="header.jsp"/>

<div class="login-wrap">
    <div class="login-card">
        <div class="login-card-header">
            <h1>SmakUA</h1>
            <p>Введіть дані для входу до системи</p>
        </div>
        <div class="login-card-body">
            <c:if test="${not empty error}">
                <div class="alert alert-error"><c:out value="${error}"/></div>
            </c:if>
            <c:if test="${param.msg == 'logged_out'}">
                <div class="alert alert-info">Ви успішно вийшли з системи.</div>
            </c:if>
            <c:if test="${param.msg == 'session_expired'}">
                <div class="alert alert-warn">Сесія закінчилась. Будь ласка, увійдіть знову.</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/login">
                <c:if test="${not empty param.redirect}">
                    <input type="hidden" name="redirect" value="<c:out value='${param.redirect}'/>">
                </c:if>
                <div class="form-group">
                    <label for="username">Логін</label>
                    <input type="text" id="username" name="username" class="form-control"
                           value="<c:out value='${username}'/>"
                           required autocomplete="username" placeholder="Введіть логін">
                </div>
                <div class="form-group">
                    <label for="password">Пароль</label>
                    <input type="password" id="password" name="password" class="form-control"
                           required autocomplete="current-password" placeholder="Введіть пароль">
                </div>
                <button type="submit" class="btn btn-primary btn-full">Увійти</button>
            </form>
        </div>
    </div>
</div>


            <div style="text-align:center;margin-top:1.25rem;font-size:.88rem;color:var(--muted);">Ще не маєте акаунту? <a href="${pageContext.request.contextPath}/register" style="color:var(--accent);font-weight:600;">Зареєструватись</a></div>
<jsp:include page="footer.jsp"/>
