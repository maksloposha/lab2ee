<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="currentPage" value="admin" scope="request"/>
<jsp:include page="../header.jsp"/>
<style>
.form-card{background:#fff;border-radius:var(--radius);border:1px solid var(--border);padding:1.75rem;max-width:640px}
.form-row{display:grid;grid-template-columns:1fr 1fr;gap:1rem}
.form-section{margin-bottom:1.5rem;padding-bottom:1.5rem;border-bottom:1px solid var(--border)}
.form-section:last-of-type{border-bottom:none;margin-bottom:0;padding-bottom:0}
.section-label{font-size:.75rem;text-transform:uppercase;letter-spacing:.8px;color:var(--muted);margin-bottom:1rem;font-weight:700}
.check-label{display:flex;align-items:center;gap:.6rem;cursor:pointer;font-weight:600;font-size:.9rem}
.check-label input[type=checkbox]{width:16px;height:16px;accent-color:var(--accent)}
</style>

<div class="page-header">
    <h1><c:out value="${pageTitle}"/></h1>
    <a href="${pageContext.request.contextPath}/admin/menu" class="btn btn-outline">До списку</a>
</div>

<div class="admin-layout">
    <aside class="admin-sidebar">
        <div class="sidebar-head">Панель адміністратора</div>
        <a href="${pageContext.request.contextPath}/admin/menu">Елементи меню</a>
        <a href="${pageContext.request.contextPath}/admin/menu/new"  class="active" >Додати страву</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Замовлення</a>
    </aside>

    <div class="admin-content">
        <div class="form-card">
            <form method="post" action="${formAction}">
                <c:if test="${not empty item}">
                    <input type="hidden" name="id" value="${item.id}">
                </c:if>

                <div class="form-section">
                    <div class="section-label">Основна інформація</div>
                    <div class="form-group">
                        <label for="name">Назва страви <span style="color:var(--red)">*</span></label>
                        <input type="text" id="name" name="name" class="form-control"
                               value="<c:out value='${item.name}'/>" required maxlength="100" placeholder="напр. Паста Карбонара">
                    </div>
                    <div class="form-group">
                        <label for="description">Опис страви</label>
                        <textarea id="description" name="description" class="form-control" rows="3"
                                  maxlength="500" placeholder="Короткий апетитний опис..."><c:out value="${item.description}"/></textarea>
                    </div>
                </div>

                <div class="form-section">
                    <div class="section-label">Ціна та категорія</div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="price">Ціна (грн) <span style="color:var(--red)">*</span></label>
                            <input type="number" id="price" name="price" class="form-control"
                                   value="${item.price}" required step="0.01" min="0.01" max="99999" placeholder="0.00">
                        </div>
                        <div class="form-group">
                            <label for="calories">Калорійність (ккал)</label>
                            <input type="number" id="calories" name="calories" class="form-control"
                                   value="${item.calories}" min="0" max="9999" placeholder="0">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="category">Категорія <span style="color:var(--red)">*</span></label>
                        <select id="category" name="category" class="form-control" required>
                            <c:forEach var="cat" items="${categories}">
                                <c:choose>
                                    <c:when test="${item.category == cat}">
                                        <option value="${cat.name()}" selected><c:out value="${cat.displayName}"/></option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${cat.name()}"><c:out value="${cat.displayName}"/></option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-section">
                    <div class="section-label">Доступність</div>
                    <label class="check-label">
                        <input type="checkbox" name="available"
                               ${empty item || item.available ? 'checked' : ''}>
                        Страва доступна для замовлення
                    </label>
                    <c:if test="${not empty item and !item.available}">
                        <p class="form-hint" style="color:var(--red);margin-top:.5rem;">Ця страва зараз прихована від клієнтів.</p>
                    </c:if>
                </div>

                <div style="display:flex;gap:.75rem;margin-top:1.5rem;padding-top:1.25rem;border-top:1px solid var(--border);">
                    <button type="submit" class="btn btn-success" style="font-size:.95rem;padding:.65rem 1.5rem;">
                        <c:choose>
                            <c:when test="${not empty item}">Зберегти зміни</c:when>
                            <c:otherwise>Додати страву</c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/menu" class="btn btn-outline">Скасувати</a>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
