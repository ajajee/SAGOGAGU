package com.wannabe.be.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wannabe.be.qnaboard.vo.QnaReplyVO;
import com.wannabe.be.qnaboard.vo.QnaVO;
import com.wannabe.be.utills.Criteria;
import com.wannabe.be.utills.PaginationVO;

@Mapper
public interface QnaBoardMapper {

	@Select("SELECT * FROM QNA WHERE PRODUCT_NO = #{product_no} ORDER BY REPLYDATE DESC")	
	public List<QnaVO> listQna(@Param("product_no") int product_no);

	@Insert("INSERT INTO QNA (PRODUCT_NO, TITLE, QNA_CONTENT, MEMBER_ID) VALUES (#{qnaVO.product_no}, #{qnaVO.title}, #{qnaVO.qna_content}, #{qnaVO.member_id})")
	public int writeQna(@Param("qnaVO") QnaVO qnaVO);

	@Update("UPDATE QNA SET QNA_CONTENT = #{qnaVO.qna_content} WHERE QNA_NO = #{qnaVO.qna_no}")
	public int modifyQna(@Param("qnaVO") QnaVO qnaVO);

	@Delete("DELETE FROM QNA WHERE QNA_NO = #{qna_no}")
	public int deleteQna(@Param("qna_no") int qna_no);

	@Select("SELECT * FROM QNA_REPLY WHERE PRODUCT_NO= #{product_no}")
	public List<QnaReplyVO> listQnaReply(int product_no);
	
	@Insert("INSERT INTO QNA_REPLY (PARENT_NO, PRODUCT_NO, QNA_REPLY_CONTENT) VALUES (#{qnaReplyVO.parent_no}, #{qnaReplyVO.product_no}, #{qnaReplyVO.qna_reply_content})")
	public void postReply(@Param("qnaReplyVO") QnaReplyVO qnaReplyVO);
	
	@Select("SELECT * FROM QNA_REPLY WHERE PARENT_NO = #{PARENT_NO} ORDER BY QNA_REPLY_NO LIMIT 1")
	public List<QnaReplyVO> fetchReply(int parent_no);
	
	@Update("UPDATE QNA SET VIEW_COUNT = ifnull(view_count, 0) + 1 WHERE QNA_NO = #{qna_no}")
	public int updateQnaViews(int qna_no);

	@Select("SELECT VIEW_COUNT FROM QNA WHERE QNA_NO = #{qna_no}")
	public int getQnaViews(int qna_no);

	@Select("SELECT COUNT(*) FROM QNA WHERE PRODUCT_NO = #{product_no}")
	public int countAllQna(int product_no);

	@Select("SELECT * FROM QNA WHERE PRODUCT_NO = #{product_no} ORDER BY REPLYDATE DESC LIMIT #{cri.pageStart}, #{cri.perPageNum}")
	public List<QnaVO> listQnaWithPaging(@Param("product_no")int product_no, @Param("cri") Criteria cri);
	
	@Update("UPDATE QNA SET ISANSWERED = TRUE WHERE QNA_NO = #{qna_no}")
	public void updateQnaStatus(@Param("qna_no") int qna_no);
	
}