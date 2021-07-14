package com.wannabe.be.product.service;

import java.util.List;

import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductVO;

public interface ProductService {


	void insertproduct(ProductVO productVO);

	List<ProductVO> getlist();

	ProductVO productDetail(int product_no);

	void modifyGoodsImage(List<ProductAttachVO> imageFileList);

	void removeGoodsImage(int product_no);

	int addNewGoodsImage(ProductAttachVO imageFileList);

}
