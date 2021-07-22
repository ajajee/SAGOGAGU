package com.wannabe.be.event.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.wannabe.be.common.CommonUtil;
import com.wannabe.be.event.domain.EventVO;
import com.wannabe.be.event.service.EventServiceImpl;

@Controller
public class EventController extends CommonUtil {
	
	@Autowired
	private EventServiceImpl eventService;


	@GetMapping("/event/eventRegistForm")
	public String eventResistForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "/event/eventRegistForm";
	}
	
	@PostMapping("/event/eventRegist")
	public String eventRegist(EventVO eventVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		return "/event/eventRegistForm";
	}
}
