package com.ls.juzimi.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/juzimi")
public class HomepageController {

	/**
	 * -页面跳转：主页
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public String showPage() {
		return "FEUI/homepage";
	}
}
