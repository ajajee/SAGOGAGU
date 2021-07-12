package com.wannabe.be.product.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wannabe.be.domain.Criteria;
import com.wannabe.be.product.vo.ProductVO;

@Repository
@Mapper
public interface ProductMapper {
	
	public void insertNewProduct(ProductVO productVO);

	public List<ProductVO> getlist();

	public ProductVO getProduct(int product_no);



	
}
