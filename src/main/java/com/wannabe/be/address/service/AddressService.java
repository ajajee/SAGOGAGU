package com.wannabe.be.address.service;

import java.util.List;
import java.util.Map;

import com.wannabe.be.address.vo.AddressVO;

public interface AddressService {


	void insertaddress(AddressVO addressVO) throws Exception;

	public List <AddressVO> list(String member_id) throws Exception;

	int deleteaddress(int address_id)throws Exception;

	int modifyaddress(AddressVO addressVO) throws Exception;

	AddressVO selectOneAddress(Map addr_map) throws Exception;
	
	

}
