<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="//apis.daum.net/maps/maps3.js?apikey=1c467eea10b19ef1154e3d458e996d7c"></script>
<script type="text/javascript" src="//apis.daum.net/maps/maps3.js?apikey=1c467eea10b19ef1154e3d458e996d7c&libraries=services,clusterer"></script>
<title>Insert title here</title>
</head>
<body>
	<div id="map" style="width:100%; height:100%;"></div>
	<script>
		var container = document.getElementById('map');
		var options = {
			center: new daum.maps.LatLng(37.53717, 127.14412),
			level: 1,
			keyboardShortcuts: true,
		};

		var map = new daum.maps.Map(container, options);
	</script>
</body>
</html>