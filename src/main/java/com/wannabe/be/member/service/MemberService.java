package com.wannabe.be.member.service;


import java.util.Map;

import com.wannabe.be.member.vo.MemberVO;

public interface MemberService {
	
	void insertmember(MemberVO memberVO) throws Exception;

	MemberVO login(Map<String, String> loginMap) throws Exception;

	int modifymember(MemberVO memberVO) throws Exception;

	MemberVO selectOnemember(String member_id);

	int deletemember(String member_id);

	int addMemberCheck(String memberID);

	int modifyPw(MemberVO memberVO);

	MemberVO getMemberInfo(String member_id);

}
