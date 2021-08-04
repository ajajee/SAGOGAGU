package com.wannabe.be.member.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wannabe.be.member.dao.MemberDao;
import com.wannabe.be.member.vo.MemberVO;

@Service("memberService")
@Transactional(propagation = Propagation.REQUIRED)
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberDao memberDao;

	@Override
	public void insertmember(MemberVO memberVO) {
		memberDao.insertNewMember(memberVO);
	}

	@Override
	public MemberVO login(Map<String, String> loginMap) {
		return memberDao.login(loginMap);
	}


	@Override
	public int modifymember(MemberVO memberVO) {
		System.out.println("service >>>> memberVO >>" + memberVO);
		return memberDao.modifymember(memberVO);
	}

	@Override
	public MemberVO selectOnemember(String member_id) {
		return memberDao.selectOnemember(member_id);
	}

	@Override
	public int deletemember(String member_id) {
		
		return memberDao.deletemember(member_id);
	}

	@Override
	public int addMemberCheck(String memberID)  {
		MemberVO member = memberDao.addMemberCheck(memberID);
		int result;
		if(member!=null) {
			result=1; // 존재하는 아이디
		}else {
			result=0; // 가입 가능한 아이디
		}
		return result;
	}

	@Override
	public int modifyPw(MemberVO memberVO) {
		return memberDao.modifyPw(memberVO);
	}

	@Override
	public MemberVO getMemberInfo(String member_id) {
		return memberDao.getMemberInfo(member_id);
	}


	






	
	
	

}
