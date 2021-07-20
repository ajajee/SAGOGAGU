package com.wannabe.be.reviewboard.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ReviewVO {
private Date regdate;
private int likes;
private int rating;
private int review_no;
private int product_no;
private String review_content;
private String member_id;
private String displayName;


 }
