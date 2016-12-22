<!-- 아래 인코딩 설정을 해주어야만 브라우저에서 한글이 깨지지 않고 정상 출력된다. -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- 아래 JSTL을 사용하기 위해 pom.xml에 jstl, tablib 설정을 해주어야 했다. -->
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<body>
	${lottoAsMap} <br>
	
	<c:forEach var="map" items="${lottoAsMap}">
		<!-- key is ${map.key}, value is ${map.value}  -->
		
		<c:forEach var="integer" items="${map.value}">
			${integer}
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


		
