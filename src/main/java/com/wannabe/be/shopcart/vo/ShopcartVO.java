package com.wannabe.be.shopcart.vo;

import java.util.Date;


import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShopcartVO {
	
	private int cart_no;
	private int product_no;
	private String member_id;
	private int cart_product_quantity;
	private Date regdate;
	private long totalPrice; 
	
	private int cpq;


	
}

