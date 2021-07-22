package com.wannabe.be.product.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.wannabe.be.common.CommonUtil;
import com.wannabe.be.member.vo.MemberVO;
import com.wannabe.be.product.service.ProductService;
import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductVO;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Slf4j
public class ProductControllerImpl extends CommonUtil implements ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductVO productVO;

	@Autowired
	private MemberVO memberVO;

	@GetMapping("/product/productform")
	public String productform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "product/productform";
	}

	@GetMapping("/product/list")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(productService.getlist());
		model.addAttribute("productList", productService.getlist());

		return "product/list";
	}

	@GetMapping("/product/get")
	public String get(Model model, @RequestParam("product_no") int product_no, HttpServletRequest request, HttpServletResponse response) throws Exception{
		model.addAttribute("product", productService.productDetail(product_no));
		return "product/get";
	}
	

	@GetMapping("/product/modifyProduct")
	public String modifyProduct(Model model, @RequestParam("product_no") int product_no, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		model.addAttribute("product", productService.productDetail(product_no));
		return "product/modifyProduct";
	}

	
	
	@PostMapping("/product/insertproduct")
	public String insertproduct(MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response) throws Exception {
		
		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		String imageFileName=null;
		
		
		Map newGoodsMap = new HashMap();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			newGoodsMap.put(name,value);
		}
		System.out.println("newGoodsMap>>>>>> " + newGoodsMap);

		//upload 메소드가 하는 일: 업로드된 image들 각각의 이름과 타입을 추출하여 ImageFileVO 객체를 생성하고 
		//이미지들을 임시 디렉터리에 저장하고 객체들을 담은 리스트를 반환 
		List<ProductAttachVO> imageFileList =upload(multipartRequest);
//		if(imageFileList!= null && imageFileList.size()!=0) {
//			for(ProductAttachVO productAttachVO : imageFileList) {
//				productAttachVO.setProduct_no(product_no);
//			}
//			newGoodsMap.put("imageFileList", imageFileList);
//		}
		System.out.println("imageFileList>>>>>> " + imageFileList);
		
//		String message = null;
//		ResponseEntity resEntity = null;
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
//		try {
//			int goods_id = adminGoodsService.addNewGoods(newGoodsMap);
//			if(imageFileList!=null && imageFileList.size()!=0) {
//				for(ImageFileVO  imageFileVO:imageFileList) {
//					imageFileName = imageFileVO.getFileName();
//					File srcFile = new File(CURR_IMAGE_REPO_PATH+"\\"+"temp"+"\\"+imageFileName);
//					File destDir = new File(CURR_IMAGE_REPO_PATH+"\\"+goods_id);
//					FileUtils.moveFileToDirectory(srcFile, destDir,true);
//				}
//			}
//			message= "<script>";
//			message += " alert('등록 성공');";
//			message +=" location.href='"+ multipartRequest.getContextPath()+"/main/main.do';";
//			message +=("</script>");
//		}catch(Exception e) {
//			if(imageFileList!=null && imageFileList.size()!=0) {
//				for(ImageFileVO  imageFileVO:imageFileList) {
//					imageFileName = imageFileVO.getFileName();
//					File srcFile = new File(CURR_IMAGE_REPO_PATH+"\\"+"temp"+"\\"+imageFileName);
//					srcFile.delete();
//				}
//			}
//			
//			message= "<script>";
//			message += " alert('등록 실패. 관리자에게 문의, 내가 관리자야, 나한테 문의?');";
//			message +=" location.href='"+multipartRequest.getContextPath()+"/admin/goods/addNewGoodsForm.do';";
//			message +=("</script>");
//			e.printStackTrace();
//			resEntity =new ResponseEntity(message, responseHeaders, HttpStatus.OK);
//			return resEntity;
//		}
//		resEntity =new ResponseEntity(message, responseHeaders, HttpStatus.OK);
//		return resEntity;
//	}
		


		return "redirect:/";
	}
	
	@PostMapping("/product/modifyGoodsImageInfo")
	public void modifyGoodsImageInfo(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)  throws Exception {
		System.out.println("modifyGoodsImageInfo");
		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String imageFileName=null;
		
		Map goodsMap = new HashMap();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			goodsMap.put(name,value);
		}
		
//		HttpSession session = multipartRequest.getSession();
//		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
//		String reg_id = memberVO.getMember_id();
		
		List<ProductAttachVO> imageFileList=null;
		int goods_id=0;
		int image_id=0;
		try {
			imageFileList =upload(multipartRequest);
			if(imageFileList!= null && imageFileList.size()!=0) {
				for(ProductAttachVO imageFileVO : imageFileList) {
					goods_id = Integer.parseInt((String)goodsMap.get("goods_id"));
					image_id = Integer.parseInt((String)goodsMap.get("image_id"));
					imageFileVO.setProduct_no(goods_id);
					imageFileVO.setProduct_img_no(image_id);
				}
				
				productService.modifyGoodsImage(imageFileList);

//				for(ProductAttachVO  imageFileVO:imageFileList) {
//					imageFileName = imageFileVO.getFileName();
//					File srcFile = new File(CURR_IMAGE_REPO_PATH+"\\"+"temp"+"\\"+imageFileName);
//					File destDir = new File(CURR_IMAGE_REPO_PATH+"\\"+goods_id);
//					FileUtils.moveFileToDirectory(srcFile, destDir,true);
//				}
			    
			}
		}catch(Exception e) {
			if(imageFileList!=null && imageFileList.size()!=0) {
				for(ProductAttachVO  imageFileVO:imageFileList) {
					imageFileName = imageFileVO.getFilename();
//					File srcFile = new File(CURR_IMAGE_REPO_PATH+"\\"+"temp"+"\\"+imageFileName);
//					srcFile.delete();
				}
			}
			e.printStackTrace();
		}
		
	}
	
	@PostMapping("/product/removeGoodsImage")
	public void  removeGoodsImage(@RequestParam("img_no") int img_no,
			                      HttpServletRequest request, HttpServletResponse response)  throws Exception {
	
		// db에서 해당 번호와 같은 이미지 삭제 
		System.out.println(img_no);
		productService.removeGoodsImage(img_no);
	}
	
	@PostMapping("/product/search")
	public @ResponseBody List<ProductVO> searchProduct(@RequestBody ProductVO pd, Model model){
		log.info("prod >>> cont >>> " + pd);
		List<ProductVO> pdList = productService.getSearchList(pd);
		log.info("prod >>> cont >>> " + pdList);		
		return pdList;
	}
	

	
}
