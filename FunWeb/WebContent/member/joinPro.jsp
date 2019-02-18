<%@page import="member.MemberDAO"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="member.MemberBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- WebContent/member/joinPro.jsp -->
<%
// 한글처리
request.setCharacterEncoding("utf-8");
// 파라미터 가져오기
String id=request.getParameter("id");
String pass=request.getParameter("pass");
String name=request.getParameter("name");
String email=request.getParameter("email");
String address=request.getParameter("address");
String phone=request.getParameter("phone");
String mobile=request.getParameter("mobile");
// 패키지 member 파일 MemberBean
// MemberBean mb객체생성
MemberBean mb=new MemberBean();
// mb 멤버변수 <= 파라미터 가져온값 저장
mb.setId(id);
mb.setPass(pass);
mb.setName(name);
mb.setEmail(email);
mb.setAddress(address);
mb.setPhone(phone);
mb.setMobile(mobile);
// 액션태그 useBean mb2객체생성
// 액션태그 setProperty 폼,멤버변수 이름 일치하면 자동을 저장
%>
<jsp:useBean id="mb2" class="member.MemberBean"/>
<jsp:setProperty property="*" name="mb2"/>
<%
//날짜 저장
mb.setReg_date(new Timestamp(System.currentTimeMillis()));
// 패키지 member 파일 MemberDAO
// MemberDAO mdao객체생성
MemberDAO mdao=new MemberDAO();
// insertMember() 메서드호출
mdao.insertMember(mb);
// login.jsp 이동
response.sendRedirect("login.jsp");
%>




