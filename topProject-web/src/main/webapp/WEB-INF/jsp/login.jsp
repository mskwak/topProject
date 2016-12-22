<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<body>
	<!--<form name="formName01" action="login.do" method="post" target="me"> -->
	<form name="formName01" action="login.do" method="post">
	<input name="id" type="text" value="${id}">
	<input name="password" type="text" value="${password}">
	<input type="submit">
	</form>
	
</body>
<!--  <iframe name="me" onload="<alert>aaa</alert>"></iframe>  -->
</html>
