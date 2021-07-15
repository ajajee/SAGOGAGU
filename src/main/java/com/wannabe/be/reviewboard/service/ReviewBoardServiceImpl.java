package com.wannabe.be.reviewboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wannabe.be.reviewboard.dao.ReviewBoardMapper;
import com.wannabe.be.reviewboard.vo.ReviewImageFileVO;
import com.wannabe.be.reviewboard.vo.ReviewVO;
import com.wannabe.be.utills.Criteria;

@Service("ReviewBoardService")
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class ReviewBoardServiceImpl implements ReviewBoardService {
	
	 @Autowired
	 private ReviewBoardMapper reviewBoardMapper;

	@Override
	public int uploadReview(ReviewVO reviewVO) {
		return reviewBoardMapper.uploadReview(reviewVO);
	}



	@Override
	public List<ReviewVO> listReview(int product_no, Criteria cri) {
		return reviewBoardMapper.listReview(product_no, cri);
		
	}



	@Override
	public void uploadReviewImage(ReviewImageFileVO fileVO) {
		reviewBoardMapper.uploeadReviewImage(fileVO);
		
	}



	@Override
	public List<ReviewImageFileVO> listReviewFile(int product_no) {
		return reviewBoardMapper.listReviewFile(product_no);

	}



	@Override
	public String getUploadPath(String filename) {
		return reviewBoardMapper.getUploadPath(filename);
	}



	public int updateLikes(int review_no) {
		return reviewBoardMapper.updateLikes(review_no);
	}



	public int countAllReview(int product_no) {
		return reviewBoardMapper.countAllReview(product_no);
	}



	@Override
	public int getLikesCount(int review_no) {
		return reviewBoardMapper.getLikesCount(review_no);
	}



	@Override
	public List<ReviewVO> getListbyLikes(int product_no, Criteria cri) {
		return reviewBoardMapper.getListByLikes(product_no, cri);
	}



	public List<ReviewVO> getListbyRatingAsc(int product_no, Criteria cri) {
		return reviewBoardMapper.getListByRatingAsc(product_no, cri);
	}



	@Override
	public List<ReviewVO> getListbyRatingDesc(int product_no, Criteria cri) {
		return reviewBoardMapper.getListByRatingDesc(product_no, cri);
	}



	@Override
	public List<ReviewImageFileVO> getImageList(int review_no) {
		return reviewBoardMapper.getImageList(review_no);
	}





}
