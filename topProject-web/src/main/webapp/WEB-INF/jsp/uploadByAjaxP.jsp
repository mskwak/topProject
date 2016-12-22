<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script src="/js/upload/vendor/jquery.ui.widget.js"></script>
<script src="/js/upload/jquery.iframe-transport.js"></script>
<script src="/js/upload/jquery.fileupload.js"></script>
<title>Insert title here</title>
<style>
.bar {
    height: 18px;
    background: green;
}
</style>
</head>
<body>
	<input id="fileupload" type="file" name="u" multiple>
	<p>file</p>
	
	<div id="progress">
    	<div class="bar" style="width: 0%;"></div>
	</div>
<!-- 	
	<form id="uploadForm" name="uploadForm" method="post" enctype="multipart/form-data">
		<input id="fileupload" name="upload" type="file" multiple>
		<input id="uploadButton" name="uploadButton" type="button" value="send">
		<input id="uploadText" name="uploadText" type="text">

	<button id="uploadButton" name="uploadButton">button</button> 
	<p id="mime"></p><br>
	<p id="status"></p><br>
	<p id="jqxhr"></p><br> -->
	
<script>
$(function () {
    $('#fileupload').fileupload({
    	url: "uploadByAjaxP.do",
    	dataType: 'json',
        add: function(e, data) {
        	$.each(data.files, function(index, file) {
        		console.log(file);
        	});
			data.submit();
		},
        done: function (e, data) {
            $.each(data.result.files, function (index, file) {
                $('<p>').text(file.name).appendTo(document.body);
            });
        },
        progress: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .bar').css('width', progress + '%');
        }
    });
});
</script>
</body>
</html>

<!--
action="parseMime.do"
target="myframe"

-->
 