package com.wannabe.be.qnaboard.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wannabe.be.member.vo.MemberVO;
import com.wannabe.be.qnaboard.vo.QnaReplyVO;
import com.wannabe.be.qnaboard.vo.QnaVO;
import com.wannabe.be.utills.Criteria;
import com.wannabe.be.utills.PaginationVO;


public interface QnaBoardController {

public String viewQnaBoard();
public ModelAndView loginCheck(HttpServletRequest request, HttpServletResponse response);
public ModelAndView uploadQna(String title, String qna_content, String member_id, int product_no) throws DataAccessException;
public String postReply(int product_no, int parent_no, String qna_reply_content) throws DataAccessException;
public List<QnaReplyVO> getReply(int parent_no) throws DataAccessException;
public int deleteQna(@RequestParam(value = "qna_no")int qna_no) throws DataAccessException;
public int modifyQna(@RequestParam(value = "qna_no") int qna_no,
		@RequestParam(value = "qna_content") String qna_content) throws DataAccessException;
public int updateQnaViews(@RequestParam("qna_no") int qna_no) throws DataAccessException;
public QnaVO getQnaViews(@RequestParam("qna_no") int qna_no) throws DataAccessException;
ModelAndView listQnawithPaging(Criteria cri, int product_no, HttpServletRequest request, HttpServletResponse response);


}