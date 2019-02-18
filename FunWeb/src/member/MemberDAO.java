package member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	// 디비연결 메서드
	private Connection getConnection() throws Exception{
//		Connection con=null;
//		//1단계  - 디비연결하기위한 설치한 프로그램 가져오기(불러오기)
//		Class.forName("com.mysql.jdbc.Driver");
//		//2단계 - 가져온 프로그램을 이용해서 디비연결(디비주소,디비접속아이디, 디비접속비밀번호)
//		String dbUrl="jdbc:mysql://localhost:3306/jspdb5";
//		String dbUser="jspid";
//		String dbPass="jsppass";
//		con=DriverManager.getConnection(dbUrl,dbUser,dbPass);
//		return con;
		
		Connection con=null;
		Context init=new InitialContext();
		DataSource ds=(DataSource)init.lookup("java:comp/env/jdbc/MysqlDB");
		con=ds.getConnection();
		return con;
	}
	
	//inserMember메서드
	public  void insertMember(MemberBean mb) {
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			//1단계  - 디비연결하기위한 설치한 프로그램 가져오기(불러오기)
			//2단계 - 가져온 프로그램을 이용해서 디비연결(디비주소,디비접속아이디, 디비접속비밀번호)
			con=getConnection();
			
			//3단계 - sql 구문을 만들고 실행할 객체생성(Statement, PreparedStatement, CallableStatement)
			String sql="insert into member(id,pass,name,reg_date,email,address,phone,mobile) values(?,?,?,?,?,?,?,?);";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, mb.getId());
			pstmt.setString(2, mb.getPass());
			pstmt.setString(3, mb.getName());
			pstmt.setTimestamp(4, mb.getReg_date());
			pstmt.setString(5, mb.getEmail());
			pstmt.setString(6, mb.getAddress());
			pstmt.setString(7, mb.getPhone());
			pstmt.setString(8, mb.getMobile());
			//4단계 - 객체 실행 insert,update,delete
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//예외상관없이 마무리 
			// 객체생성 기억장소 해제  , 정리
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {} 
		}
	}//inserMember메서드
	
	//getMember메서드
	public MemberBean  getMember(String id) {
		MemberBean mb=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		//MemberBean mb=new MemberBean();
		try {
			//1단계 드라이버 불러오기
			//2단계 디비연결
			con=getConnection();
			
			//3단계 sql만들고 실행하는 객체생성
			String sql="select * from member where id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,id);
			//4단계 rs=실행 결과 저장
			rs=pstmt.executeQuery();
			//5단계 rs내용 가져와서 => mb에 저장
			if(rs.next()) {
				// 데이터 있으면 객체생성
				mb=new MemberBean();
				//  rs 첫행의 id 열가져와서 => mb에 id변수저장
				mb.setId(rs.getString("id"));
				mb.setPass(rs.getString("pass"));
				mb.setName(rs.getString("name"));
				mb.setReg_date(rs.getTimestamp("reg_date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//예외상관없이 기억장소 정리
			if(rs!=null) try{rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {} 
		}
		return mb;
	}//getMember메서드
	
	//userCheck메서드
	public int  userCheck(String id,String pass) {
		int check=-1;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			//1단계  - 디비연결하기위한 설치한 프로그램 가져오기(불러오기)
			//2단계 - 가져온 프로그램을 이용해서 디비연결(디비주소,디비접속아이디, 디비접속비밀번호)
			con=getConnection();
			//3단계 - sql 구문을 만들고 실행할 객체생성(Statement, PreparedStatement, CallableStatement)
			// select  pass열   조건 id=?
			String sql="select pass from member where id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,id);
			//4단계 - 실행결과저장 = 객체 실행       select
			rs=pstmt.executeQuery();
			//5단계 - 저장된 내용을 비교
			if(rs.next()){
				//데이터 있으면 "아이디 있음"
				if(pass.equals(rs.getString("pass"))){ //비밀번호 맞으면   "비밀번호맞음" 세션값생성 "id",id값
				check=1;
				}else{  //비밀번호 틀리면 "비밀번호틀림"
					check=0;
				}
			}else{
				//데이터 없으면 "아이디없음"
				check=-1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//닫기
			if(rs!=null) try{rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {} 
		}
		return check;
	}
	
	//updateMember(mb)
	public void updateMember(MemberBean mb) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			//1단계 드라이버 불러오기
			//2단계 디비연결
			con=getConnection();
			
			//3
			String sql="update member set name=? where id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, mb.getName()); //첫번째물음표 문자열 자동으로 ''붙음 ,값
			pstmt.setString(2, mb.getId()); //두번째물음표 정수형 ,값
			//4단계 - 객체 실행 insert,update,delete  
			pstmt.executeUpdate(); 
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//객체 닫기
			if(rs!=null) try{rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {} 
		}
	}
	//deleteMember(id,pass)
	public void deleteMember(String id,String pass) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			//1단계 드라이버로더
			//2단계 디비연결
			con=getConnection();
			//3단계 sql
			String sql="delete from member where id=? and pass=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id); //첫번째물음표 문자열 자동으로 ''붙음 ,값
			pstmt.setString(2, pass); //두번째물음표 문자열 ,값
			//4단계 실행
			pstmt.executeUpdate(); 
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//닫기
			if(rs!=null) try{rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {} 
		}
	}
	//getMemberList()
	public List getMemberList() {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List memberList =new ArrayList();
		try {
			//1단계 드라이버 로더
			//2단계 디비연결
			con=getConnection();
			//3단계 sql 만들기 member모든 회원다 가져오기
			String sql="select * from member";
			pstmt=con.prepareStatement(sql);
			//4단계 rs = 실행 결과 저장
			rs=pstmt.executeQuery();
			//5단계 while 첫행으로 이동 데이터 있으면
			//      MemberBean mb 객체 생성  
			//   MemberBean 멤버변수 id,pass,name,reg_date
			//    <= rs 가져온 id, pass, name, reg_date 내용 저장
			//  배열 memberList한 칸에 저장  memberList.add(mb)
			while(rs.next()) {
				MemberBean mb=new MemberBean();
				mb.setId(rs.getString("id"));
				mb.setPass(rs.getString("pass"));
				mb.setName(rs.getString("name"));
				mb.setReg_date(rs.getTimestamp("reg_date"));
				memberList.add(mb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//객체닫기
			if(rs!=null) try{rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {} 
		}
		return memberList;
	}
	
	//idCheck(id)
	public int idCheck(String id) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		int check=0;
		try {
			//1,2 
			con=getConnection();
			//3 sql
			String sql="select * from member where id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			//4 rs <= 실행저장
			rs=pstmt.executeQuery();
			//5 rs 첫행 데이터 있으면 아이디중복 check=1 아이디없음 check=0
			if(rs.next()) {
				check=1;
			}else {
				check=0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) try{rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {} 
		}
		return check;
	}
	
}//클래스







