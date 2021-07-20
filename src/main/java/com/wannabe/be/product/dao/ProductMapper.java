package com.wannabe.be.product.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductCategoryVO;
import com.wannabe.be.product.vo.ProductVO;

@Repository
@Mapper
public interface ProductMapper {

	public void insertNewProduct(ProductVO productVO);

	public List<ProductVO> getlist();

	public ProductVO getProduct(int product_no);

	public List<ProductVO> getAvailableProductNo();

	public List<ProductCategoryVO> getAvailableCategories();

	public List<ProductVO> getLatestProducts();

	public ProductVO getProductDetailsByProduct_no(int product_no);

	public List<ProductVO> getProductListByRating();

	public List<ProductVO> getProductListWithPageInfo(ProductVO product);

}
