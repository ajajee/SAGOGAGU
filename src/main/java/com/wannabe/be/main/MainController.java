package com.wannabe.be.main;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.ognl.IteratorElementsAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wannabe.be.product.service.ProductService;
import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductVO;
import com.wannabe.be.reviewboard.service.ReviewBoardService;
import com.wannabe.be.reviewboard.vo.ReviewImageFileVO;
import com.wannabe.be.reviewboard.vo.ReviewVO;

@Controller
public class MainController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ReviewBoardService reviewBoardService;
	
	
	/* 메인 게시 상품 배열 처리 */ 
	@RequestMapping(value = { "/", "/main" })
	public String greeting(Model model) {
		
		/* 베스트, 신상품 리스트  처리 */ 
		List<ProductVO> latestProductList = productService.getLatestProducts();
		List<ProductVO> bestProductList = productService.getProductListByRating();
		List<ProductAttachVO> ProductImageList = new ArrayList<>();
		List<ProductAttachVO> bestImageList = new ArrayList<>();
		List<ProductAttachVO> tempList = new ArrayList<>();
		NumberFormat numberFormat = NumberFormat.getInstance();

		for (int i = 0; i <latestProductList.size(); i++) {
			ProductVO product = latestProductList.get(i);
			int product_no = product.getProduct_no();
			tempList.addAll(productService.getProductImageByProductNo(product_no));	
			double price = product.getProduct_price();
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			decimalFormat.setGroupingUsed(true);
	        decimalFormat.setGroupingSize(3);
	        decimalFormat.format(price);
	        product.setFormattedPrice(decimalFormat.format(price));
		}
		
		/* null값있는 리스트 항목 제거  */ 
		for (int j = 0; j < tempList.size(); j++) {
			if (tempList.get(j).getFilename().isEmpty()) {
				tempList.remove(j);
			} else {
				ProductImageList.add(tempList.get(j));		
			}
		}
		
		for(int k = 0; k < bestProductList.size(); k++) {
			int product_no = bestProductList.get(k).getProduct_no();
			bestImageList.addAll(productService.getProductImageByProductNo(product_no));
		}
		
		
		model.addAttribute("latestProductList", latestProductList);
		model.addAttribute("ProductImageList", ProductImageList);
		model.addAttribute("bestProductList", bestProductList);
		model.addAttribute("bestImageList", bestImageList);
		

		return "index";
	}
}
