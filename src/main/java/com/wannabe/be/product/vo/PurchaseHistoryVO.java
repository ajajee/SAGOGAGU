package com.wannabe.be.product.vo;

import java.sql.Date;

import com.wannabe.be.address.vo.AddressVO;

import lombok.Data;

@Data
public class PurchaseHistoryVO {

	int product_no;
	int product_quantity;
	int product_price;
	int product_delivery_price;
	int product_sale_percent;
	int address_id;
	String address_message;
	String purchase_history_state;
	Date regdate;
	Date delivery_start_datetime;
	Date deliver_end_datetime;
	Date cancel_request_datetime;
	Date refund_request_datetime;
	Date refund_complete_datetime;
	String member_id;
	String product_title;
	
	
}
