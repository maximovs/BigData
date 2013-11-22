
<%@ include file="../header.jsp" %>


<c:if test="${empty groupCountList}">
	<h2>No se encontraron grupos</h2>
</c:if>

<c:if test="${not empty groupCountList}">
	<h2>Grupos encontrados</h2>
	
	<table class="table table-striped">
		<thead>
		<tr>
			<th>Nombre</th>
			<th>Cantidad</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${groupCountList}" var="gc">
			<tr>
			<td>${gc.groupId}</td>
			<td>${gc.ammount}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</c:if>


<%@ include file="../footer.jsp" %>