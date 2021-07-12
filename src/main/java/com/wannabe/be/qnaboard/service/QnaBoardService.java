package com.wannabe.be.qnaboard.service;

import java.util.List;

import com.wannabe.be.qnaboard.vo.QnaReplyVO;
import com.wannabe.be.qnaboard.vo.QnaVO;
import com.wannabe.be.utills.Criteria;

public interface QnaBoardService {

	public List<QnaVO> listQna(int product_no);
	public void writeQna(QnaVO qnaVO);
	public int modifyQna(QnaVO qnaVO);
	public int deleteQna(int qna_no);
	public List<QnaReplyVO> listQnaReply(int product_no);
	public void postReply(QnaReplyVO qnaReplyVO);
	public List<QnaReplyVO> fetchReplies(int parent_no);
	public int updateQnaViews(int qna_no);
	public QnaVO getQnaViews(int qna_no);
	public List<QnaVO> listQnaWithPaging(int product_no,  Criteria cri);
	public int countAllQna(int product_no);
	public void updateQnaStatus(int qna_no);
	

}
