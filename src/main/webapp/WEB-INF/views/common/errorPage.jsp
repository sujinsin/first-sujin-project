<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="error-container" style="text-align:center;">
		<h1>Error</h1>
<%-- 		<h2 style="color:red;"><%= request.getAttribute("msg") %></h2> --%>
<%-- 		<h2 style="color:blue;"><%= request.getAttribute("javax.servlet.error.message") %></h2><!-- 옛방식 스크립트릿으로 표현하는거 보여주는것임.   --> --%>
		<h2 style="color:red;">${msg}</h2>
		<h2 style="color:blue;">${requestScope['javax.servlet.error.message']}</h2><!-- 옛방식 스크립트릿으로 표현하는거 보여주는것임.   -->
		<!-- 리퀘스트 스코프 객체안에 들어가있는 속성 이래여. 대괄호  -->
		
		<a href="home.do">시작페이지로 돌아가기</a>
	</div>
</body>
</html>