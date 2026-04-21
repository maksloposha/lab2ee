<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentPage" value="menu" scope="request"/>
<c:set var="pageTitle"   value="Меню" scope="request"/>
<jsp:include page="../header.jsp"/>
<style>
.menu-layout{display:flex;gap:1.75rem;align-items:flex-start}

.cat-sidebar {
    flex-shrink: 0;
    background: #fff;
    border-radius: var(--radius);
    border: 1px solid var(--border);
    overflow: hidden;
    position: sticky;
    top: 72px;
    display: flex;
    flex-direction: column;
}

.cat-sidebar .sidebar-head{background:var(--warm);color:var(--brown);font-size:.75rem;padding:.7rem 1rem;font-weight:700;text-transform:uppercase;letter-spacing:.8px}
.cat-sidebar a {
    display: block;
    padding: .65rem 1rem;
    color: var(--dark);
    text-decoration: none;
    font-size: .88rem;
    border-bottom: 1px solid var(--border);
    transition: background .12s;
    width: 100%;        /* ← ось це */
    box-sizing: border-box;
}.cat-sidebar a:last-child{border-bottom:none}
.cat-sidebar a:hover{background:var(--warm)}
.cat-sidebar a.active{background:var(--accent);color:#fff;font-weight:600}
.menu-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(250px,1fr));gap:1.25rem;flex:1}
.menu-card{background:#fff;border-radius:var(--radius);border:1px solid var(--border);display:flex;flex-direction:column;transition:transform .18s,box-shadow .18s}
.menu-card:hover{transform:translateY(-2px);box-shadow:0 8px 20px var(--shadow)}
.menu-card.unavail{opacity:.6}
.menu-card-header{background:var(--warm);padding:.85rem 1rem;border-bottom:1px solid var(--border)}
.menu-card-header .cat-label{font-size:.72rem;font-weight:700;text-transform:uppercase;letter-spacing:.5px;color:var(--brown)}
.menu-card-header h3{font-weight:700;font-size:.97rem;margin-top:.2rem;color:var(--dark);line-height:1.3}
.menu-card-body{padding:1rem;flex:1;display:flex;flex-direction:column}
.menu-card-body .desc{color:var(--muted);font-size:.82rem;line-height:1.45;flex:1;margin-bottom:.85rem}
.menu-card-meta{display:flex;gap:.75rem;margin-bottom:.9rem;flex-wrap:wrap}
.meta-pill{font-size:.75rem;padding:.2rem .6rem;border-radius:99px;background:var(--bg);color:var(--muted);border:1px solid var(--border)}
.meta-pill.avail{background:#ecf6ee;color:var(--green);border-color:#b5d9be}
.meta-pill.unavail{background:#fbecec;color:var(--red);border-color:#f0b8b8}
.menu-card-foot{display:flex;align-items:center;justify-content:space-between;padding-top:.85rem;border-top:1px solid var(--border)}
.price{font-size:1.08rem;font-weight:700;color:var(--accent)}
.qty-form{display:flex;align-items:center;gap:.35rem}
.qty-input{width:42px;text-align:center;padding:.28rem;border:1.5px solid var(--border);border-radius:6px;font-size:.9rem;font-weight:600}
.empty-state{text-align:center;padding:4rem 2rem;color:var(--muted);grid-column:1/-1}
.empty-state h3{font-family:'Playfair Display',serif;font-size:1.3rem;margin-bottom:.5rem;color:var(--dark)}
</style>

<div class="page-header">
    <h1>Меню ресторану</h1>
    <c:if test="${not empty param.error}"><span class="alert alert-error" style="margin:0;padding:.45rem .9rem;">Товар недоступний</span></c:if>
</div>

<div class="menu-layout">
    <nav class="cat-sidebar">
        <div class="sidebar-head">Категорії</div>
        <a href="${pageContext.request.contextPath}/menu" class="${empty selectedCategory?'active':''}">Всі страви</a>
        <c:forEach var="cat" items="${categories}">
            <a href="${pageContext.request.contextPath}/menu?category=${cat.name()}"
               class="${selectedCategory==cat?'active':''}">${cat.displayName}</a>
        </c:forEach>
    </nav>

    <div class="menu-grid">
        <c:choose>
            <c:when test="${empty menuItems}">
                <div class="empty-state">
                    <h3>Страви не знайдено</h3>
                    <p>У цій категорії поки немає доступних страв.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="item" items="${menuItems}">
                    <div class="menu-card ${!item.available?'unavail':''}">
                        <div class="menu-card-header">
                            <div class="cat-label"><c:out value="${item.category.displayName}"/></div>
                            <h3><c:out value="${item.name}"/></h3>
                        </div>
                        <div class="menu-card-body">
                            <p class="desc"><c:out value="${item.description}"/></p>
                            <div class="menu-card-meta">
                                <span class="meta-pill">${item.calories} ккал</span>
                                <c:if test="${item.available}"><span class="meta-pill avail">Доступно</span></c:if>
                                <c:if test="${!item.available}"><span class="meta-pill unavail">Немає</span></c:if>
                            </div>
                            <div class="menu-card-foot">
                                <span class="price"><fmt:formatNumber value="${item.price}" pattern="#,##0.00"/>&nbsp;&#8372;</span>
                                <c:if test="${item.available}">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin}">
                                            <form method="post" action="${pageContext.request.contextPath}/order/add" class="qty-form">
                                                <input type="hidden" name="menuItemId" value="${item.id}">
                                                <input type="number" name="quantity" value="1" min="1" max="20" class="qty-input">
                                                <button type="submit" class="btn btn-primary btn-sm">Додати</button>
                                            </form>
                                        </c:when>
                                        <c:when test="${empty sessionScope.currentUser}">
                                            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Увійти</a>
                                        </c:when>
                                    </c:choose>
                                </c:if>
                                <c:if test="${!item.available}"><span class="badge badge-cancelled">Недоступно</span></c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
