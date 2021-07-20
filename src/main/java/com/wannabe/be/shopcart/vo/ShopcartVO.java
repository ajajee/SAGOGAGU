package com.wannabe.be.shopcart.vo;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ShopcartVO {
	
	private int shopcart_no;
	private int product_no;
	private String member_id;
	private int cart_product_quantity;
	private Date regdate;

}
