package com.wannabe.be.product.vo;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AttachFileDTO {
	private String filename;
	private String uploadpath;
	private String uuid;
	private String filetype;
}
