package com.wannabe.be.product.vo;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ProductVO {
	int product_no;
	String product_category_code;
	String product_title;
	String product_content;
	String product_writer;
	int product_price;
	String product_delivery_message;
	int product_delivery_price;
	int product_sale_percent;
	String product_company;
	int product_quantity;
	String product_type;
	String product_code;
	Date regdate;
	Date updatedate;
	String secession;

	private List<ProductAttachVO> attachList;
}
