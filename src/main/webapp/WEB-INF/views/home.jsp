<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentPage" value="home" scope="request"/>
<c:set var="pageTitle"   value="Головна" scope="request"/>
<jsp:include page="header.jsp"/>
<style>
.hero{background:linear-gradient(135deg,var(--dark) 0%,#3a1e0e 55%,#6a3018 100%);border-radius:14px;padding:4rem 2.5rem;text-align:center;margin-bottom:2.5rem}
.hero h1{font-family:'Playfair Display',serif;font-size:2.8rem;font-weight:700;color:#fff;margin-bottom:.85rem;line-height:1.2}
.hero h1 em{color:var(--gold);font-style:normal}
.hero p{color:#b8a898;font-size:1.05rem;margin-bottom:2rem}
.hero-actions{display:flex;gap:.85rem;justify-content:center;flex-wrap:wrap}
.stats-row{display:grid;grid-template-columns:repeat(3,1fr);gap:1.25rem;margin-bottom:2.5rem}
.stat-card{background:#fff;border-radius:var(--radius);border:1px solid var(--border);padding:1.5rem;text-align:center}
.stat-label{font-size:.82rem;color:var(--muted);text-transform:uppercase;letter-spacing:.5px;margin-bottom:.4rem}
.stat-value{font-family:'Playfair Display',serif;font-size:2rem;font-weight:700;color:var(--accent)}
.section-title{font-family:'Playfair Display',serif;font-size:1.5rem;font-weight:700;margin-bottom:1.25rem}
.featured-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(230px,1fr));gap:1.25rem;margin-bottom:2rem}
.food-card{background:#fff;border-radius:var(--radius);border:1px solid var(--border);overflow:hidden;transition:transform .2s,box-shadow .2s;display:flex;flex-direction:column}
.food-card:hover{transform:translateY(-3px);box-shadow:0 8px 24px var(--shadow)}
.food-card-img{height:80px;background:var(--warm);display:flex;align-items:center;justify-content:center}
.food-card-img-text{font-family:'Playfair Display',serif;font-size:1rem;font-weight:700;color:var(--brown);padding:.5rem 1rem;text-align:center;line-height:1.3}
.food-card-body{padding:1.1rem;flex:1;display:flex;flex-direction:column}
.food-card-body h3{font-weight:700;font-size:.95rem;margin-bottom:.3rem}
.food-card-body .desc{color:var(--muted);font-size:.82rem;line-height:1.45;flex:1;margin-bottom:.85rem}
.food-card-foot{display:flex;align-items:center;justify-content:space-between;border-top:1px solid var(--border);padding-top:.75rem}
.price{font-size:1.05rem;font-weight:700;color:var(--accent)}
</style>

<section class="hero">
    <h1>Смачна їжа &mdash; <em>швидко і зручно</em></h1>
    <p>Замовляйте улюблені страви прямо з нашого ресторану. Свіже, смачне, гаряче.</p>
    <div class="hero-actions">
        <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary" style="font-size:.95rem;padding:.7rem 1.75rem;">Переглянути меню</a>
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin}">
                <a href="${pageContext.request.contextPath}/order/cart" class="btn btn-outline" style="color:#c8b8a8;border-color:#6a5a4a;font-size:.95rem;padding:.7rem 1.75rem;">Мій кошик</a>
            </c:when>
            <c:when test="${empty sessionScope.currentUser}">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline" style="color:#c8b8a8;border-color:#6a5a4a;font-size:.95rem;padding:.7rem 1.75rem;">Увійти та замовити</a>
            </c:when>
        </c:choose>
    </div>
</section>

<div class="stats-row">
    <div class="stat-card"><div class="stat-label">Позицій у меню</div><div class="stat-value">12</div></div>
    <div class="stat-card"><div class="stat-label">Хвилин на доставку</div><div class="stat-value">30</div></div>
    <div class="stat-card"><div class="stat-label">Рейтинг закладу</div><div class="stat-value">4.9</div></div>
</div>

<h2 class="section-title">Популярні страви</h2>
<div class="featured-grid">
    <c:forEach var="item" items="${featuredItems}">
        <div class="food-card">
            <div class="food-card-img">
                <div class="food-card-img-text"><c:out value="${item.category.displayName}"/></div>
            </div>
            <div class="food-card-body">
                <h3><c:out value="${item.name}"/></h3>
                <p class="desc"><c:out value="${item.description}"/></p>
                <div class="food-card-foot">
                    <span class="price"><fmt:formatNumber value="${item.price}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
                    <c:if test="${item.available}">
                        <c:choose>
                            <c:when test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin}">
                                <form method="post" action="${pageContext.request.contextPath}/order/add" style="display:inline">
                                    <input type="hidden" name="menuItemId" value="${item.id}">
                                    <input type="hidden" name="quantity" value="1">
                                    <button type="submit" class="btn btn-primary btn-sm">Додати</button>
                                </form>
                            </c:when>
                            <c:when test="${empty sessionScope.currentUser}">
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Увійти</a>
                            </c:when>
                        </c:choose>
                    </c:if>
                    <c:if test="${!item.available}"><span class="badge badge-cancelled">Немає</span></c:if>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
<div style="text-align:center;"><a href="${pageContext.request.contextPath}/menu" class="btn btn-outline">Переглянути все меню</a></div>
<jsp:include page="footer.jsp"/>
