<em><%@ include file="../header.jsp" %>

<h2>Registro de usuarios</h2>
<c:if test="${not empty errorList}">
	<h2>"Usuario no ingresado"</h2>
	<c:forEach items="${errorList}" var="errorMessage">
		<h4>${errorMessage.description}</h4>
	</c:forEach>
</c:if>
<c:if test="${not empty success}">
<h2>Usuario ingresado con &eacute;xito</h2>
</c:if>
<form:form class="form-vertical" action="add" method="post" commandName="addUserForm">
	<div class="error">
	<form:errors path="*" />
	</div>
	<fieldset>
		<div class="fieldset">
			<label class="control-label" for="username">Usuario:</label>
			<form:input path="username" class="span3"/>
		</div>
		<div class="control-label">
			<label for="name">Nombre:</label>
			<form:input path="name" class="span3"/>
		</div>
		<div class="control-label">
			<label for="dni">DNI: (sin puntos)</label>
			<form:input path="dni" class="span3"/>
		</div>
		<div class="control-label">
			<label for="clearence">Nivel de privilegios:</label>
			<form:select path="clearence">
			    <form:option value="7" label="Basico"/>
					<form:option value="3" label="Intermedio"/>
					<form:option value="1" label="Administrador"/>
			</form:select>
		</div>
		<div class="control-label">
			<label for="password">Contrase&ntilde;a:</label>
			<form:password path="password" />
		</div>
		<div class="control-label">
			<label for="password">Ingrese contrase&ntilde;a nuevamente:</label>
			<form:password path="password2" />
		</div>
		<div class="form-actions">
			<input type="submit" name="submit" value="Ingresar" />
		</div>
	</fieldset>
</form:form>
<div class="footer">Todos los datos son abligatorios</div>
<%@ include file="../footer.jsp" %></em>