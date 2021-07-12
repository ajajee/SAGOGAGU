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
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.wannabe.be.domain.Criteria;
import com.wannabe.be.domain.PageDTO;
import com.wannabe.be.member.vo.MemberVO;
import com.wannabe.be.product.service.ProductService;
import com.wannabe.be.product.vo.AttachFileDTO;
import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductVO;

import net.coobird.thumbnailator.Thumbnailator;

@Controller
public class ProductControllerImpl implements ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductVO productVO;

	@Autowired
	private MemberVO memberVO;

	@GetMapping("/product/productform")
	public String productform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "productform";
	}

	@GetMapping("/product/list")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(productService.getlist());
		model.addAttribute("productList", productService.getlist());

		return "list";
	}

	@GetMapping("/product/get")
	public String get(Model model, @RequestParam("product_no") int product_no, HttpServletRequest request, HttpServletResponse response) throws Exception{
		model.addAttribute("product", productService.productDetail(product_no));
		return "get";
	}
	

	@GetMapping("/product/modifyProduct")
	public String modifyProduct(Model model, @RequestParam("product_no") int product_no, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		model.addAttribute("product", productService.productDetail(product_no));
		return "modifyProduct";
	}

	
	
	@PostMapping("/product/insertproduct")
	public String insertproduct(ProductVO _productVO, MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response) throws Exception {
		// 세션에서 회원 아이디 가져와서 상품 객체에 담기
		HttpSession session = multipartRequest.getSession();
		memberVO = (MemberVO) session.getAttribute("memberInfo");
		_productVO.setProduct_writer(memberVO.getMember_id());

		// 멀티파트리퀘스트에서 이미지파일 추출
		_productVO.setAttachList(upload(multipartRequest));

		System.out.println(_productVO.getAttachList());
		productService.insertproduct(_productVO);

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
	
	@PostMapping("/product/addNewGoodsImage")
	@ResponseBody
	public int addNewGoodsImage(@RequestParam("goods_id") int _goods_id,
			MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		
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
		
		List<ProductAttachVO> imageFileList=null;
		int img_no=0;
		try {
			imageFileList =upload(multipartRequest);
			if(imageFileList!= null && imageFileList.size()!=0) {
				for(ProductAttachVO imageFileVO : imageFileList) {
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
		}catch(Exception e) {
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
	

	// 이미지 저장 및 uuid, 파일이름, 타입 설정
	public List<ProductAttachVO> upload(MultipartHttpServletRequest multipartRequest) {

		String imageFileName = null;
		Iterator<String> fileNames = multipartRequest.getFileNames();

		List<ProductAttachVO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload";

		String uploadFolderPath = getFolder();
		// make folder --------
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
			System.out.println("HI");
		}
		// make yyyy/MM/dd folder

		while (fileNames.hasNext()) {
			ProductAttachVO productAttachVO = new ProductAttachVO();
			String fileName = fileNames.next(); // input file name 뽑기
			productAttachVO.setFiletype(fileName); // 메인 이미지, 상세 이미지 타입 구분

			MultipartFile mFile = multipartRequest.getFile(fileName); // input file name으로 파일 뽑기
			String uploadFileName = mFile.getOriginalFilename(); // 파일에서 이미지 이름 뽑기
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1); //
			productAttachVO.setFilename(uploadFileName);
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;

			try {
				File saveFile = new File(uploadPath, uploadFileName);
				productAttachVO.setUuid(uuid.toString());
				productAttachVO.setUploadpath(uploadFolderPath);

				if (checkImageType(saveFile)) {
					System.out.println(uploadPath);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					System.out.println(uploadFileName);
					Thumbnailator.createThumbnail(mFile.getInputStream(), thumbnail, 100, 100);
					mFile.transferTo(saveFile);

					System.out.println("111");
					thumbnail.close();
					System.out.println("222");
				}

				// add to List
				list.add(productAttachVO);
				System.out.println(list);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return list;
	}

	// 이미지 저장경로 폴더 만들기
	private String getFolder() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();

		String str = sdf.format(date);

		return str.replace("-", File.separator);
	}

	// 이미지 파일 확인
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
