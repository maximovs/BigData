<%@ include file="../header.jsp" %>
<p/>
<h3><a href='../check/search'>Buscar cheques</a></h3><br>
<h3>Ingresar</h3><br>
<h4><a href='../check/add'>Cheque</a></h4><br>
<h4><a href='../checkprovider/add'>Proveedor de cheques</a></h4><br>
<h4><a href='../bank/add'>Banco</a></h4><br>
<h3>Cheques rechazados</h3>
<h4><a href='checkSearchResults?status=2'>Ver</a></h4><br>
<h4><a href='rejectEntry'>Rechazar un cheque</a></h4><br>
<c:if test="${user.clearence == 1}">
<h3>Administradores</h3>
<h4><a href='add'>Registrar un usuario</a></h4><br>
<h4><a href='disapproveEntry'>Cancelar una entrada ya aprobada (se puede volver a aprobar)</a></h4><br>
<h4><a href='removeEntry'>Eliminar un cheque</a></h4><br>
</c:if>
<h4><a href='logout'>Salir</a></h4><br>

<%@ include file="../footer.jsp" %>