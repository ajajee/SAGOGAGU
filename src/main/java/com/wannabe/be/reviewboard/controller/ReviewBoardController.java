package com.wannabe.be.reviewboard.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wannabe.be.reviewboard.service.ReviewBoardService;
import com.wannabe.be.reviewboard.vo.ReviewImageFileVO;
import com.wannabe.be.reviewboard.vo.ReviewVO;
import com.wannabe.be.utills.Criteria;
import com.wannabe.be.utills.PaginationVO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@Controller
public class ReviewBoardController {

	@Autowired
	private ReviewBoardService reviewBoardService;

	/* 리뷰 작성 폼 */
	@GetMapping("/product/reviewForm")
	public String reviewForm(@RequestParam(value ="product_no")int product_no, HttpSession session) {
		session.setAttribute("product_no", product_no);
	
		return "/board/reviewForm";
	}

	/* 리뷰 작성 */
	@RequestMapping(value = "/product/uploadReview", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseStatus(HttpStatus.CREATED)
	public ModelAndView uploadReview(@RequestParam(value = "images") List<MultipartFile> files,
			@RequestParam(value = "review_content") String review_content,
			@RequestParam(value = "member_id", required = false) String member_id,
			@RequestParam(value = "product_no") int product_no, @RequestParam(value = "rating") int rating,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

		String uploadpath = "C:\\reviewImages\\upload\\" + product_no + "\\";
		ReviewVO reviewVO = new ReviewVO();
		reviewVO.setMember_id(member_id);
		reviewVO.setProduct_no(product_no);
		reviewVO.setRating(rating);
		reviewVO.setReview_content(review_content);
		if (files.isEmpty()) {
			reviewBoardService.uploadReview(reviewVO);
		} else {
			reviewBoardService.uploadReview(reviewVO);
			int review_no = reviewVO.getReview_no();

			for (MultipartFile file : files) {
				String filename = file.getOriginalFilename();
				String desFileName =  UUID.randomUUID().toString() + "_" + filename; /* + "." + filetype; */
				File desFile = new File(uploadpath + desFileName);
				desFile.getParentFile().mkdirs();
				try {
					file.transferTo(desFile);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
				ReviewImageFileVO fileVO = new ReviewImageFileVO();
				fileVO.setReview_no(review_no);
				fileVO.setFilename(desFileName);
				fileVO.setProduct_no(product_no);
				fileVO.setUploadpath(uploadpath);
				reviewBoardService.uploadReviewImage(fileVO);
				System.out.println(fileVO);
			}

		}
		redirectAttributes.addAttribute("product_no", product_no);
		return new ModelAndView("forward:/product/reviewBoard");

	}

	/* 리뷰 게시판 */
	@RequestMapping(value = "/product/reviewBoard", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView reviewBoard(@RequestParam("product_no") int product_no,
			@RequestParam(value = "sortType", required = false) String sortType, Criteria cri) {

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
		List<ReviewImageFileVO> imageList = reviewBoardService.listReviewFile(product_no);
		ModelAndView mv = new ModelAndView("/board/reviewBoard");
		mv.addObject("reviewList", reviewList);
		mv.addObject("imageList", imageList);
		mv.addObject("pageInfo", paginationVO);
		mv.addObject("product_no", product_no);
		System.out.println(paginationVO);

		return mv;

	}

	/* 리뷰 게시판 썸네일 */
	@GetMapping(value = "/thumbnails")
	public void thumbnails(@RequestParam("filepath") String filepath, @RequestParam("filename") String filename,
			HttpServletResponse response) throws IOException {
		OutputStream out = response.getOutputStream();
		File thumnail = new File(filepath + filename);

		if (thumnail.exists()) {
			Thumbnails.of(thumnail).size(95, 95).crop(Positions.CENTER).toOutputStream(out);
			byte[] buffer = new byte[1024 * 8];
			out.write(buffer);
			out.close();
		}
	}

	/* 게시판 이미지 클릭시 원본 이미지 보여주기 */
	@GetMapping(value = "/reviewImage", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getImage(@RequestParam("filepath") String filepath,
			@RequestParam(value = "filename") String filename, @RequestParam(value = "product_no") int product_no)
			throws IOException {
		// String filepath = reviewBoardService.getUploadPath(filename);
		File img = new File(filepath + filename);
		return ResponseEntity.ok()
				.contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img)))
				.body(Files.readAllBytes(img.toPath()));
	}

	/* 좋아요 수 증가 */
	@GetMapping(value = "/product/updateLikes")
	@ResponseBody
	public int updateLikes(@RequestParam(value = "review_no") int review_no) {
		return reviewBoardService.updateLikes(review_no);

	}

	/* 좋아요 수 반영 */
	@GetMapping(value = "/product/getLikesCount")
	@ResponseBody
	public int getLikes(@RequestParam(value = "review_no") int review_no) {
		return reviewBoardService.getLikesCount(review_no);

	}

}
