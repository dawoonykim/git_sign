package test1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.jasper.tagplugins.jstl.core.Catch;

import oracle.jdbc.internal.ObjectDataFactory;
import oracle.sql.ARRAY;

public class MemberDAO {

	private DataSource dataFactory;
//	private PreparedStatement pstmt;
//	private Connection conn;
	// MemberDAO 객체 초기화(생성자)시에 위 정보를 불러오게 해라 - JNDI

	public MemberDAO() {
		try {
			System.out.println("MemberDAO 객체 생성");
			Context ctx = new InitialContext();
			Context enContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) enContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			System.out.println("DB 연결을 위한 MemberDAO 객체 생성시 에러");
		}
	}

	// 회원 목록 가져옴 (그 전에 연결)
	public List<MemberVO> listMembers() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		try {
			Connection conn = dataFactory.getConnection();
			String sql = "select * from T_MEMBER";
//			ResultSet rs=pstmt.executeQuery();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery(sql);

			while (rs.next()) {
				String id = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				System.out.println(id + " " + pwd + " " + name + " " + email + " " + joinDate);

				MemberVO vo = new MemberVO();
				vo.setId(id);
				vo.setPwd(pwd);
				vo.setName(name);
				vo.setEmail(email);
				vo.setJoinDate(joinDate);

				list.add(vo);
			}

			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("SQL 실행시 에러");
		}
		return list;
	}

	public void addMember(MemberVO memberVO) {
		try {
			Connection conn1 = dataFactory.getConnection();
			String id = memberVO.getId();
			String pwd = memberVO.getPwd();
			String name = memberVO.getName();
			String email = memberVO.getEmail();

			String query = "insert into t_member(id,pwd,name,email) VALUES(?,?,?,?)";
			System.out.println("회원 추가 sql문: " + query);

			PreparedStatement pstmt1 = conn1.prepareStatement(query);

			pstmt1.setString(1, id);
			pstmt1.setString(2, pwd);
			pstmt1.setString(3, name);
			pstmt1.setString(4, email);
			System.out.println(id + pwd + name + email);

//			int num = pstmt.executeUpdate();
//			System.out.println(num + "명이 추가됨");

			pstmt1.executeUpdate();
			pstmt1.close();
		} catch (Exception e) {
			System.out.println("회원 추가시 에러");
		}
	}

	public void delMember(String id) {
		System.out.println("삭제하고자 하는 id: " + id);
		try {
			Connection conn2 = dataFactory.getConnection();
			String query = "delete from t_member where id=?";
			System.out.println("preparedStatement: " + query);

			PreparedStatement pstmt2 = conn2.prepareStatement(query);
			pstmt2.setString(1, id);
			
			pstmt2.executeUpdate();
			pstmt2.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
