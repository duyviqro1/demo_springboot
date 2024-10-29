<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix ="c" uri = "jakarta.tags.core" %>
<form action="${pageContext.request.contextPath}/admin/categories/save" method="post" enctype = "multipart/form-data">

<h2> ${category.isEdit ? 'Edit Category' : 'Add New Category' }</h2>

	<input type="hidden" value="${category.isEdit}" name="isEdit">
	<input type="hidden" value="${category.id}" name="id">
	
	<label for="Categoryname">Category Name:</label>
	<br> 
		<input type="text"name="name" value="${category.name}">
	<br> 
		<label for="images">images:</label>
	<br> 
		<input type="text"id="imagess" name="images" value="${category.images}">
	<br>
		<label for="status">status:</label>
	<br> 
		<input type="text"name="status" value="${category.status}">

	<br> <c:if test="${category.isEdit }">
		<input type="submit" value="Update">
		</c:if>
		<c:if test="${!category.isEdit }">
		<input type="submit" value="Insert">
		</c:if>
</form>
