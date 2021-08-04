package com.wannabe.be.product.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wannabe.be.product.dao.ProductAttachMapper;
import com.wannabe.be.product.dao.ProductMapper;
import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductCategoryVO;
import com.wannabe.be.product.vo.ProductVO;
import com.wannabe.be.product.vo.PurchaseHistoryVO;
import com.wannabe.be.shopcart.vo.ShopcartVO;

@Service("productService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private ProductAttachMapper productAttachMapper;

	@Override
	public void insertproduct(ProductVO productVO) {
		productMapper.insertNewProduct(productVO);
		int product_no = productVO.getProduct_no();

		if (productVO.getAttachList() == null || productVO.getAttachList().size() <= 0) {
			return;
		} else {
			List<ProductAttachVO> attachList = productVO.getAttachList();
			for (ProductAttachVO attach : attachList) {
				attach.setProduct_no(product_no);
				productAttachMapper.insert(attach);
			}
		}
	}

	@Override
	public ProductVO getProductDetailsByProduct_no(int product_no) {
		return productMapper.getProductDetailsByProduct_no(product_no);
	}

	@Override
	public List<ProductVO> getAvailableProductNo() {
		return productMapper.getAvailableProductNo();
	}

	@Override
	public List<ProductCategoryVO> getAvailableCategories() {
		return productMapper.getAvailableCategories();
	}

	@Override
	public List<ProductVO> getLatestProducts() {
		return productMapper.getLatestProducts();
	}

	@Override
	public List<ProductAttachVO> getProductImageByProductNo(int product_no) {
		return productAttachMapper.getProductImageByProductNo(product_no);
	}

	@Override
	public List<ProductVO> getProductListByRating() {
		return productMapper.getProductListByRating();
	}

	@Override
	public List<ProductVO> getProductListWithPageInfo(ProductVO product) {
		return productMapper.getProductListWithPageInfo(product);
	}

	@Override
	public int getAvgRatingByProduct_no(int product_no) {
		return productMapper.getAvgRatingByProduct_no(product_no);
	}


	@Override
	public void updatePurchaseHistory(PurchaseHistoryVO history) {
		productMapper.updatePurchaseHistory(history);
		
	}
	
	@Override
	public List<PurchaseHistoryVO> getHistory(String member_id) {
		return productMapper.getHistory(member_id);
	}


	/*----------------------------------------------------------------------------------------*/

	@Override
	public List<ProductVO> getlist() {

		List<ProductVO> list_productVO = productMapper.getlist();
		List<ProductAttachVO> list_productAttachVO = productAttachMapper.getlist();

		for (ProductVO productVO : list_productVO) {
			for (ProductAttachVO productAttachVO : list_productAttachVO) {
				if (productVO.getProduct_no() == productAttachVO.getProduct_no()) {
					List<ProductAttachVO> temp_productAttachVO = new ArrayList<>();
					temp_productAttachVO.add(productAttachVO);
					productVO.setAttachList(temp_productAttachVO);
				}
			}
		}

		return list_productVO;

	}

	@Override
	public ProductVO productDetail(int product_no) {
		ProductVO productVO = productMapper.getProduct(product_no);
		productVO.setAttachList(productAttachMapper.getProductImages(product_no));
		return productVO;
	}

	@Override
	public void modifyGoodsImage(List<ProductAttachVO> imageFileList) {
		for (int i = 0; i < imageFileList.size(); i++) {
			ProductAttachVO imageFileVO = imageFileList.get(i);
			productAttachMapper.updateGoodsImage(imageFileVO);
		}
	}

	@Override
	public void removeGoodsImage(int product_no) {
		productAttachMapper.deleteGoodsImage(product_no);

	}

	@Override
	public int addNewGoodsImage(ProductAttachVO imageFileList) {
		int img_no = productAttachMapper.selectKey();
		System.out.println(img_no);
		productAttachMapper.addNewGoodsImage(imageFileList);
		return img_no;
	}

	@Override
	public void addItemToCart(ShopcartVO shopcartVO) {
		// TODO Auto-generated method stub
		
	}


	




}
