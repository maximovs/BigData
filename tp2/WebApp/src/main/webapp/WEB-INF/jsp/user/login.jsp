<%@ include file="../header.jsp" %>
<div>Inicio<br></div>
<p/>
<form:form class="well form-inline" action="login" method="post" commandName="loginUserForm">
	<div class="error">
	<form:errors path="*" />
	</div>
	<label for="username">Usuario:</label>
	<form:input path="username" class="span3"/><!-- <input type="text" name="username" class="span3"/> -->
	<label for="password">Contrase&ntilde;a</label>
	<form:password path="password" /><!-- <input type="password" name="pwd" /> -->
	<input type="submit" value="Ingresar" class="btn" />
</form:form>
      

<%@ include file="../footer.jsp" %>