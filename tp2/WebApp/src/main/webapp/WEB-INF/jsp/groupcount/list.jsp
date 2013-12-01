
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

<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>

<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>

<script type="text/javascript">
$(function () {
    $('#container').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: 'Monthly Average Rainfall'
        },
        subtitle: {
            text: 'Source: WorldClimate.com'
        },
        xAxis: {
            categories: [
                'Jan',
            ]
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Rainfall (mm)'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [
        <c:forEach items="${groupCountList}" var="gc">
        {
        	name: '${gc.groupId}',
        	data: [${gc.ammount}]
        },
		</c:forEach>
       ]
    });
});
</script>


<%@ include file="../footer.jsp" %>