package com.wannabe.be.qnaboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wannabe.be.member.vo.MemberVO;
import com.wannabe.be.qnaboard.dao.QnaBoardMapper;
import com.wannabe.be.qnaboard.vo.QnaReplyVO;
import com.wannabe.be.qnaboard.vo.QnaVO;
import com.wannabe.be.utills.Criteria;
import com.wannabe.be.utills.PaginationVO;

@Component
@Service("QnaBoardService")
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class QnaBoardServiceImpl implements QnaBoardService {
	@Autowired
	QnaBoardMapper qnaBoardMapper;
	
	@Override
	public List<QnaVO> listQna(int product_no) {
		List<QnaVO> qnaList = null;
		qnaList = qnaBoardMapper.listQna(product_no);
		return qnaList;
	}
	/*
	 * public <QnaVO> getallQnaByProductNo(int product_no){
	 * 
	 * return qnaBoardMapper.getQnaByProductNo(product_no); }
	 */

	@Override
	public void writeQna(QnaVO qnaVO) {
		qnaBoardMapper.writeQna(qnaVO);
	}

	@Override
	public int modifyQna(QnaVO qnaVO) {
		return qnaBoardMapper.modifyQna(qnaVO);
		
	}
	
	public int deleteQna(int qna_no) {
		return qnaBoardMapper.deleteQna(qna_no);
		
	}


	@Override
	public List<QnaReplyVO> listQnaReply(int product_no) {
		List<QnaReplyVO> replyList =null;
		replyList = qnaBoardMapper.listQnaReply(product_no);
		return replyList;
	}

	@Override
	public void postReply(QnaReplyVO qnaReplyVO) {
		qnaBoardMapper.postReply(qnaReplyVO);
		
	}

	@Override 
	public List<QnaReplyVO> fetchReplies(int parent_no) {
		List<QnaReplyVO> qnaReplyList = qnaBoardMapper.fetchReply(parent_no);
		return qnaReplyList;
	}

	@Override
	public int updateQnaViews(int qna_no) {
		return qnaBoardMapper.updateQnaViews(qna_no);
	}

	public QnaVO getQnaViews(int qna_no) {
		QnaVO qnaVO = new QnaVO();
		int view_count = qnaBoardMapper.getQnaViews(qna_no);
		qnaVO.setView_count(view_count);
		return qnaVO;
	}
	

	@Override
	public List<QnaVO> listQnaWithPaging(int product_no,  Criteria cri){
		return qnaBoardMapper.listQnaWithPaging(product_no, cri);
	}
	
	public int countAllQna(int product_no) {
		return qnaBoardMapper.countAllQna(product_no);
	}

	@Override
	public void updateQnaStatus(int qna_no) {
	 qnaBoardMapper.updateQnaStatus(qna_no);
		
	}
	
}
