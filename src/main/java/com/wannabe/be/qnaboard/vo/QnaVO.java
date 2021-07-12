package com.wannabe.be.qnaboard.vo;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Component
@Getter
@Setter
public class QnaVO {
	private int qna_no;
	private int product_no;
	private String member_id;
	private String qna_content;
	private Date replyDate;
	private Date updateDate;
	private String title; 
	private int view_count;
	private String displayName;
	private int isAnswered;

	
}
