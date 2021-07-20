package com.wannabe.be.product.service;

import java.util.ArrayList;
import java.util.List;

import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductVO;
import com.wannabe.be.shopcart.vo.ShopcartVO;

public interface ProductService {


	void insertproduct(ProductVO productVO);

	List<ProductVO> getlist();

	ProductVO productDetail(int product_no);

	void modifyGoodsImage(List<ProductAttachVO> imageFileList);

	void removeGoodsImage(int product_no);

	int addNewGoodsImage(ProductAttachVO imageFileList);

	List<ProductVO> getAvailableProductNo();

	List<com.wannabe.be.product.vo.ProductCategoryVO> getAvailableCategories();

	List<ProductVO> getLatestProducts();

	List<ProductAttachVO> getProductImageByProductNo(int product_no);

	ProductVO getProductDetailsByProduct_no(int product_no);

	void addItemToCart(ShopcartVO shopcartVO);

	List<ProductVO> getProductListByRating();

	List<ProductVO> getProductListWithPageInfo(ProductVO product);



}
