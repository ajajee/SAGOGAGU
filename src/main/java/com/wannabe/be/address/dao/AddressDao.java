package com.wannabe.be.address.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wannabe.be.address.vo.AddressVO;

@Repository
@Mapper
public interface AddressDao {
	
	public void insertaddress(AddressVO addressVO) throws Exception ;
	public List <AddressVO> list(String member_id) throws Exception;
	
	public int deleteaddress(int address_id) throws Exception;
	public AddressVO selectOneAddress(Map addr_map) throws Exception ;
	public int modifyaddress(AddressVO addressVO) throws Exception;

}
