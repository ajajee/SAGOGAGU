package com.wannabe.be.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*@Configuration*/
public class ServletConfig implements WebMvcConfigurer{
	  @Override
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {

	    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	    registry.addResourceHandler("/upload/**").addResourceLocations("file:///c:/upload/");
	    
	  }
	  
}
