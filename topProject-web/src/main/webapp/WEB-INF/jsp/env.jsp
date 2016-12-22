<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<body>
	<table border="1">
	<c:forEach var="map" items="${systemEnvironment}">
		<!-- key is ${map.key}, value is ${map.value}  -->
		<tr>
			<td>${map.key}</td>
			<td>${map.value}</td>
		</tr>
	</c:forEach>
	</table>
</body>
<script type="text/javascript">
var agent = navigator.userAgent; 
var name = navigator.appName;
console.log(agent);
console.log(name);
</script>
</html>


		
