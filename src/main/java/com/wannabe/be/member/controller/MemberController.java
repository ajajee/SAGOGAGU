package com.wannabe.be.member.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysql.cj.log.Log;
import com.wannabe.be.member.service.MemberService;
import com.wannabe.be.member.vo.MemberVO;
import com.wannabe.be.product.service.ProductService;
import com.wannabe.be.product.vo.ProductAttachVO;
import com.wannabe.be.product.vo.ProductVO;
import com.wannabe.be.product.vo.PurchaseHistoryVO;
import com.wannabe.be.reviewboard.vo.ReviewImageFileVO;

@Controller
public class MemberController  {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private MemberVO memberVO;

	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping("/member/memberform")
	public String memberform(@RequestParam(value = "name", required = false, defaultValue = "파라미터 입력") String name,
			Model model) {
		model.addAttribute("name", name);
		return "member/memberform";
	}

	
	
	/*주문 히스토리 불러 */ 
	@RequestMapping("/member/viewHistory")
	public String viewHistory (Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		memberVO = (MemberVO) session.getAttribute("memberVO");
	
		String member_id = memberVO.getMember_id();
		List<ProductAttachVO> productImageList = new ArrayList<>();
		List<PurchaseHistoryVO> historyList = productService.getHistory(member_id);
		for (PurchaseHistoryVO purchase : historyList) {
			
			int product_no = purchase.getProduct_no();
			ProductVO product = productService.getProductDetailsByProduct_no(product_no);
			String product_title = product.getProduct_title();
			purchase.setProduct_title(product_title);
			productImageList.addAll(productService.getProductImageByProductNo(product_no));
			
		}
		model.addAttribute("historyList", historyList);
		model.addAttribute("productImageList", productImageList);
		System.out.println(historyList);

		return "/product/purchaseHistoryForm";
	
	}
	
	
	@RequestMapping("/member/myinformation")
	public String myinformation(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("controller >>> myinformation>>> ");
		HttpSession session = request.getSession();
		session.setAttribute("memberVO", memberVO);
//		HttpSession session = request.getSession();
//		memberVO = (MemberVO) session.getAttribute("memberInfo");
//		String member_id = memberVO.getMember_id();
//		MemberVO member = memberService.selectOnemember(member_id);
//		
//		model.addAttribute("member", member);
//		model.addAttribute("member_id", member_id);
		

		return "member/myinformation";
	}

	@GetMapping("/member/loginform")
	public String loginform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "member/loginform";
	}

	@PostMapping("/member/login")
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		memberVO = memberService.login(loginMap);
		
		System.out.println(memberVO.getMember_pw());
		System.out.println(passwordEncoder.matches(loginMap.get("member_pw"), memberVO.getMember_pw()));
		
		System.out.println("member_id >>> " + memberVO.getMember_id());
	
		if (memberVO != null && passwordEncoder.matches(loginMap.get("member_pw"), memberVO.getMember_pw())) {
			session.setAttribute("isLogOn", true);
			session.setAttribute("memberInfo", memberVO);
			session.setMaxInactiveInterval(40*60);
			
			  String dest = (String) session.getAttribute("dest");
		      String redirect = (dest == null) ? "/" : dest;
//		      log.info("redirect dest >>> " + redirect);
		      session.removeAttribute("dest");
		   
			mav.setViewName("redirect:" + redirect); 
			
			System.out.println("member >> memebrVO >> " + memberVO );
			
			if(memberVO.getMember_id().equals("admin")) {
				session.setAttribute("isadmin", true);
			} 
				
		} else {
			mav.setViewName("/member/loginform");
		} 
		return mav;
	}

	@GetMapping("/member/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		session.removeAttribute("isLogOn");
		session.removeAttribute("memberInfo");
		session.invalidate();
		mav.setViewName("redirect:/");
		return mav;
	}

	@PostMapping("/member/insertmember")
	public ModelAndView insertmember(MemberVO memberVO) throws Exception {
		ModelAndView mav = new ModelAndView();

		// spring security
		String encPassword = passwordEncoder.encode(memberVO.getMember_pw());
		memberVO.setMember_pw(encPassword);

		memberService.insertmember(memberVO);
		mav.setViewName("redirect:/main");
		return mav;
	}
	

	@RequestMapping("/member/modifymemberform")
	public String selectonemember(HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
		
		HttpSession session = request.getSession();
		memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		MemberVO member = memberService.selectOnemember(member_id);
		
		model.addAttribute("member", member);
		model.addAttribute("member_id", member_id);

		return "member/modifymemberform";

	}
	
	@PostMapping("/member/modifymember")
	public String modifymember(@ModelAttribute("memberVO") MemberVO memberVO, RedirectAttributes redirect,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("controller1 >>>> memberVO >>" + memberVO);
		HttpSession session = request.getSession();
		
		int result = memberService.modifymember(memberVO);
		System.out.println("controller >>>> modify_result >>>> " + result);
		
		memberVO = memberService.selectOnemember(memberVO.getMember_id());
		System.out.println("controller2 >>>> memberVO >>" + memberVO);
		session.setAttribute("memberInfo", memberVO);
		
//		redirect.addAttribute("member", memberVO);
//		redirect.addAttribute("member_id", memberVO.getMember_id());
		return "member/myinformation";
	}
	
	@GetMapping("/member/modifyPwForm")
	public String modifyPwForm() {
		return "member/modifyPw";
		
	}
	
	
	@PostMapping("/member/modifyPw")
	public String modifypw(@RequestParam("member_pw") String member_pw, HttpSession session, Model model)
			throws Exception {
		
		memberVO = (MemberVO) session.getAttribute("memberInfo");
//		String member_id = memberVO.getMember_id();
//		MemberVO member = memberService.selectOnemember(member_id);
		
//		String pw = memberVO.getMember_pw();
//		System.out.println("pw >>" + pw);
		System.out.println("modi controller1 >>>> memberVO >>" + memberVO);
		String encPassword = passwordEncoder.encode(member_pw);
		memberVO.setMember_pw(encPassword);
		System.out.println("modi controller2 >>>> memberVO >>" + memberVO);
		int result = memberService.modifyPw(memberVO);
		System.out.println("cont >>> result>>>" + result);
		model.addAttribute("result", result);

		return "member/myinformation";

	}


	@RequestMapping("/member/deletememberform")
	public String deletememberform(RedirectAttributes redirect,HttpServletRequest request, HttpServletResponse response) {
		return "member/deletememberform";
	}
	
	@RequestMapping("/member/deletemember")
	public String deletemember(RedirectAttributes redirect,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		int result = memberService.deletemember(member_id);
		System.out.println("delete_result >>>> " + result);
		session.removeAttribute("isLogOn");
		session.removeAttribute("memberInfo");
		return "redirect:/";
	}

	@PostMapping("/member/addMemberCheck")
	public void addMemberCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");

		String memberID = request.getParameter("memberID");
		int result = memberService.addMemberCheck(memberID);

		System.out.println(result);
		if (memberID.equals("") || memberID == null) {
			result = 1;
		}

		response.getWriter().write(result + "");
	}

}
