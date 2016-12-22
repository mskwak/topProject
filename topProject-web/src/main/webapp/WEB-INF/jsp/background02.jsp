<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
</head>
<body>
<div id="left" style="width: 200px; height: 600px; float: left;">
	<ul>
		<li>env.do</li>
		<li>map.do</li>
		<li>model.do</li>
		<li>modelmap.do</li>
		<li>properties.do</li>
		<li>fileUpload.do</li>
		<li>uploadFrame.do</li>
		<li>uploadAjax.do</li>
		<li>input.do</li>
	</ul>
</div>
<div id="body"></div>
<script>
$("#left ul li").click(function() {
	var text = $(this).text();
	alert(text);
});
</script>
</body>
</html>