package com.wannabe.be.member.vo;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;
@Component
@Data
public class MemberVO {
	
	String member_id;
	String member_pw;
	String member_name;
	String member_phone;
	String member_email;
	String member_authority;
	Date regdate;
	Date updatedate;
	String secession;;
}
