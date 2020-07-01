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

import com.ls.juzimi.springboot.pojo.Category;
import com.ls.juzimi.springboot.service.CategoryService;

@Controller
@RequestMapping("/juzimi/category")
public class CategoryController {

	@Autowired
	private CategoryService cs;
	
	/**
	 * -页面跳转：新建分类
	 * 
	 * @param category
	 * @return
	 */
	@RequestMapping("/newCategory")
	public String showPage(@ModelAttribute("category") Category category) {
		return "category/newCategory";
	}
	
	/**
	 * -新建分类
	 * @Valid			开启对Category对象的数据校验
	 * BindingResult	封装了校验的结果
	 * 
	 * @param model
	 * @param category
	 * @param result
	 * @return
	 */
	@RequestMapping("/addCategory")
	public String addCategory(Model model, @Valid Category category, BindingResult result) {
		if(result.hasErrors()) {
			return "category/newCategory";
		}
		model.addAttribute("status", this.cs.addCategory(category));
		return "result";
	}
	
	/**
	 * -分类管理
	 */
	@RequestMapping("/admin_category")
	public String findCategoryAll(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "9") int size,
			@RequestParam(value = "pro", defaultValue = "id") String pro){
		Page<Category> page = this.cs.findCategoryAllPageAndSort(start, size, Direction.ASC, pro);
		model.addAttribute("page", page);
		model.addAttribute("prop", pro);
		return "category/adminCategory";
	}
	
	/**
	 * -根据分类 id查询分类
	 */
	@RequestMapping("/findCategoryById")
	public String findCategoryById(Integer id,Model model){
		model.addAttribute("category", this.cs.findCategoryById(id));
		return "category/updateCategory";
	}
	
	/**
	 * -更新分类
	 */
	@RequestMapping("/updateCategory")
	public String updateCategory(Model model, @Valid Category category, BindingResult result){
		if(result.hasErrors()) {
			return "redirect:/juzimi/category/findCategoryById?id=" + category.getId();
		}
		model.addAttribute("status", this.cs.updateCategoryName(category));
		return "result";
	}
	
	/**
	 * -删除分类
	 */
	@RequestMapping("/delCategory")
	public String delCategory(Integer id){
		this.cs.deleteCategoryById(id);
		return "redirect:/juzimi/category/admin_category";
	}
}
