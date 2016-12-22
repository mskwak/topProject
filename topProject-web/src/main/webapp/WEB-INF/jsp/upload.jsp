<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- target에는 post 요청을 날린 후 서버로 부터의 응답(json, html, xml...)을 표시할 타겟을 지정한다. 
	서버로부터의 응답이 html 이면 html 페이지가 iframe 안에 표시되고,
	서버로부터의 응답이 json 문자열 이면 json 문자열이 iframe 안에 표시된다.
	
	또 한가지, post 요청을 날린 후 브라우저 상에서 F5 버튼을 눌렀을 때의 서로 다른 반응
	target을 iframe의 name으로 지정했을 경우: F5 버튼을 눌러도 브라우저 상에서의 경고창이 뜨지 않는다.
	target을 지정하지 않은 경우: F5 버튼을 눌렀을 때 경고창이 출력된다. (FF에서의 경고창 문구: 현재 페이지를 표시하려면, Firefox가 이전에 수행했던 정보가 필요합니다. 이전에 수행했던 작업(검색 혹은 입력 양식 제출)을 다시 반복합니다.)
	-->
	
	<!--
		* 웹프로그래밍에서 이름짓기의 중요성: 파일 이름을 수정해야 할 경우...
		1. 파일시스템 상에서 파일 이름을 고쳐야 하고
		2. jsp 소스 파일 내에서 파일 이름을 고쳐야 하고
		3. 컨트롤러 클래스에서 파일 이름을 고쳐야 한다.	
	--> 
	
	<form action="upload.do" method="post" enctype="multipart/form-data" target="myframe">
		<input type="file" name="upload" multiple>
		<input type="submit"/>
	</form>
	
	<iframe name="myframe"></iframe>
	<!-- <iframe name="myframe" style="display: none"></iframe> -->
</body>
</html>