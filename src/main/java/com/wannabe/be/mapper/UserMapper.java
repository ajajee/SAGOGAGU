package com.wannabe.be.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wannabe.be.address.vo.AddressVO;
import com.wannabe.be.member.vo.MemberVO;

@Mapper
public interface UserMapper {
	
	List<com.wannabe.be.member.vo.MemberVO> userList();
	MemberVO fetchUserByID(String member_id);
	void updateUser(MemberVO user);
	void insertUser(MemberVO user);
	void deleteUser(int id);
	void loginUser(MemberVO user);
	
	List<com.wannabe.be.address.vo.AddressVO> AddrList();
	AddressVO fetchaddrByID(String member_id);
	void update_addr(AddressVO user);
	void insert_addr(AddressVO user);
	void delete_addr(int id);

}
