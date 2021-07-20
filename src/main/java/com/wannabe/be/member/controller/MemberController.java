package com.wannabe.be.member.controller;

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

import com.wannabe.be.member.service.MemberService;
import com.wannabe.be.member.vo.MemberVO;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberVO memberVO;

	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping("/member/memberform")
	public String memberform(@RequestParam(value = "name", required = false, defaultValue = "파라미터 입력") String name,
			Model model) {
		model.addAttribute("name", name);
		return "/member/memberform";
	}

	@GetMapping("/member/myinformation")
	public String myinformation(Model model) {

		return "myinformation";
	}

	@GetMapping("/member/loginform")
	public String loginform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "/member/loginform";
	}

	@PostMapping("/member/login")
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		memberVO = memberService.login(loginMap);
		System.out.println(memberVO.getMember_pw());

		if (memberVO != null && passwordEncoder.matches(loginMap.get("member_pw"), memberVO.getMember_pw())) {
			session.setAttribute("isLogOn", true);
			session.setAttribute("memberInfo", memberVO);
			mav.setViewName("redirect:/main");
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

		return "/member/modifymemberform";

	}

	@PostMapping("/member/modifymember")
	public String modifymember(@ModelAttribute("memberVO") MemberVO memberVO, RedirectAttributes redirect,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		
		int result = memberService.modifymember(memberVO);
		System.out.println("modify_result >>>> " + result);
		memberVO = memberService.selectOnemember(memberVO.getMember_id());
		session.setAttribute("memberInfo", memberVO);
		redirect.addAttribute("member_id", memberVO.getMember_id());
		return "/member/myinformation";

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
		return "redirect:/main";
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
