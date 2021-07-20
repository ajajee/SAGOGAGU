package com.wannabe.be.product.vo;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
public class ProductAttachVO {

	private String uploadpath;
	private String filename;
	private String filetype;
	private int product_img_no;
	private int product_no;

}
