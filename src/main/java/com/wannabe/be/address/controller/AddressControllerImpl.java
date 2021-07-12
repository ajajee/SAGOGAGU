package com.wannabe.be.address.controller;


import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wannabe.be.address.service.AddressService;
import com.wannabe.be.address.vo.AddressVO;

@Controller

public class AddressControllerImpl {

	@Autowired
	private AddressService addressService;

	@RequestMapping("/member/addressform")
	public String addressform(@RequestParam("member_id") String member_id, Model model ) throws Exception {
		System.out.println("member_id1 >>>" + member_id );
		model.addAttribute("member_id", member_id);
		return "addressform";
	}
	
	@RequestMapping("/member/myaddressform")
	public String myaddress(@RequestParam("member_id") String member_id, Model model) throws Exception {
		
		List<AddressVO> list = null;
		list = addressService.list(member_id);
		
		model.addAttribute("myaddress", list);
	
		return "myaddressform";
	}

	
	@RequestMapping("/member/insertaddress")
	public String insertaddress(@RequestParam("member_id") String member_id, AddressVO addressVO, Model model) throws Exception {
		
		System.out.println("member_id2 >>> "+ member_id);
		System.out.println("addressVO >>>" + addressVO);
		addressService.insertaddress(addressVO);
	
		model.addAttribute("address", addressVO);
		
		return "redirect:/member/myinformation";
	}
	
	@RequestMapping("/member/modifyaddressform")
	public String modifyaddress(@RequestParam Map addr_map, Model model) throws Exception {
		System.out.println("addr_map >>>  " + addr_map);
		AddressVO address = addressService.selectOneAddress(addr_map);
		model.addAttribute("address", address);
		model.addAttribute("member_id", addr_map.get("member_id"));
		
		return "modifyaddressform";

	}
	
	@PostMapping("/member/modifyaddress")
	public String modifyaddress(@ModelAttribute("addressVO") AddressVO addressVO, RedirectAttributes redirect) throws Exception {
		int result = addressService.modifyaddress(addressVO);
		System.out.println("modify_result >>>> " + result);
		redirect.addAttribute("member_id", addressVO.getMember_id());
		return "redirect:/member/myaddressform";

	}
	
	@RequestMapping("/member/deleteaddress")
	public String deleteaddress(@RequestParam("member_id") String member_id, @RequestParam("address_id") int address_id, RedirectAttributes redirect) throws Exception {
	
		addressService.deleteaddress(address_id);
		System.out.println("delete_result >>>> " + address_id);
		redirect.addAttribute("member_id", member_id);
	
		return "redirect:/member/myaddressform";
	}
	
	
}

