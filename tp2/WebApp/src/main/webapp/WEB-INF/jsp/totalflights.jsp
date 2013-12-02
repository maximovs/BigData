
<%@ include file="header.jsp" %>


<c:if test="${empty flightsList}">
	<h2>No se encontraron grupos</h2>
</c:if>

<c:if test="${not empty flightsList}">
	<h2>Vuelos para Septiembre 2001</h2>
	
	<table class="table table-striped">
		<thead>
		<tr>
			<th>Fecha</th>
			<th>Totales</th>
			<th>Cancelados</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${flightsList}" var="fl">
			<tr>
			<td>${fl.date}</td>
			<td>${fl.total}</td>
			<td>${fl.cancelled}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<h3>Total de vuelos: ${total}</h3>
	<h3>Total Cancelados: ${totalcancelled}</h3>
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
            text: 'Flight analysys for September 2001'
        },
        subtitle: {
            text: 'Source: airplenes.com'
        },
        xAxis: {
            categories: [
					<c:forEach items="${flightsList}" var="tf">
						'${tf.date}',
					</c:forEach>
            ]
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Flights'
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
        {
        	name: 'Arrived',
        	data: [
        	            <c:forEach items="${flightsList}" var="tf">
        	            ${tf.total},
        	            </c:forEach>
        	            ]
        },
        {
        	name:'Cancelled',
        	data: [
   	            <c:forEach items="${flightsList}" var="tf">
	            ${tf.cancelled},
	            </c:forEach>
	            ]
        },{
        	name:'Total',
        	data: [
   	            <c:forEach items="${flightsList}" var="tf">
	            ${tf.cancelled + tf.total},
	            </c:forEach>
	            ]
        }
        ]
    });
});
</script>


<%@ include file="footer.jsp" %>