<%@page import="board.BoardDAO"%>
<%@page import="board.BoardBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
// writePro.jsp
//한글처리
request.setCharacterEncoding("utf-8");
// BoradBean bb 객체생성 
BoardBean bb=new BoardBean();
// 멤버변수 <- 파라미터 값 가져와서 저장
bb.setName(request.getParameter("name"));
bb.setPass(request.getParameter("pass"));
bb.setSubject(request.getParameter("subject"));
bb.setContent(request.getParameter("content"));
// 액션태그 useBean bb2객체생성
// 액션태그 setProperty 
%>
<jsp:useBean id="b" class="board.BoardBean"/>
<jsp:setProperty property="*" name="b"/>
<%
// BoardDAO bdao 객체생성
BoardDAO bdao=new BoardDAO();
// 메서드호출 insertBoard(BoardBean bb)
bdao.insertBoard(bb);
// notice.jsp 이동
response.sendRedirect("notice.jsp");
%>
</body>
</html>





