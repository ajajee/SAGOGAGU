package com.wannabe.be.address.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wannabe.be.address.dao.AddressDao;
import com.wannabe.be.address.vo.AddressVO;

@Service("addressService")
@Transactional(propagation = Propagation.REQUIRED)
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressDao addressDao;

	@Override
	public void insertaddress(AddressVO addressVO) throws Exception{
		addressDao.insertaddress(addressVO);
		
	}


	@Override
	public List<AddressVO> list(String member_id) throws Exception {
		
		return addressDao.list(member_id);
	}

	@Override
	public AddressVO selectOneAddress(Map addr_map) throws Exception {
		
		return addressDao.selectOneAddress(addr_map);
	}


	@Override
	public int modifyaddress(AddressVO addressVO) throws Exception {
		System.out.println("service >>>> addressVO >>>> " + addressVO);
		return addressDao.modifyaddress(addressVO);
	}


	@Override
	public int deleteaddress(int address_id) throws Exception {
		return addressDao.deleteaddress(address_id);
	}




	
	

}

