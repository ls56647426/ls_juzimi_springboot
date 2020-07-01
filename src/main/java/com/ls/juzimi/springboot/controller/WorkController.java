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

import com.ls.juzimi.springboot.pojo.Work;
import com.ls.juzimi.springboot.service.WorkService;

@Controller
@RequestMapping("/juzimi/work")
public class WorkController {

	@Autowired
	private WorkService ws;
	
	/**
	 * -页面跳转：发布作品
	 * 
	 * @param work
	 * @return
	 */
	@RequestMapping("/publishWork")
	public String showPage(@ModelAttribute("work") Work work) {
		return "work/publishWork";
	}
	
	/**
	 * -发布作品
	 * @param model
	 * @param work
	 * @param result
	 * @return
	 */
	@RequestMapping("/addWork")
	public String addWork(Model model, @Valid Work work, BindingResult result) {
		//数据校验
		if(result.hasErrors()) {
			return "work/publishWork";
		}
		
		model.addAttribute("status", this.ws.addWork(work));
		return "result";
	}
	
	/**
	 * -作品管理
	 */
	@RequestMapping("/admin_work")
	public String findWorkAll(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "9") int size,
			@RequestParam(value = "pro", defaultValue = "id") String pro){
		Page<Work> page = this.ws.findWorkAllPageAndSort(start, size, Direction.ASC, pro);
		
		model.addAttribute("page", page);
		model.addAttribute("prop", pro);
		return "work/adminWork";
	}
	
	/**
	 * -根据作品id查询作品
	 */
	@RequestMapping("/findWorkById")
	public String findWorkById(Integer id,Model model){
		model.addAttribute("work", this.ws.findWorkById(id));
		return "work/updateWork";
	}
	
	/**
	 * -更新作品
	 */
	@RequestMapping("/updateWork")
	public String updateWork(Model model, @Valid Work work, BindingResult result){
		if(result.hasErrors()) {
			return "redirect:/juzimi/work/findWorkById?id=" + work.getId();
		}
		
		model.addAttribute("status", this.ws.updateWork(work));
		return "result";
	}
	
	/**
	 * -删除作品
	 */
	@RequestMapping("/delWork")
	public String delSentenceSet(Integer id){
		this.ws.deleteWorkById(id);
		return "redirect:/juzimi/work/admin_work";
	}
}
