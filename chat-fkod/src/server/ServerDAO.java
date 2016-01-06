package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import global.Constants;
import global.DatabaseFactory;
import global.Vendor;
import member.MemberVO;
public class ServerDAO {
	
	private Connection con; // Connection DB와 연결
	private PreparedStatement pstmt; // setter의 느낌
	private ResultSet rs; // ResultSet return 받아서 DB로 던짐

	public ServerDAO() {
		con = DatabaseFactory.getDatabase(Vendor.ORACLE, Constants.ORACLE_ID, Constants.ORACLE_PASSWORD)
				.getConnection();
	}
	
	public int confirmSignUp(String name, String phone, String password, String email) {
		int result = 0;
		try {
			pstmt = con.prepareStatement("insert into member(name, password, phone, email) values (?,?,?,?)");
			pstmt.setString(1, name);
			pstmt.setString(2, password);
			pstmt.setString(3, phone);
			pstmt.setString(4, email);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<MemberVO> confirmLogin(String phone, String password) {
		List<MemberVO> vec = new Vector<MemberVO>();
		MemberVO temp = null;

		try {
			rs = con.createStatement()
					.executeQuery("select * from member where phone in ( select fphone from friend where uphone = "
							+ makeQuery(phone) + ")");
			while (rs.next()) { // 친구정보
				temp = new MemberVO();
				temp.setEmail(rs.getString("email"));
				temp.setName(rs.getString("name"));
				temp.setPassword(rs.getString("password"));
				temp.setPhone(rs.getString("phone"));
				vec.add(temp); // 친구정보 저장
			}
			rs = con.createStatement().executeQuery("select * from member where phone = " + makeQuery(phone)
					+ " and password = " + makeQuery(password));
			if (rs.next()) { // 마지막은 내정보
				temp = new MemberVO();
				temp.setEmail(rs.getString("email"));
				temp.setName(rs.getString("name"));
				temp.setPassword(rs.getString("password"));
				temp.setPhone(rs.getString("phone"));
				vec.add(temp); // 내정보 저장
			} else {
				vec.clear();
				return vec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vec;
	}

	public String makeQuery(String str) {
		return "'" + str + "'";
	}


	public MemberVO searchFriend(String phone) { //찾은 친구 반환
		MemberVO temp = null;
		try {
			rs = con.createStatement().executeQuery("select * from member where phone = " + makeQuery(phone));
			if (rs.next()) {
				temp = new MemberVO();
				temp.setEmail(rs.getString("email"));
				temp.setName(rs.getString("name"));
				temp.setPassword(rs.getString("password"));
				temp.setPhone(rs.getString("phone"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	public int addFriend(String myPhone, String targetPhone) {
		int result = 0;
		try {
			pstmt = con.prepareStatement("insert into friend(f_seq, uphone, fphone) values(f_seq.nextval,?,?)");
			pstmt.setString(1, myPhone);
			pstmt.setString(2, targetPhone);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public int deleteFriend(String myPhone, String fPhone) {
		int temp =0;
		try {
			pstmt = con.prepareStatement("delete from friend where uphone = " 
					+ makeQuery(myPhone) + " and fphone = " + makeQuery(fPhone));
			temp = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
}
