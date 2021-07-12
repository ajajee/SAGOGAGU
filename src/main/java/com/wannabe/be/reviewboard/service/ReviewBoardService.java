package com.wannabe.be.reviewboard.service;

import java.util.List;

import com.wannabe.be.reviewboard.vo.ReviewImageFileVO;
import com.wannabe.be.reviewboard.vo.ReviewVO;
import com.wannabe.be.utills.Criteria;

public interface ReviewBoardService {

	int uploadReview(ReviewVO reviewVO);
	void uploadReviewImage(ReviewImageFileVO fileVO);
	List<ReviewVO> listReview(int product_no, Criteria cri);
	List<ReviewImageFileVO> listReviewFile(int review_no);
	String getUploadPath(String filename);
	int updateLikes(int review_no);
	int countAllReview(int product_no);
	int getLikesCount(int review_no);
	List<ReviewVO> getListbyLikes(int product_no, Criteria cri);
	List<ReviewVO> getListbyRatingAsc(int product_no, Criteria cri);
	List<ReviewVO> getListbyRatingDesc(int product_no, Criteria cri);
	List<ReviewImageFileVO> getImageList(int review_no);

}
