package com.wannabe.be.shopcart.service;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wannabe.be.shopcart.vo.ShopcartVO;

@Mapper
public interface ShopcartService {
	
	public List <ShopcartVO> list(String member_id) throws Exception;

	public int deletecart(ShopcartVO shopcartVO);

	public int modifycart(ShopcartVO shopcartVO);

	@Insert("Insert into cart (member_id, product_no, cart_product_quantity) values(#{shopcartVO.member_id}, #{shopcartVO.product_no}, #{shopcartVO.art_product_quantity})")
	public void addItemToCart(@Param("shopcartVO") ShopcartVO shopcartVO);

}
