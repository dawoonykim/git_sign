package test1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.ORADataFactory;

@WebServlet("/test")
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	MemberDAO memDAO = new MemberDAO();

	public void init(ServletConfig config) throws ServletException {
		System.out.println("초기화");
	}

	public void destroy() {
		System.out.println("종료");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
//		System.out.println("doHandle 메소드 실행");
		PrintWriter pw = response.getWriter();
//		pw.write("hi");
		String command = request.getParameter("command");
		System.out.println("커맨드: " + command);
		
		MemberDAO dao = new MemberDAO();
		List<MemberVO> list = dao.listMembers();
		if (command == null) {
		

			pw.write("<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset=\"UTF-8\">"
					+ "<title>Insert title here</title>" + "<style>table,tr,th,td {" + "border: solid 2px black;"
					+ "border-collapse: collapse;" + " padding: 8px;}</style>" + "</head>"
					+ "<table><tr><th>아이디</th><th>비밀번호</th>" + "<th>이름</th><th>이메일</th><th>가입일</th></tr>");
			int i = 0;
			while (i < list.size()) {
				String id = list.get(i).getId();
				String pwd = list.get(i).getPwd();
				String name = list.get(i).getName();
				String email = list.get(i).getEmail();
				Date joinDate = list.get(i).getJoinDate();
				pw.write("<tr><td>\r\n" + id + "</td><td>" + pwd + "</td><td>" + name + "</td><td>" + email + "</td><td>"
						+ joinDate + "</td>" + "<td><a href='http://localhost:8090/pro07_1/test?command=delMember&id=" + id
						+ "'>삭제</a></td>" + "</tr>");
				i++;
			}
		}
		

		pw.write("</table>" + "<body>" + "<tr></tr>" + "<a href='http://localhost:8090/pro07_1/NewFile.html'>새 회원 등록하기"
				+ "</body>" + "</html>");
		if (command != null && command.equals("addMember")) {
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			System.out.println("가져온 가입 정보 : " + id + ", " + pwd + ", " + name + ", " + email);

			MemberVO vo = new MemberVO();
			vo.setId(id);
			vo.setPwd(pwd);
			vo.setName(name);
			vo.setEmail(email);

			dao.addMember(vo);
		} else if (command != null && command.equals("delMember")) {
			String id = request.getParameter("id");
			dao.delMember(id);
		}

//		memDAO.listMembers();

		pw.close();

	}

}
