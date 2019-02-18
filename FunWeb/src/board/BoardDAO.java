package board;

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

public class BoardDAO {
	//1,2단계 디비연결 메서드
	private Connection getConnection() throws Exception{
//		Connection con=null;
//		//1단계 드라이버 로더
//		Class.forName("com.mysql.jdbc.Driver");
//		//2단계 디비연결
//		String dbUrl="jdbc:mysql://localhost:3306/jspdb5";
//		String dbUser="jspid";
//		String dbPass="jsppass";
//		con=DriverManager.getConnection(dbUrl,dbUser,dbPass);
//		return con;
		
		// 커넥션 풀(Connection Pool)
		// - 데이터베이스와 연결된  Connection 객체를 미리생성 하고 풀(Pool)저장
		// - 필요할때마다 풀에 접근하여 Connection 객체 이름을 호출해서 사용
		// - 작업이 끝나면 다시 반환
		
		// 톰캣에서 제공되는 DBCP API 사용
		// META-INF - context.xml 파일만들기
		
		Connection con=null;
		Context init=new InitialContext();
		DataSource ds=(DataSource)init.lookup("java:comp/env/jdbc/MysqlDB");
		con=ds.getConnection();
		return con;
	}
	//insertBoard(BoardBean bb)
	public void insertBoard(BoardBean bb) {
		Connection con=null;
		PreparedStatement pstmt=null;
		int num=0;
		String sql="";
		ResultSet rs=null;
		try {
			//1,2단계 디비연결 메서드
			con=getConnection();
			// num구하기
			//3단계 sql 가장 큰번호 구해오기  max(num)
			sql="select max(num) from board";
			pstmt=con.prepareStatement(sql);
			//4단계 rs <= 실행 결과 
			rs=pstmt.executeQuery();
			//5단계 rs에 접근 가장큰번호 구해오기 +1
			if(rs.next()) {
				num=rs.getInt("max(num)")+1;
			}
			
			//3 단계 sql 객체생성 insert
			sql="insert into board(num,name,pass,subject,content,readcount,date) values(?,?,?,?,?,?,now())";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num); //구하기
			pstmt.setString(2, bb.getName()); //파라미터 가져온값(자바빈 저장된값)
			pstmt.setString(3, bb.getPass());//파라미터 가져온값(자바빈 저장된값)
			pstmt.setString(4, bb.getSubject());//파라미터 가져온값(자바빈 저장된값)
			pstmt.setString(5, bb.getContent());//파라미터 가져온값(자바빈 저장된값)
			pstmt.setInt(6, 0);//조회수 초기값 0으로 설정
			//4 단계 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//기억장소 정리,닫기
			if(rs!=null) try {rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {}
		}
	}//insertBoard(BoardBean bb)
	
