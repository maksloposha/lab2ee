<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="currentPage" value="login" scope="request"/>
<c:set var="pageTitle"   value="Вхід до системи" scope="request"/>
<jsp:include page="header.jsp"/>

<style>
    .login-wrap {
        max-width: 420px;
        margin: 3rem auto;
    }
    .login-card {
        background: #fff;
        border-radius: var(--radius);
        border: 1px solid var(--border);
        overflow: hidden;
        box-shadow: 0 4px 24px var(--shadow);
    }
    .login-card-header {
        background: var(--dark);
        padding: 2rem;
        text-align: center;
    }
    .login-card-header h1 {
        font-family: 'Playfair Display', serif;
        color: var(--gold);
        font-size: 1.6rem;
        margin-bottom: .4rem;
    }
    .login-card-header p {
        color: #8a7a6a;
        font-size: .88rem;
    }
    .login-card-body { padding: 2rem; }
</style>

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

                <button type="submit" class="btn btn-primary" style="width:100%;justify-content:center;font-size:.95rem;padding:.7rem;">
                    Увійти
                </button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
