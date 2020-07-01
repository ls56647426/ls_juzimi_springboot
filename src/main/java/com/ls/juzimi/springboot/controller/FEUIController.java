package com.ls.juzimi.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/juzimi/")
public class FEUIController {

	@RequestMapping("/majorNav")
	public String showMajorNav() {
		return "FEUI/majorNav";
	}
	
	@RequestMapping("/profile")
	public String showProfile() {
		return "FEUI/profile";
	}
	
	@RequestMapping("/southStatement")
	public String showSouthStatement() {
		return "FEUI/southStatement";
	}
	
	@RequestMapping("/basicData")
	public String showBasicData() {
		return "FEUI/basicData";
	}
}
