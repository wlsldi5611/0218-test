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
request.setCharacterEncoding("utf-8");
// 검색어 파라미터 가져오기  search
String search = request.getParameter("search");
// BoardDAO bdao
BoardDAO bdao=new BoardDAO();
// int count = 검색어를 포함한 글개수 가져오는 메서드 호출 getBoardCount()
int count=bdao.getBoardCount(search);
int pageSize=15;// 한화면에 보여줄 글개수
// String pageNum 파라미터 가져오기
String pageNum=request.getParameter("pageNum");
// pageNum가 없으면 "1"페이지 설정
if(pageNum==null){
	pageNum="1";
}
// int currentPage= pageNum 정수형변경
int currentPage=Integer.parseInt(pageNum);
// int startRow= 1, 11, 21,31 ..
int startRow=(currentPage-1)*pageSize+1;
// int endRow=; 10 20 30 40
int endRow=currentPage*pageSize;
List boardList=null;
if(count!=0){
	boardList = bdao.getBoardList(startRow, pageSize, search);
}
%>
<article>
<h1>Notice Search[전체글개수 : <%=count %>]</h1>
<table id="notice">
<tr><th class="tno">No.</th>
    <th class="ttitle">Title</th>
    <th class="twrite">Writer</th>
    <th class="tdate">Date</th>
    <th class="tread">Read</th></tr>
    <%
    // 날짜 출력 포맷
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
    for(int i=0;i<boardList.size();i++){
    	BoardBean bb = (BoardBean)boardList.get(i);
    	%>
<tr><td><%=bb.getNum() %></td>
<td class="left"><%=bb.getSubject() %></td>
    <td><%=bb.getName() %></td><td><%=sdf.format(bb.getDate()) %></td>
    <td><%=bb.getReadcount() %></td></tr>
    	<%
    }
    %>
</table>
<%
//로그인하면 글쓰기 버튼 보이기
// 세션값가져오기
String id=(String)session.getAttribute("id");
// 세션값이 있으면 글쓰기 버튼이 보이기
if(id!=null){
	%>
<div id="table_search">
   <input type="button" value="글쓰기" class="btn" 
          onclick="location.href='writeForm.jsp'">
</div>
	<%
}
%>
<div id="table_search">
<form action="noticeSearch.jsp" method="post">
<input type="text" name="search" class="input_box">
<input type="submit" value="search" class="btn">
</form>
</div>
<div class="clear"></div>
<div id="page_control">
<a href="#">Prev</a>
<a href="#">1</a><a href="#">2</a><a href="#">3</a>
<a href="#">4</a><a href="#">5</a><a href="#">6</a>
<a href="#">7</a><a href="#">8</a><a href="#">9</a>
<a href="#">10</a>
<a href="#">Next</a>
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