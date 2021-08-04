package com.wannabe.be.member.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.wannabe.be.member.vo.MemberVO;

@Repository
@Mapper
public interface MemberDao {
	
	public void insertNewMember(MemberVO memberVO);

	public MemberVO login(Map<String, String> loginMap);

	public int modifymember(MemberVO memberVO);

	public MemberVO selectOnemember(String member_id);

	public int deletemember(String member_id);

	public MemberVO addMemberCheck(String memberID);

	public int modifyPw(MemberVO memberVO);
	
	@Select("SELECT * FROM MEMBER WHERE MEMBER_ID = #{member_id}")
	public MemberVO getMemberInfo(@Param("member_id") String member_id);


}
