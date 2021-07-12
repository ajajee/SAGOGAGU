package com.wannabe.be.mypage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageControllerImpl implements MypageController {
	@GetMapping("/mypage")
	public String mypage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "mypage";
	}
}
