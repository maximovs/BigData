
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
	var chartOptions = {
	        chart: {
	            type: 'column',
	            events: {
	                load: function() {
	                }
	            }
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
	            },
	        	animate: false
	        },
	        series: [
	        <c:forEach items="${groupCountList}" var="gc">
	        {
	        	name: '${gc.groupId}',
	        	data: [${gc.ammount}]
	        },
			</c:forEach>
	       ]
	    };
    $('#container').highcharts(chartOptions);

    var prevData;
    setInterval(function() {
    	$.get("http://localhost:8080/WebApp/bin/groupcount/update", null, function( data ) {

    		  chartOptions.series = $.parseJSON(data);
              	  console.log(data);

		var dataHasChanged = false;
    		  if (prevData) {

              for (var i = 0; i < data.length;i++) {
                    var item = data[i];
                    if (prevData[i] != item) {
                        dataHasChanged = true;
			console.log("DATA CHANGED!!");
                        };
                    }

    		  }

              if (dataHasChanged) {
    		    $('#container').highcharts(chartOptions);
              }
            prevData = data;
    		});
    }, 1000);
});
</script>


<%@ include file="../footer.jsp" %>
