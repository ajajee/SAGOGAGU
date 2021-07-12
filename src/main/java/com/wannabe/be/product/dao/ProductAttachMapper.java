package com.wannabe.be.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wannabe.be.product.vo.ProductAttachVO;

@Repository
@Mapper
public interface ProductAttachMapper {

	public void insert(ProductAttachVO vo);

	public void delete(String uuid);

	public List<ProductAttachVO> findByBno(Long bno);

	public void deleteAll(Long bno);

	public List<ProductAttachVO> getOldFiles();

	public List<ProductAttachVO> getlist();

	public List<ProductAttachVO> getProductImages(int product_no);

	public void updateGoodsImage(ProductAttachVO imageFileList);

	public void deleteGoodsImage(int product_no);

	public void addNewGoodsImage(ProductAttachVO imageFileList);

	public int selectKey();
	

}