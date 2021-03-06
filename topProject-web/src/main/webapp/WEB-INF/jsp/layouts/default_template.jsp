<!-- 아래 인코딩 설정을 해주어야만 브라우저에서 한글이 깨지지 않고 정상 출력된다. -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- 아래 JSTL을 사용하기 위해 pom.xml에 jstl, tablib 설정을 해주어야 했다. -->
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<html>
 <head>
  
 </head>
 <body>
 	<table border="1" style="width:100%; height:100%">
 		<tr>
 			<td colspan="2"><tiles:insertAttribute name="header" /></td>
 		</tr>
 		<tr>
 			<td style="width:200px"><tiles:insertAttribute name="menu" /></td>
 			<td><tiles:insertAttribute name="body" /></td>
 		</tr>
 		<tr>
 			<td colspan="2"><tiles:insertAttribute name="footer" /></td>
 		</tr>
 	</table>
 </body>
</html>