	//getBoardCount()
	public int getBoardCount() {
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		ResultSet rs=null;
		int count=0;
		try {
			//1,2디비연결 메서드호출
			con=getConnection();
			//3 sql count(*)
			sql="select count(*) from board";
			pstmt=con.prepareStatement(sql);
			//4 rs <= 실행
			rs=pstmt.executeQuery();
			//5 rs 첫행이동 데이터 있으면  count<=저장
			if(rs.next()) {
				count=rs.getInt("count(*)");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) try {rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {}
		}
		return count;
	}
	
	//getBoardCount()
		public int getBoardCount(String search) {
			Connection con=null;
			PreparedStatement pstmt=null;
			String sql="";
			ResultSet rs=null;
			int count=0;
			try {
				//1,2디비연결 메서드호출
				con=getConnection();
				//3 sql count(*)
				//sql="select count(*) from board where subject like '%검색어%'";
				sql="select count(*) from board where subject like ?";
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, "%"+search+"%");
				//4 rs <= 실행
				rs=pstmt.executeQuery();
				//5 rs 첫행이동 데이터 있으면  count<=저장
				if(rs.next()) {
					count=rs.getInt("count(*)");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				if(rs!=null) try {rs.close();}catch(SQLException ex) {}
				if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
				if(con!=null) try {con.close();}catch(SQLException ex) {}
			}
			return count;
		}
	
	//getBoardList()
	public List getBoardList(int startRow,int pageSize) {
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		ResultSet rs=null;
		List boardList=new ArrayList();
		try {
			//1,2 디비연결 메서드호출
			con=getConnection();
			//3 sql 객체생성
			sql="select * from board order by num desc limit ?,?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, startRow-1);
			pstmt.setInt(2, pageSize);
			//4 rs=실행 저장
			rs=pstmt.executeQuery();
			//5 rs => 한개글 BoardBean => boardList 한칸 저장
			while(rs.next()) {
				// 한 개 글 BoardBean 저장
				BoardBean bb=new BoardBean();
				bb.setNum(rs.getInt("num"));
				bb.setPass(rs.getString("pass"));
				bb.setName(rs.getString("name"));
				bb.setSubject(rs.getString("subject"));
				bb.setContent(rs.getString("content"));
				bb.setDate(rs.getDate("date"));
				bb.setReadcount(rs.getInt("readcount"));
				// boardList 한칸 저장
				boardList.add(bb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) try {rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {}
		}
		return boardList;
	}
	
	public List getBoardList(int startRow,int pageSize,String search) {
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		ResultSet rs=null;
		List boardList=new ArrayList();
		try {
			//1,2 디비연결 메서드호출
			con=getConnection();
			//3 sql 객체생성
			sql="select * from board where subject like ? order by num desc limit ?,?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, "%"+search+"%");
			pstmt.setInt(2, startRow-1);
			pstmt.setInt(3, pageSize);
			//4 rs=실행 저장
			rs=pstmt.executeQuery();
			//5 rs => 한개글 BoardBean => boardList 한칸 저장
			while(rs.next()) {
				// 한 개 글 BoardBean 저장
				BoardBean bb=new BoardBean();
				bb.setNum(rs.getInt("num"));
				bb.setPass(rs.getString("pass"));
				bb.setName(rs.getString("name"));
				bb.setSubject(rs.getString("subject"));
				bb.setContent(rs.getString("content"));
				bb.setDate(rs.getDate("date"));
				bb.setReadcount(rs.getInt("readcount"));
				// boardList 한칸 저장
				boardList.add(bb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) try {rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {}
		}
		return boardList;
	}
	
	//getBoard(num)
	public BoardBean getBoard(int num) {
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		ResultSet rs=null;
		BoardBean bb=null;
		try {
			//1,2 디비연결 메서드 호출
			con=getConnection();
			//3 sql 조건 num에 해당하는 게시판 글 가져오기
			sql="select * from board where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			//4 rs 실행 저장
			rs=pstmt.executeQuery();
			//5 rs 첫행 이동 했을때 저장된 내용을  
			// BoradBean bb객체생성 멤버변수 <= rs가져온 내용 저장  
			if(rs.next()) {
				bb=new BoardBean();
				bb.setNum(rs.getInt("num"));
				bb.setPass(rs.getString("pass"));
				bb.setName(rs.getString("name"));
				bb.setSubject(rs.getString("subject"));
				bb.setContent(rs.getString("content"));
				bb.setDate(rs.getDate("date"));
				bb.setReadcount(rs.getInt("readcount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) try {rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {}
		}
		return bb;
	}
	
	//updateReadCount(num)
	public void updateReadCount(int num) {
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		ResultSet rs=null;
		try {
			//1,2단계 디비연결
			con=getConnection();
			//3단계 sql 만들고 객체생성 
			// update readcount=readcount+1 조건 num에 해당하는
			sql="update board set readcount=readcount+1 where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			//4단계 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) try {rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {}
		}
	}
	
	//numCheck(bb)
	public int numCheck(BoardBean bb) {
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		ResultSet rs=null;
		int check=-1;
		try {
			//1,2 디비연결
			con=getConnection();
			//3 sql 객체생성   select pass가져오기 조건 num에 해당하는
			sql="select pass from board where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, bb.getNum());
			//4 rs <= 실행 저장
			rs=pstmt.executeQuery();
			//5 rs 첫행 이동 데이터 있으면
			//      폼비밀번호 디비 비밀번호 비교 일치하면  check=1
			//                           틀리면   check=0
			//  데이터 없으면  check=-1
			if(rs.next()) {
				if(bb.getPass().equals(rs.getString("pass"))) {
					check=1;
				}else {
					check=0;
				}
			}else {
				check=-1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) try {rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {}
		}
		return check;
	}
	
	//updateBoard(bb)
	public void updateBoard(BoardBean bb) {
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		ResultSet rs=null;
		try {
			//1,2 디비연결 메서드 호출
			con=getConnection();
			//3 sql update
			sql="update board set name=?,subject=?,content=? where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, bb.getName());
			pstmt.setString(2, bb.getSubject());
			pstmt.setString(3, bb.getContent());
			pstmt.setInt(4, bb.getNum());
			//4 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) try {rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {}
		}
	}
	
	//deleteBoard(bb)
	public void deleteBoard(BoardBean bb) {
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		ResultSet rs=null;
		try {
			//1,2 디비연결 메서드 호출
			con=getConnection();
			//3 sql delete
			sql="delete from board where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, bb.getNum());
			//4 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) try {rs.close();}catch(SQLException ex) {}
			if(pstmt!=null) try {pstmt.close();}catch(SQLException ex) {}
			if(con!=null) try {con.close();}catch(SQLException ex) {}
		}
	}
	
	
}//클래스
