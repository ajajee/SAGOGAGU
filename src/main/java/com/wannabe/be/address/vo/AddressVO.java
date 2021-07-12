package com.wannabe.be.address.vo;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressVO {
	
		private int address_id;
		private String address_name;
		private String zipcode;
		private String address_first;
		private String address_middle;
		private String address_last;
		private String address_message;
		private String member_id;
	
		

}

