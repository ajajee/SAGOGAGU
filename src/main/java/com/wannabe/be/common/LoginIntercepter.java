package com.wannabe.be.common;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginIntercepter implements HandlerInterceptor {

	public List<String> loginEssential = Arrays.asList("/product/productform","/post/**", "/comment/**", "/category/**", "/member/manage/**",
			"/main/edit/**");

	public List<String> loginInessential = Arrays.asList("/post/board/**", "/product/**", "/post/like/**");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Boolean logon = (Boolean) request.getSession().getAttribute("isLogOn");

		if (logon != null) {
			return true;
		}

		else {
			String destUri = request.getRequestURI();
			String destQuery = request.getQueryString();
			String dest = (destQuery == null) ? destUri : destUri + "?" + destQuery;
			request.getSession().setAttribute("dest", dest);

			response.sendRedirect("/member/loginform");
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}