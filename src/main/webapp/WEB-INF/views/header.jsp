<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${not empty pageTitle ? pageTitle : 'SmakUA'}"/> — Система замовлення їжі</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg:       #f7f4ef;
            --surface:  #ffffff;
            --warm:     #f0e8dc;
            --brown:    #6b4e35;
            --dark:     #1e120a;
            --accent:   #c96a2a;
            --green:    #2e6b40;
            --red:      #9b2b2b;
            --gold:     #b8943a;
            --muted:    #7a6a5a;
            --border:   #ddd0c0;
            --shadow:   rgba(30,18,10,.10);
            --radius:   10px;
        }
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Inter', sans-serif; background: var(--bg); color: var(--dark); min-height: 100vh; display: flex; flex-direction: column; font-size: 15px; }

        /* ── Navbar ── */
        nav { background: var(--dark); display: flex; align-items: center; justify-content: space-between; padding: 0 2rem; position: sticky; top: 0; z-index: 200; box-shadow: 0 2px 12px var(--shadow); }
        .brand { font-family: 'Playfair Display', serif; font-size: 1.5rem; font-weight: 700; color: var(--gold); text-decoration: none; padding: 1rem 0; letter-spacing: -.5px; }
        .nav-group { display: flex; align-items: center; gap: .15rem; }
        .nav-link { color: #ccc0b0; text-decoration: none; padding: .5rem .9rem; border-radius: 6px; font-size: .88rem; font-weight: 500; transition: background .15s, color .15s; white-space: nowrap; }
        .nav-link:hover { background: rgba(255,255,255,.08); color: #fff; }
        .nav-link.active { background: var(--accent); color: #fff; }
        .nav-sep { width: 1px; height: 20px; background: rgba(255,255,255,.12); margin: 0 .5rem; }
        .nav-badge { display: inline-block; background: var(--red); color: #fff; border-radius: 99px; font-size: .68rem; padding: 1px 5px; margin-left: 3px; font-weight: 700; vertical-align: middle; }
        .nav-user { color: #ccc0b0; font-size: .82rem; padding: .4rem .8rem; border-radius: 6px; border: 1px solid rgba(255,255,255,.12); }

        /* ── Layout ── */
        main { flex: 1; padding: 2rem; max-width: 1300px; margin: 0 auto; width: 100%; }
        footer { background: var(--dark); color: #6a5a4a; text-align: center; padding: 1.25rem; font-size: .82rem; margin-top: auto; }

        /* ── Card ── */
        .card { background: var(--surface); border-radius: var(--radius); border: 1px solid var(--border); }

        /* ── Buttons ── */
        .btn { display: inline-flex; align-items: center; gap: 5px; padding: .5rem 1.1rem; border-radius: 7px; font-family: 'Inter', sans-serif; font-size: .88rem; font-weight: 600; cursor: pointer; border: none; text-decoration: none; transition: opacity .15s, box-shadow .15s; white-space: nowrap; }
        .btn:hover { opacity: .88; box-shadow: 0 3px 10px var(--shadow); }
        .btn:active { opacity: .75; }
        .btn-primary { background: var(--accent); color: #fff; }
        .btn-success { background: var(--green); color: #fff; }
        .btn-danger  { background: var(--red);   color: #fff; }
        .btn-outline { background: transparent; color: var(--brown); border: 1.5px solid var(--border); }
        .btn-dark    { background: var(--dark); color: #fff; }
        .btn-sm      { padding: .3rem .7rem; font-size: .8rem; }

        /* ── Alerts ── */
        .alert { padding: .8rem 1.1rem; border-radius: 8px; margin-bottom: 1.25rem; font-weight: 500; font-size: .9rem; }
        .alert-success { background: #ecf6ee; color: #1d5c2e; border: 1px solid #b5d9be; }
        .alert-error   { background: #fbecec; color: #7a2020; border: 1px solid #f0b8b8; }
        .alert-info    { background: #e8f0fb; color: #1a3d8a; border: 1px solid #b8ccf5; }
        .alert-warn    { background: #fdf6e3; color: #7a5a10; border: 1px solid #e8d890; }

        /* ── Table ── */
        table { width: 100%; border-collapse: collapse; }
        th { background: var(--warm); color: var(--brown); font-weight: 600; padding: .75rem 1rem; text-align: left; font-size: .8rem; text-transform: uppercase; letter-spacing: .5px; border-bottom: 2px solid var(--border); }
        td { padding: .75rem 1rem; border-bottom: 1px solid var(--border); font-size: .92rem; vertical-align: middle; }
        tr:last-child td { border-bottom: none; }
        tbody tr:hover td { background: var(--bg); }

        /* ── Status badges ── */
        .badge { display: inline-block; padding: .2rem .65rem; border-radius: 99px; font-size: .75rem; font-weight: 700; text-transform: uppercase; letter-spacing: .3px; }
        .badge-pending   { background: #fef3cd; color: #7a5c10; }
        .badge-confirmed { background: #d0e8ff; color: #0a3a7a; }
        .badge-preparing { background: #ffe4cc; color: #8a3a0a; }
        .badge-ready     { background: #d0ead8; color: #0a3a1a; }
        .badge-delivered { background: #e0e0e0; color: #4a4a4a; }
        .badge-cancelled { background: #fad8d8; color: #7a1a1a; }

        /* ── Forms ── */
        .form-group { margin-bottom: 1.1rem; }
        .form-group label { display: block; margin-bottom: .35rem; font-weight: 600; font-size: .85rem; color: var(--brown); }
        .form-control { width: 100%; padding: .58rem .85rem; border: 1.5px solid var(--border); border-radius: 7px; font-family: 'Inter', sans-serif; font-size: .92rem; background: #fff; color: var(--dark); transition: border-color .2s; }
        .form-control:focus { outline: none; border-color: var(--accent); box-shadow: 0 0 0 3px rgba(201,106,42,.12); }
        .form-control.error { border-color: var(--red); }
        textarea.form-control { resize: vertical; min-height: 80px; }
        .form-hint { font-size: .78rem; color: var(--muted); margin-top: .3rem; }

        /* ── Page header ── */
        .page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 1.75rem; padding-bottom: .85rem; border-bottom: 2px solid var(--border); gap: 1rem; flex-wrap: wrap; }
        .page-header h1 { font-family: 'Playfair Display', serif; font-size: 1.75rem; font-weight: 700; }

        /* ── Admin layout ── */
        .admin-layout { display: flex; gap: 1.75rem; align-items: flex-start; }
        .admin-sidebar { width: 210px; flex-shrink: 0; background: var(--surface); border-radius: var(--radius); border: 1px solid var(--border); overflow: hidden; }
        .admin-sidebar .sidebar-head { background: var(--dark); color: var(--gold); font-family: 'Playfair Display', serif; font-size: .9rem; padding: .85rem 1.1rem; font-weight: 700; letter-spacing: .3px; }
        .admin-sidebar a { display: block; padding: .7rem 1.1rem; color: var(--dark); text-decoration: none; font-size: .88rem; font-weight: 500; border-bottom: 1px solid var(--border); transition: background .12s; }
        .admin-sidebar a:last-child { border-bottom: none; }
        .admin-sidebar a:hover  { background: var(--warm); }
        .admin-sidebar a.active { background: var(--accent); color: #fff; font-weight: 600; }
        .admin-content { flex: 1; min-width: 0; }

        /* ── Misc ── */
        .text-muted   { color: var(--muted); }
        .text-accent  { color: var(--accent); }
        .text-success { color: var(--green); font-weight: 600; }
        .text-danger  { color: var(--red);   font-weight: 600; }
        .fw-bold      { font-weight: 700; }
        .available    { color: var(--green); font-weight: 600; }
        .unavailable  { color: var(--red);   font-weight: 600; }
    </style>
</head>
<body>
<nav>
    <a class="brand" href="${pageContext.request.contextPath}/">SmakUA</a>
    <div class="nav-group">
        <a href="${pageContext.request.contextPath}/"      class="nav-link ${currentPage=='home'?'active':''}">Головна</a>
        <a href="${pageContext.request.contextPath}/menu"  class="nav-link ${currentPage=='menu'?'active':''}">Меню</a>

        <c:choose>
            <c:when test="${not empty sessionScope.currentUser}">
                <%-- Links available only to logged-in users --%>
                <c:if test="${not sessionScope.currentUser.admin}">
                    <a href="${pageContext.request.contextPath}/order/cart" class="nav-link ${currentPage=='cart'?'active':''}">
                        Кошик
                        <c:if test="${not empty sessionScope.cart and sessionScope.cart.size() > 0}">
                            <span class="nav-badge">${sessionScope.cart.size()}</span>
                        </c:if>
                    </a>
                    <a href="${pageContext.request.contextPath}/my-orders"
                       class="nav-link ${currentPage=='myorders'?'active':''}">
                        Мої замовлення
                    </a>
                </c:if>
                <c:if test="${sessionScope.currentUser.admin}">
                    <div class="nav-sep"></div>
                    <a href="${pageContext.request.contextPath}/admin/menu"   class="nav-link ${currentPage=='admin'?'active':''}">Панель адміна</a>
                </c:if>
                <div class="nav-sep"></div>
                <span class="nav-user"><c:out value="${sessionScope.currentUser.fullName}"/> (${sessionScope.currentUser.role.displayName})</span>
                <a href="${pageContext.request.contextPath}/logout" class="nav-link">Вийти</a>
            </c:when>
            <c:otherwise>
                <div class="nav-sep"></div>
                <a href="${pageContext.request.contextPath}/login" class="nav-link ${currentPage=='login'?'active':''}">Увійти</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>
<main>
