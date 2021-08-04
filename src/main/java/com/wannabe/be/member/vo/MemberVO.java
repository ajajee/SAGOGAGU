package com.wannabe.be.member.vo;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;
@Component
@Data
public class MemberVO {
	
	private String member_id;
	private String member_pw;
	private String member_name;
	private String member_phone;
	private String member_email;
	private String member_authority;
	private Date regdate;
	private Date updatedate;
	private String secession;
	
}
