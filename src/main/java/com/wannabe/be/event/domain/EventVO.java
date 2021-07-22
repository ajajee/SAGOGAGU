package com.wannabe.be.event.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EventVO {
	
	private int event_id;
	private Date begindate; 
	private Date enddate; 
	private int product_no;
	
	
}
