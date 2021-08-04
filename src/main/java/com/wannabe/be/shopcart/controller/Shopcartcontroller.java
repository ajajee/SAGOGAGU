package com.wannabe.be.shopcart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wannabe.be.member.vo.MemberVO;
import com.wannabe.be.shopcart.service.ShopcartService;
import com.wannabe.be.shopcart.vo.ShopcartVO;

@Controller
public class Shopcartcontroller {
	
	@Autowired
	private ShopcartService shopcartService;
	
	@Autowired
	private ShopcartVO shopcartVO;
	
	@Autowired
	private MemberVO memberVO;
	
	
	
	/* 카트에 아이템 추가 */
	@GetMapping(value = "/product/addtoCart")
	public void addItemToCart(@RequestParam("product_no") int product_no, @RequestParam(value = "totalPrice", required = false) String totalPrice, @RequestParam("member_id") String member_id,
			@RequestParam("cart_product_quantity") int cart_product_quantity, HttpServletRequest request,
			HttpServletResponse response) {
		ShopcartVO shopcartVO = new ShopcartVO();
		shopcartVO.setMember_id(member_id);
		shopcartVO.setProduct_no(product_no);
		shopcartVO.setCart_product_quantity(cart_product_quantity);
		shopcartService.addItemToCart(shopcartVO);
	}
	
	
	
	
	@RequestMapping("/product/mycart")
	public String shopcart(MemberVO memberVO,  Model model, HttpSession session) throws Exception {
		
		
		memberVO = (MemberVO) session.getAttribute("memberInfo");
		System.out.println("memberVO >>" + memberVO);
		
		String member_id = memberVO.getMember_id();
		
		List<ShopcartVO> list = null;
		list = shopcartService.list(member_id);
		
		model.addAttribute("mycart", list);
		System.out.println("list >>" + list);
	
		return "product/mycart";
	}
	
	@RequestMapping("/product/modifycart")
	public String modifycart(ShopcartVO shopcartVO, int cart_product_quantity, RedirectAttributes redirect)  throws Exception {
		
		System.out.println("shopcartVO >> " + shopcartVO);
		
		int cpq = shopcartVO.getCpq();
		
		shopcartVO.setCart_product_quantity(cpq);
		
		int result = shopcartService.modifycart(shopcartVO);
		
		System.out.println("cpq2 >>" + shopcartVO.getCart_product_quantity());
	
		redirect.addAttribute("member_id", shopcartVO.getMember_id());
		
		return "redirect:/product/mycart";

	}
	

	@RequestMapping("/product/deletecart")
	public String deletecart(ShopcartVO shopcartVO, RedirectAttributes redirect) throws Exception {
	
		shopcartService.deletecart(shopcartVO);
		System.out.println("delete_result >>>> "+ shopcartVO.getMember_id() + shopcartVO.getCart_no());
		redirect.addAttribute("member_id", shopcartVO.getMember_id());
	
		return "redirect:/productmycart";
	}

}
