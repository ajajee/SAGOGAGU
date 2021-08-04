
package com.wannabe.be.product.controller;

import java.awt.PageAttributes.MediaType;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonValue;
import com.wannabe.be.address.service.AddressService;
import com.wannabe.be.address.vo.AddressVO;
import com.wannabe.be.member.service.MemberService;
import com.wannabe.be.member.vo.MemberVO;
import com.wannabe.be.product.service.ProductService;
import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductCategoryVO;
import com.wannabe.be.product.vo.ProductVO;
import com.wannabe.be.product.vo.PurchaseHistoryVO;
import com.wannabe.be.reviewboard.service.ReviewBoardService;
import com.wannabe.be.reviewboard.vo.ReviewImageFileVO;
import com.wannabe.be.reviewboard.vo.ReviewVO;
import com.wannabe.be.shopcart.service.ShopcartService;
import com.wannabe.be.shopcart.vo.ShopcartVO;
import com.wannabe.be.utills.Criteria;
import com.wannabe.be.utills.PaginationVO;

import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ReviewBoardService reviewBoardService;
	
	@Autowired
	private ShopcartService shopcartService; 
	
	@Autowired
	private MemberService memberService; 
	
	@Autowired
	private AddressService adressService; 
	
	@Autowired
	private ProductVO productVO;

	@Autowired
	private MemberVO memberVO;
	

	/* 제품 추가 양식 */
	@GetMapping("/product/productform")
	public String productform(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		List<ProductVO> productList = productService.getAvailableProductNo();
		List<ProductCategoryVO> categoryList = productService.getAvailableCategories();

		model.addAttribute("productList", productList);
		model.addAttribute("categoryList", categoryList);
		return "/product/productform";
	}

	/* 제품 추가 */
	@PostMapping(value = "/product/insertproduct")
	public String insertproduct(ProductVO _productVO, MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response, Model model,
			@RequestParam(value = "list", required = false) List<ProductAttachVO> list) throws Exception {

		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		String imageFileName = null;

		Map newProductMap = new HashMap();
		Enumeration enu = multipartRequest.getParameterNames();

		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			newProductMap.put(name, value);
		}

		List<ProductAttachVO> imageFileList = upload(multipartRequest);
		_productVO.setAttachList(imageFileList);
		productService.insertproduct(_productVO);

		int product_no = _productVO.getProduct_no();
		for (ProductAttachVO imageFile : imageFileList) {
			imageFile.setProduct_no(product_no);
		}

		return "redirect:/";
	}

	/* 제품 이미지 파일 업로드 */
	public List<ProductAttachVO> upload(MultipartHttpServletRequest multipartRequest) {

		String imageFileName = null;
		Iterator<String> fileNames = multipartRequest.getFileNames();

		List<ProductAttachVO> list = new ArrayList<>();
		
		String uploadpath = "C:\\productImages\\upload\\";
		File uploadPath = new File(uploadpath);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		while (fileNames.hasNext()) {

			ProductAttachVO productAttachVO = new ProductAttachVO();
			String fileName = fileNames.next(); // input file name 뽑기
			productAttachVO.setFiletype(fileName); // 메인 이미지, 상세 이미지 타입 구분
			MultipartFile mFile = multipartRequest.getFile(fileName); // input file name으로 파일 뽑기
			String uploadFileName = mFile.getOriginalFilename(); // 파일에서 이미지 이름 뽑기
			uploadFileName = UUID.randomUUID().toString() + "_" + uploadFileName;
			productAttachVO.setFilename(uploadFileName);
			productAttachVO.setUploadpath(uploadpath);

			try {
				File saveFile = new File(uploadPath, uploadFileName);
				saveFile.getParentFile().mkdirs();
				mFile.transferTo(saveFile);
				list.add(productAttachVO);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/* 상품 상세 페이지 */
	@GetMapping("/product/productDetails")
	public String getProductDetails(@RequestParam("product_no") int product_no, @RequestParam(value = "sortType", required = false) String sortType, HttpServletRequest request,
			HttpServletResponse response, Model model, Criteria cri) {

		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		ProductVO product = productService.getProductDetailsByProduct_no(product_no);
		List<ProductAttachVO> imageList = productService.getProductImageByProductNo(product_no);
		String member_id = memberVO.getMember_id();
		PaginationVO paginationVO = new PaginationVO();
		cri.setPerPageNum(10);
		paginationVO.setCri(cri);

		List<ReviewVO> reviewList = null;
		paginationVO.setTotalCount(reviewBoardService.countAllReview(product_no));
		if ("".equals(sortType) || sortType == null) {
			reviewList = reviewBoardService.listReview(product_no, cri);
		} else if (sortType.equals("ByLikes")) {
			reviewList = reviewBoardService.getListbyLikes(product_no, cri);
			System.out.println(reviewList.get(1));
		} else if (sortType.equals("ByRatingAsc")) {
			reviewList = reviewBoardService.getListbyRatingAsc(product_no, cri);
		} else if (sortType.equals("ByRatingDesc")) {
			reviewList = reviewBoardService.getListbyRatingDesc(product_no, cri);
		}

		String temp = "";
		for (int i = 0; i < reviewList.size(); i++) {
			temp = reviewList.get(i).getMember_id();
			String displayName = temp.substring(0, 3);
			for (int j = 0; j < temp.length() - 3; j++) {
				displayName += "*";
				reviewList.get(i).setDisplayName(displayName);
			}
		}
		List<ReviewImageFileVO> reviewImageList = reviewBoardService.listReviewFile(product_no);
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("reviewImageList", reviewImageList);
		model.addAttribute("pageInfo", paginationVO);
		model.addAttribute("product_no", product_no);
		model.addAttribute("product", product);
		model.addAttribute("imageList", imageList);
		model.addAttribute("member_id", member_id);

		return "/product/productDetailsForm";
	}

	/* 상품 썸네일 */
	@GetMapping(value = "/productThumbs")
	public void thumbnails(@RequestParam("filepath") String filepath, @RequestParam("filename") String filename,
			@RequestParam(value = "displayType", required = false) String displayType, HttpServletResponse response)
			throws IOException {
		OutputStream out = response.getOutputStream();
		File thumbnail = new File(filepath + filename);

		if (!thumbnail.exists()) {
			thumbnail.mkdirs();
		}

		if ("".equals(displayType) || displayType == null) {
			Thumbnails.of(thumbnail).size(250, 220).crop(Positions.CENTER).toOutputStream(out);
		} else if (displayType.equals("main_detail")) {
			Thumbnails.of(thumbnail).size(420, 420).keepAspectRatio(true).toOutputStream(out);
		} else {
			Thumbnails.of(thumbnail).size(150, 150).crop(Positions.CENTER).toOutputStream(out);
		}

		byte[] buffer = new byte[1024 * 8];
		out.write(buffer);
		out.close();
	}



	/* 무한 스크롤 - Ajax 처리 */
	@ResponseBody
	@GetMapping(value = "/product/getBestProducts")
	public Map getBestProductList(@RequestParam("pageNum") String pageNum, Model model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {

		final int PAGE_ROW_COUNT = 2;
		int currentPage = Integer.parseInt(pageNum);

		int startRowNum = 0 + (currentPage - 1) * PAGE_ROW_COUNT;
		int endRowNum = currentPage * PAGE_ROW_COUNT;
		int rowCount = PAGE_ROW_COUNT;
		ProductVO product = new ProductVO();
		product.setStartRowNum(startRowNum);
		product.setEndRowNum(endRowNum);
		product.setRowCount(rowCount);

		List<ProductVO> productList = productService.getProductListWithPageInfo(product);
		List<ProductAttachVO> ImageList = new ArrayList<>();

		for (int i = 0; i < productList.size(); i++) {
			int product_no = productList.get(i).getProduct_no();
			ImageList.addAll(productService.getProductImageByProductNo(product_no));
		}
	

		int totalRow = productList.size() / 4;
		int totalPageCount = (int) Math.ceil(totalRow / (double) PAGE_ROW_COUNT);
		
		
		  HashMap<String, Object> map = new HashMap<>();
		  
		  map.put("newProductList", productList); 
		  map.put("newImageList", ImageList);
		  map.put("totalPageCount", totalPageCount); 
		  map.put("pageNum", pageNum);
		  map.put("totalRow", totalRow);	  
		  
		  return map;
		 
	}
	
	/*주문 정보 화면 */
	@RequestMapping(value="/product/paymentForm")
	public String paymentForm(@RequestParam("product_no")int product_no, @RequestParam("cart_product_quantity")String cart_product_quantity, @RequestParam("member_id") String member_id, @RequestParam("totalPrice")String totalPrice, Model model, HttpSession session) throws Exception {
		
		
		ShopcartVO shopcart= new ShopcartVO();
		shopcart.setCart_product_quantity(Integer.parseInt(cart_product_quantity));
		shopcart.setMember_id(member_id);
		shopcart.setProduct_no(product_no);
		shopcart.setTotalPrice(Integer.parseInt(totalPrice));
		memberVO = memberService.getMemberInfo(member_id);
		ProductVO product = productService.getProductDetailsByProduct_no(product_no);
	
		List<AddressVO> addressList = adressService.list(member_id);
		
		model.addAttribute("shopcart", shopcart);
		model.addAttribute("addressList", addressList);
		model.addAttribute("memberVO", memberVO);
		model.addAttribute("product", product);
		
		session.setAttribute("shopcart", shopcart);
		session.setAttribute("memberVO", memberVO);
		session.setAttribute("product_no", product_no);
		session.setAttribute("addressList", addressList);
		
		return "/product/paymentForm";
		
	}
	
	/* 결제 화면 */ 
	@RequestMapping(value="/product/payment")
	public ModelAndView processPayment(@RequestParam(value = "member_id", required = false)String member_id, 
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		
		
		memberVO = (MemberVO) session.getAttribute("memberVO");
		int product_no = (int) session.getAttribute("product_no");
		ShopcartVO shopcart = (ShopcartVO) session.getAttribute("shopcart");
		List<AddressVO> list = (List<AddressVO>) session.getAttribute("addressList");
		ProductVO product = productService.getProductDetailsByProduct_no(product_no);
		PurchaseHistoryVO history = new PurchaseHistoryVO();
		history.setMember_id(memberVO.getMember_id());	
		history.setProduct_delivery_price(product.getProduct_delivery_price());
		history.setProduct_quantity(shopcart.getCart_product_quantity());
		history.setProduct_price(product.getProduct_price());
		history.setProduct_sale_percent(product.getProduct_sale_percent());
		history.setProduct_price(product.getProduct_price());
		history.setProduct_no(product_no);
		history.setAddress_id(list.get(0).getAddress_id());
		
		productService.updatePurchaseHistory(history);
	
		
		return new ModelAndView("forward:/main");

	}
	


	@GetMapping("/product/list")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(productService.getlist());
		model.addAttribute("productList", productService.getlist());

		return "list";
	}

	@GetMapping("/product/get")
	public String get(Model model, @RequestParam("product_no") int product_no, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		model.addAttribute("product", productService.productDetail(product_no));
		return "get";
	}

	@GetMapping("/product/modifyProduct")
	public String modifyProduct(Model model, @RequestParam("product_no") int product_no, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		model.addAttribute("product", productService.productDetail(product_no));
		return "modifyProduct";
	}

	@PostMapping("/product/modifyGoodsImageInfo")
	public void modifyGoodsImageInfo(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		System.out.println("modifyGoodsImageInfo");
		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String imageFileName = null;

		Map goodsMap = new HashMap();
		Enumeration enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			goodsMap.put(name, value);
		}

//		HttpSession session = multipartRequest.getSession();
//		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
//		String reg_id = memberVO.getMember_id();

		List<ProductAttachVO> imageFileList = null;
		int goods_id = 0;
		int image_id = 0;
		try {
			imageFileList = upload(multipartRequest);
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ProductAttachVO imageFileVO : imageFileList) {
					goods_id = Integer.parseInt((String) goodsMap.get("goods_id"));
					image_id = Integer.parseInt((String) goodsMap.get("image_id"));
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
		} catch (Exception e) {
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ProductAttachVO imageFileVO : imageFileList) {
					imageFileName = imageFileVO.getFilename();
//					File srcFile = new File(CURR_IMAGE_REPO_PATH+"\\"+"temp"+"\\"+imageFileName);
//					srcFile.delete();
				}
			}
			e.printStackTrace();
		}

	}

	@PostMapping("/product/removeGoodsImage")
	public void removeGoodsImage(@RequestParam("img_no") int img_no, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// db에서 해당 번호와 같은 이미지 삭제
		System.out.println(img_no);
		productService.removeGoodsImage(img_no);
	}

	@PostMapping("/product/addNewGoodsImage")
	@ResponseBody
	public int addNewGoodsImage(@RequestParam("goods_id") int _goods_id, MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response) throws Exception {

		System.out.println("addNewGoodsImage");
		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

//		String imageFileName=null;
//		Map goodsMap = new HashMap();
//		Enumeration enu=multipartRequest.getParameterNames();
//		while(enu.hasMoreElements()){
//			String name=(String)enu.nextElement();
//			String value=multipartRequest.getParameter(name);
//			goodsMap.put(name,value);
//		}

//		HttpSession session = multipartRequest.getSession();
//		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
//		String reg_id = memberVO.getMember_id();

		List<ProductAttachVO> imageFileList = null;
		int img_no = 0;
		try {
			imageFileList = upload(multipartRequest);
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ProductAttachVO imageFileVO : imageFileList) {
					imageFileVO.setProduct_no(_goods_id);
					img_no = productService.addNewGoodsImage(imageFileVO);
				}

//				for(ImageFileVO  imageFileVO:imageFileList) {
//					imageFileName = imageFileVO.getFileName();
//					File srcFile = new File(CURR_IMAGE_REPO_PATH+"\\"+"temp"+"\\"+imageFileName);
//					File destDir = new File(CURR_IMAGE_REPO_PATH+"\\"+goods_id);
//					FileUtils.moveFileToDirectory(srcFile, destDir,true);
//				}
			}
		} catch (Exception e) {
//			if(imageFileList!=null && imageFileList.size()!=0) {
//				for(ProductAttachVO  imageFileVO:imageFileList) {
//					imageFileName = imageFileVO.getFileName();
//					File srcFile = new File(CURR_IMAGE_REPO_PATH+"\\"+"temp"+"\\"+imageFileName);
//					srcFile.delete();
//				}
//			}
			e.printStackTrace();
		}
		return img_no;
	}

	/* 이미지 파일 확인 */
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
