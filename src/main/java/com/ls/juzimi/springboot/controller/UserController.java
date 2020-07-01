package com.ls.juzimi.springboot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.UserService;

@Controller
@RequestMapping("/juzimi/user")
public class UserController {
	
	@Autowired
	private UserService us;
	
	/**
	 * -页面跳转：注册账号
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/register")
	public String showPage(@ModelAttribute("user") User user) {
		return "user/register";
	}
	
	/**
	 * -注册账号
	 * @Valid			开启对Users对象的数据校验
	 * BindingResult	封装了校验的结果
	 * 
	 * @param user
	 * @param result
	 * @return
	 */
	@RequestMapping("/addUser")
	public String addUser(Model model, @Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			return "user/register";
		}
		model.addAttribute("status", this.us.addUser(user));
		return "result";
	}
	
	/**
	 * -用户管理
	 */
	@RequestMapping("/admin_user")
	public String findUserAll(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "9") int size,
			@RequestParam(value = "pro", defaultValue = "id") String pro){
		Page<User> page = this.us.findUserAllPageAndSort(start, size, Direction.ASC, pro);
		model.addAttribute("page", page);
		model.addAttribute("prop", pro);
		return "user/adminUser";
	}
	
	/**
	 * -根据用户 id查询用户
	 */
	@RequestMapping("/findUserById")
	public String findUserById(Integer id,Model model){
		model.addAttribute("user", this.us.findUserById(id));
		return "user/updateUser";
	}

	/**
	 * -更新用户
	 */
	@RequestMapping("/updateUser")
	public String updateUser(Model model, @Valid User user, BindingResult result){
		if(result.hasErrors()) {
			return "redirect:/juzimi/user/findUserById?id=" + user.getId();
		}
		model.addAttribute("status", this.us.updateUserPasswordByName(user));
		return "result";
	}

	/**
	 * -删除用户
	 */
	@RequestMapping("/delUser")
	public String delUser(Integer id){
		this.us.deleteUserById(id);
		return "redirect:/juzimi/user/admin_user";
	}

}
