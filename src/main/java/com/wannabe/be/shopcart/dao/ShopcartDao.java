package com.wannabe.be.shopcart.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wannabe.be.shopcart.vo.ShopcartVO;

@Repository
@Mapper
public interface ShopcartDao {
	
	public List <ShopcartVO> list(String member_id) throws Exception;

	public int deletecart(ShopcartVO shopcartVO);

	public int modifycart(ShopcartVO shopcartVO);
	
	

}
