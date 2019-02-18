<%@page import="java.text.SimpleDateFormat"%>
<%@page import="board.BoardBean"%>
<%@page import="board.BoardDAO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="../css/default.css" rel="stylesheet" type="text/css">
<link href="../css/subpage.css" rel="stylesheet" type="text/css">
<!--[if lt IE 9]>
<script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE9.js" type="text/javascript"></script>
<script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/ie7-squish.js" type="text/javascript"></script>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js" type="text/javascript"></script>
<![endif]-->
<!--[if IE 6]>
 <script src="../script/DD_belatedPNG_0.0.8a.js"></script>
 <script>
   /* EXAMPLE */
   DD_belatedPNG.fix('#wrap');
   DD_belatedPNG.fix('#main_img');   

 </script>
 <![endif]-->
</head>
<body>
<div id="wrap">
<!-- 헤더들어가는 곳 -->
<jsp:include page="../inc/top.jsp" />
<!-- 헤더들어가는 곳 -->

<!-- 본문들어가는 곳 -->
<!-- 메인이미지 -->
<div id="sub_img_center"></div>
<!-- 메인이미지 -->

<!-- 왼쪽메뉴 -->
<nav id="sub_menu">
<ul>
<li><a href="#">Notice</a></li>
<li><a href="#">Public News</a></li>
<li><a href="#">Driver Download</a></li>
<li><a href="#">Service Policy</a></li>
</ul>
</nav>
<!-- 왼쪽메뉴 -->

<!-- 게시판 -->
<%
// num 파라미터 가져오기
int num=Integer.parseInt(request.getParameter("num"));
// BoardDAO bdao
BoardDAO bdao=new BoardDAO();
// updateReadCount(num) 조회수 증가함수 호출
bdao.updateReadCount(num);
// BoardBean bb = getBoard(num) num에 해당하는 정보 가져오기
BoardBean bb=bdao.getBoard(num);
// 엔터  \r\n  => <br>태그로 변경
String content=bb.getContent();
if(content!=null){
	content=content.replace("\r\n", "<br>");
}
%>
<article>
<h1>Notice Content </h1>
<table id="notice">
<tr><td class="twrite">글번호</td><td><%=bb.getNum() %></td>
    <td class="twrite">조회수</td><td><%=bb.getReadcount() %></td></tr>
<tr><td class="twrite">글쓴이</td><td><%=bb.getName() %></td>
    <td class="twrite">글쓴날짜</td><td><%=bb.getDate() %></td></tr>
<tr><td class="twrite">글제목</td><td colspan="3"><%=bb.getSubject() %></td></tr>
<tr><td class="twrite">글내용</td><td colspan="3"><%=content %></td></tr>
</table>
<div id="table_search">
<%
//로그인하면 글수정,글삭제,답글쓰기 버튼 보이기
// 세션값가져오기
String id=(String)session.getAttribute("id");
// 세션값이 있으면 글쓰기 버튼이 보이기
if(id!=null){
	// 로그인사람 ,글쓴사람이 동일하면 글수정 , 글삭제 버튼 보이기
	if(id.equals(bb.getName())){
		%>
   <input type="button" value="글수정" class="btn" 
          onclick="location.href='updateForm.jsp?num=<%=num%>'">
   <input type="button" value="글삭제" class="btn" 
          onclick="location.href='deleteForm.jsp?num=<%=num%>'">		
		<%
	}
	%>
   <input type="button" value="답글쓰기" class="btn" 
          onclick="location.href='reWriteForm.jsp?num=<%=num%>'">
	<%
}
%>
 <input type="button" value="글목록" class="btn" 
          onclick="location.href='notice.jsp'">
</div>
<div class="clear"></div>
<div id="page_control">
</div>
</article>
<!-- 게시판 -->
<!-- 본문들어가는 곳 -->
<div class="clear"></div>
<!-- 푸터들어가는 곳 -->
<jsp:include page="../inc/bottom.jsp" />
<!-- 푸터들어가는 곳 -->
</div>
</body>
</html>