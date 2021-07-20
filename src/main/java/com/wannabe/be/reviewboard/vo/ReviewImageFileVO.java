package com.wannabe.be.reviewboard.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewImageFileVO {
	private int review_img_id;
	private String uuid;
	private String filename; 
	private int review_no;
	private List<ReviewImageFileVO> files;
	private String uploadpath;
	private int product_no;
	private boolean main_img;

	
}
