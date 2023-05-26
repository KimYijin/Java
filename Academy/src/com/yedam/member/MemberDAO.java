package com.yedam.member;

import com.yedam.common.DAO;

public class MemberDAO extends DAO {
	private static MemberDAO memDao = null;
	
	private MemberDAO(){
		
	}
	public static MemberDAO getInstance() {
		if(memDao == null) {
			memDao = new MemberDAO();
		}
		return memDao;
	}
	
	//로그인
	
	public Member login (String id) {
		Member member = null;
		try {
			conn();
			String sql = "SELECT * FROM member WHERE member_id =? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				member = new Member();
				member.setMemberId(id);
				member.setMemberPw(rs.getString("member_pw"));
				member.setMemberName(rs.getString("member_name"));
				member.setMemberAuth(rs.getString("member_auth"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return member;
	}
	
	//회원 가입 - 기본 정보
	public int insertMember(Member member) {
		int result = 0;
		try {
			conn();
			String sql = "INSERT INTO member VALUES(?, ?, ?, 'S')";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getMemberPw());
			pstmt.setString(3, member.getMemberName());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return result;
	}
	
	//내 정보 조회 - 수강 정보 조회
	public Member getCourseInfo (String id) {
		Member member = null;
		try {
			conn();
			String sql = "SELECT * FROM courseinfo WHERE member_id =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				member = new Member();
				member.setMemberId(id);
				member.setLevelId(rs.getInt("level_id"));
				member.setStartDate(rs.getDate("start_date"));
				member.setDuration(rs.getInt("duration"));
				member.setEndDate(rs.getString("end_date"));
				member.setTestApply(rs.getString("test_apply"));
				member.setTestApprove(rs.getString("test_approve"));
				member.setTestResult(rs.getString("test_result"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return member;
	}
	//비밀번호 수정
	public int updatePw(Member member) {
		int result = 0;
		try {
			conn();
			String sql = "UPDATE member SET member_pw = ? WHERE member_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberPw());
			pstmt.setString(2, member.getMemberId());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return result;
	}
	

	//수강 등록 유무 확인
	public Member checkCourse(String id) {
		Member member = null;
		try {
			conn();
			String sql = "SELECT * FROM courseinfo WHERE member_id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, MemberService.memberInfo.getMemberId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				member = new Member();
				member.setMemberId(id);
				member.setLevelId(rs.getInt("level_id"));
				member.setStartDate(rs.getDate("start_date"));
				member.setDuration(rs.getInt("duration"));
				member.setEndDate(rs.getString("end_date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return member;
	}
	
	//수강 신청
	public int insertCourse( int selectLevel, int selectDuration) {
		int result = 0;
		try {
			conn();
			String sql = "INSERT INTO courseinfo VALUES(?, ?, sysdate, ?, to_char(add_months(sysdate, ?),'YYYY-MM-DD'),null, null, null)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, MemberService.memberInfo.getMemberId());
            pstmt.setInt(2, selectLevel);
            pstmt.setInt(3, selectDuration);
            pstmt.setInt(4, selectDuration);
            
            result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return result;
	}
	
	//재 수강 신청
	public int updateCourse( int selectLevel, int selectDuration) {
		int result = 0;
		try {
			conn();
			String sql = "UPDATE courseinfo "
					+ "SET level_id = ?, start_date = sysdate, duration = ?, end_date = to_char(add_months(sysdate, ?),'YYYY-MM-DD'),"
					+ "test_apply = null, test_approve = null, test_result = null\r\n"
					+ "WHERE member_id = ?";
			
			pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, selectLevel);
            pstmt.setInt(2, selectDuration);
            pstmt.setInt(3, selectDuration);
            pstmt.setString(4, MemberService.memberInfo.getMemberId());
            
            result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return result;
	}
	
	//레벨 테스트 신청
	public int testApply (String id) {
		int result = 0;
		try {
			conn();
			String sql = "UPDATE courseinfo SET test_apply = ? WHERE member_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "O");
			pstmt.setString(2, MemberService.memberInfo.getMemberId());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return result;
	}
	
	
}
