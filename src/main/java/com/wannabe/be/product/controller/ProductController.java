
package com.wannabe.be.product.controller;

import java.awt.PageAttributes.MediaType;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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

import com.wannabe.be.member.vo.MemberVO;
import com.wannabe.be.product.service.ProductService;
import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductCategoryVO;
import com.wannabe.be.product.vo.ProductVO;
import com.wannabe.be.shopcart.vo.ShopcartVO;

import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

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
	public String getProductDetails(@RequestParam("product_no") int product_no, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		ProductVO product = productService.getProductDetailsByProduct_no(product_no);
		List<ProductAttachVO> imageList = productService.getProductImageByProductNo(product_no);
		String member_id = memberVO.getMember_id();
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
			Thumbnails.of(thumbnail).size(290, 260).crop(Positions.CENTER).toOutputStream(out);
		} else if (displayType.equals("main_detail")) {
			Thumbnails.of(thumbnail).size(490, 490).keepAspectRatio(true).toOutputStream(out);
		} else {
			Thumbnails.of(thumbnail).size(150, 150).crop(Positions.CENTER).toOutputStream(out);
		}

		byte[] buffer = new byte[1024 * 8];
		out.write(buffer);
		out.close();
	}

	/* 카트에 아이템 추가 */
	@GetMapping(value = "/product/addItemToCart")
	public void addItemToCart(@RequestParam("product_no") int product_no, @RequestParam("member_id") String member_id,
			@RequestParam("cart_product_quantity") int cart_product_quantity, HttpServletRequest request,
			HttpServletResponse response) {
		ShopcartVO shopcartVO = new ShopcartVO();
		shopcartVO.setMember_id(member_id);
		shopcartVO.setProduct_no(product_no);
		shopcartVO.setCart_product_quantity(cart_product_quantity);
		productService.addItemToCart(shopcartVO);
	}

	/* 무한 스크롤 - Ajax 처리 */
	@ResponseBody
	@GetMapping(value = "/product/getBestProducts")
	public Object getBestProductList(@RequestParam("pageNum") String pageNum, Model model, HttpServletRequest request,
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

		List<ProductVO> newProductList = productService.getProductListWithPageInfo(product);
		List<ProductAttachVO> newImageList = new ArrayList<>();

		for (int i = 0; i < newProductList.size(); i++) {
			int product_no = newProductList.get(i).getProduct_no();
			newImageList.addAll(productService.getProductImageByProductNo(product_no));
		}

		int totalRow = newProductList.size() / 4;

		int totalPageCount = (int) Math.ceil(totalRow / (double) PAGE_ROW_COUNT);

		HashMap<String, Object> map = new HashMap<>();
		map.put("newProductList", newProductList);
		map.put("totalPageCount", totalPageCount);
		map.put("pageNum", pageNum);
		map.put("totalRow", totalRow);
		
		System.out.println(map);
		return map;
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
