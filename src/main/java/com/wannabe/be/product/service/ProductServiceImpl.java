package com.wannabe.be.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wannabe.be.product.dao.ProductAttachMapper;
import com.wannabe.be.product.dao.ProductMapper;
import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductVO;

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

		if (productVO.getAttachList() == null || productVO.getAttachList().size() <= 0) {
			return;
		}

		productVO.getAttachList().forEach(attach -> {
			System.out.println("hi");
			attach.setProduct_no(productVO.getProduct_no());
			productAttachMapper.insert(attach);
		});
	}

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
	public List<ProductVO> getSearchList(ProductVO pd) {
		return productMapper.getSearchList(pd);
	}

	

}
