package com.wannabe.be.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.wannabe.be.reviewboard.vo.ReviewImageFileVO;
import com.wannabe.be.reviewboard.vo.ReviewVO;
import com.wannabe.be.utills.Criteria;

@Mapper
public interface ReviewBoardMapper {

	@Insert("INSERT INTO REVIEW (PRODUCT_NO, REVIEW_CONTENT, RATING, MEMBER_ID) VALUES (#{reviewVO.product_no}, #{reviewVO.review_content}, #{reviewVO.rating}, #{reviewVO.member_id})") 
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty = "reviewVO.review_no", before=false, resultType=int.class)
	public int uploadReview(@Param("reviewVO") ReviewVO reviewVO);

	@Insert("INSERT INTO REVIEW_IMG (UPLOADPATH, FILENAME, FILETYPE, FILEORIGINALNAME, UUID, PRODUCT_NO, REVIEW_NO) VALUES (#{uploadpath}, #{filename}, #{filetype}, #{fileOriginalName}, #{uuid}, #{product_no}, #{review_no})")
	public void uploeadReviewImage(ReviewImageFileVO fileVO);
	
	@Select("SELECT * FROM REVIEW WHERE PRODUCT_NO = #{product_no} ORDER BY REGDATE DESC LIMIT #{cri.pageStart}, #{cri.perPageNum}")
	public List<ReviewVO> listReview(@Param("product_no") int product_no, @Param("cri") Criteria cri);
	
	@Select("SELECT * FROM REVIEW_IMG WHERE PRODUCT_NO = #{product_no}")
	public List<ReviewImageFileVO> listReviewFile(int product_no);
	
	@Select("SELECT UPLOADPATH FROM REVIEW_IMG WHERE FILENAME = #{filename}")
	public String getUploadPath(String filename);
	

	@Update("UPDATE REVIEW SET LIKES = ifnull(likes, 0) + 1 WHERE REVIEW_NO= #{review_no}")
	public int updateLikes(int review_no);

	@Select("SELECT COUNT(*) FROM REVIEW WHERE PRODUCT_NO = #{product_no}")
	public int countAllReview(int product_no);
	
	@Select("SELECT LIKES FROM REVIEW WHERE REVIEW_NO = #{review_no}")
	public int getLikesCount(int review_no);
		
	@Select("SELECT * FROM REVIEW WHERE PRODUCT_NO = #{product_no} ORDER BY LIKES DESC, REGDATE DESC LIMIT #{cri.pageStart}, #{cri.perPageNum}")
	public List<ReviewVO> getListByLikes(@Param("product_no") int product_no, @Param("cri") Criteria cri);
	
	@Select("SELECT * FROM REVIEW WHERE PRODUCT_NO = #{product_no} ORDER BY RATING ASC, REGDATE DESC LIMIT #{cri.pageStart}, #{cri.perPageNum}")
	public List<ReviewVO> getListByRatingAsc(@Param("product_no")int product_no, @Param("cri") Criteria cri);
	
	@Select("SELECT * FROM REVIEW WHERE PRODUCT_NO = #{product_no} ORDER BY RATING DESC, REGDATE DESC LIMIT #{cri.pageStart}, #{cri.perPageNum}")
	public List<ReviewVO> getListByRatingDesc(@Param("product_no")int product_no, @Param("cri") Criteria cri);
	
	@Select("SELECT * FROM REVIEW_IMG WHERE REVIEW_NO = #{review_no}")
	public List<ReviewImageFileVO> getImageList(int review_no);

}
