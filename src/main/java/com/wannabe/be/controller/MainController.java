package com.wannabe.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wannabe.be.product.service.ProductService;

@Controller
public class MainController {
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = {"/", "/main"})
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "파라미터 입력") String name, Model model) {
        model.addAttribute("name", name);
    	model.addAttribute("productList", productService.getlist());
        return "index";
    }
	

}
