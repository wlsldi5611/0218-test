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
// BoardDAO bdao
BoardDAO bdao=new BoardDAO();
// int count = 글개수 가져오는 메서드 호출 getBoardCount()
int count=bdao.getBoardCount();
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
	boardList = bdao.getBoardList(startRow, pageSize);
}
%>
<article>
<h1>Notice [전체글개수 : <%=count %>]</h1>
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
<tr onclick="location.href='content.jsp?num=<%=bb.getNum()%>'">
<td><%=bb.getNum() %></td>
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
<%
if(count!=0){
	// 게시판 전체 글페이지 수 구하기
	int pageCount= (count/pageSize) + (count%pageSize==0?0:1);
	// 한화면에 보여질 페이지 수 설정 
	int pageBlock=10;
	// 한화면에 시작하는 페이지번호 구하기  현페이지 1~10 => 1  / 11 ~ 20 => 11
	int startPage=((currentPage-1)/pageBlock)*pageBlock+1;
	// 한화면에 끝나는 페이지번호 구하기  startPage 10  20  30  40  ...
	int endPage=startPage+pageBlock-1;
	if(endPage > pageCount){
		endPage=pageCount;
	}
	if(startPage > pageBlock){
		%><a href="notice.jsp?pageNum=<%=startPage-pageBlock%>">Prev</a><%
	}
	for(int i=startPage;i<=endPage;i++){
		%><a href="notice.jsp?pageNum=<%=i%>"><%=i %></a><%
	}
	if(endPage < pageCount){
		%><a href="notice.jsp?pageNum=<%=startPage+pageBlock%>">Next</a><%
	}
}
%>
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