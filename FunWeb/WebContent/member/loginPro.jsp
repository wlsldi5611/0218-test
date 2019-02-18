
<%@page import="member.MemberDAO"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>WebContent/member2/loginPro.jsp</h1>
	<%
		//한글처리
		request.setCharacterEncoding("utf-8");
		// id, pass 파라미터 가져오기
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		//MemberDAO에서  메서드만들기    
		//리턴할형  int  userCheck(String id,String pass)
		//MemberDAO mdao 객체생성
		MemberDAO mdao = new MemberDAO();
		//메서드호출    int check  = MemberDAO주소.userCheck(id,pass)
		int check = mdao.userCheck(id, pass);
		//check ==1  세션값 생성, main.jsp이동
		//check==0  "비밀번호틀림" 뒤로이동 
		//check==-1  "아이디없음" 뒤로이동
		if (check == 1) {
			//세션값생성 "id",id값
			session.setAttribute("id", id);
			// 이동 main.jsp 
			response.sendRedirect("../main/main.jsp");
		} else if (check == 0) {
	%>
	<script>
		alert("비밀번호틀림");
		history.back();
	</script>
	<%
		} else {
	%>
	<script>
		alert("아이디없음");
		history.back();
	</script>
	<%
		}
	%>



</body>
</html>







