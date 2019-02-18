<%@page import="member.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
function result() {
	// join.jsp(창을열게끔한페이지) id텍스트상자 value값<= 중복체그 선택한아이디value값 
	opener.document.fr.id.value=document.wfr.userId.value;
	//창닫기
	window.close();
}
</script>
</head>
<body>
<h1>WebContent/member/idCheck.jsp</h1>
<h1>아이디 중복체크</h1>
<%
// String id = userId 파라미터 가져오기
String id=request.getParameter("userId");
// MemberDAO mdao 객체생성
MemberDAO mdao=new MemberDAO();
// int check = idCheck(id)메서드호출
int check=mdao.idCheck(id);
// check==1 "아이디중복 다른아이디 검색하세요"
// check==0 "아이디없음 사용가능한 아이디이다"
if(check==1){
	%>아이디중복 다른아이디 검색하세요<%
}else{
	%>아이디없음 사용가능한 아이디이다
	<input type="button" value="아이디선택" onclick="result()">
	<%
}
%>
<form action="idCheck.jsp" method="get" name="wfr">
검색한 아이디<input type="text" name="userId" value="<%=id%>">
<input type="submit" value="아이디중복체크"> 
</form>
</body>
</html>



