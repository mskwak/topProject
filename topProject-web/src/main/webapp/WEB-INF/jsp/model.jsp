<!-- 아래 JSTL을 사용하기 위해 pom.xml에 jstl, tablib 설정을 해주어야 했다. -->  
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<body>
	<!-- 별도의 maven 설정을 하지 않아도 EL을 사용할 수 있었다. -->
	
	
	Map: ${lottoAsMap} <br>
	
	<c:forEach var="map" items="${lottoAsMap}">
		<!-- key is ${map.key}, value is ${map.value}  -->
		
		<c:forEach var="integer" items="${map.value}">
<%-- 		
			<fmt:formatNumber minIntegerDigits="2" value="${integer}" />
			<img src="http://localhost/image/digit/${integer}.png"/>
--%>		
			<img src="http://localhost/image/digit/<fmt:formatNumber minIntegerDigits="2" value="${integer}" />.png" />
		</c:forEach>
		<br>
	</c:forEach>
	
	<br>
	
	${lottoAsList} <br>
	
	<c:forEach var="list" items="${lottoAsList}">
		<c:forEach var="integer" items="${list}">
			${integer}
		</c:forEach>
		<br>
	</c:forEach>
</body>
</html>


		
