<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<title>Insert title here</title>
</head>
<body>
	<form id="uploadForm" name="uploadForm" method="post" enctype="multipart/form-data">
		<input id="upload" name="upload" type="file" multiple>
		<input id="uploadButton" name="uploadButton" type="button" value="send">
		<!-- <input id="uploadText" name="uploadText" type="text"> -->
	</form>
	<!-- <button id="uploadButton" name="uploadButton">button</button>  -->
	<p id="mime"></p><br>
	<p id="status"></p><br>
	<p id="jqxhr"></p><br>
	
<script>
$(document).ready(function() {
/* 	
	$("#uploadButton").click(function() {
		var serial = $("form").serialize();
		console.log(serial);
	});
*/	

 	$("#uploadButton").click(function() {
 	 	var form = $("form")[0];
 	 	var formData = new FormData(form);
 	 	
 	 	/*
			dataType: The type of data expected from the server. Default: Intelligent Guess (xml, json, script, text, html)
			$.ajax: http://api.jquery.com/jQuery.ajax
 	 	*/
 	 	
 	 	$.ajax({
 	 		url: "uploadByAjax.do",
 	 		processData: false,
 	 		contentType: false,
 	 		data: formData,
 	 		dataType: "text",
 	 		type: "POST",
 	 		success: function(result, textStatus, jqXHR) {
				//console.log(textStatus);
				//console.log(jqXHR);
				$("#mime").html(result);
				$("#status").html(textStatus);
				$("#jqxhr").html(jqXHR);
 	 		}
 	 	}); 
 	});

/*
 	$.post("uploadAjax.do", $("#uploadForm").serialize(), function(data) {
		$("p").html(data);
	});
*/
});
</script>
</body>
</html>

<!--
action="parseMime.do"
target="myframe"

-->
 