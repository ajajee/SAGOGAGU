package com.wannabe.be.reviewboard.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wannabe.be.utills.Criteria;

public interface ReviewBoardController {
	
	public String reviewForm();
	int getLikes(int review_no);
	int updateLikes(int review_no);
	ModelAndView reviewBoard(int product_no, String sortType, Criteria cri);
	ModelAndView uploadReview(List<MultipartFile> files, String review_content, String member_id, int product_no,
	int rating, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes);
	ResponseEntity<byte[]> getImage(String filename, int product_no) throws IOException;
	void thumbnails(String filename, HttpServletResponse response) throws IOException;
}
