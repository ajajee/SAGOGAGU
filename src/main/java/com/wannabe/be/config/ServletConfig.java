package com.wannabe.be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wannabe.be.common.LoginIntercepter;

@Configuration
public class ServletConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/upload/**").addResourceLocations("file:///c:/upload/");

	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		LoginIntercepter loginIntercepter = new LoginIntercepter();
		
		registry
			.addInterceptor(loginIntercepter)
			.addPathPatterns(loginIntercepter.loginEssential);
			/*.excludePathPatterns(loginIntercepter.loginInessential);*/
	}

}
