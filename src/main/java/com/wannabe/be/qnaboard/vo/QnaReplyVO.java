package com.wannabe.be.qnaboard.vo;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QnaReplyVO {
	
	private int parent_no;
	private int product_no; 
	private String qna_reply_writer;
	private String qna_reply_title;
	private int qna_reply_no;
	private String qna_reply_content;
	private Date qna_reply_date;	
	private int view_count;
}
