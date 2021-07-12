package com.wannabe.be.reviewboard.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
	private String uuid;
	private String filename; 
	private String filetype;
	private int review_no;
	private List<ReviewImageFileVO> files;
	private String fileOriginalName;
	private String uploadpath;
	private int product_no;

	
}
