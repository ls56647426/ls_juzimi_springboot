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

import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.service.SentenceService;

@Controller
@RequestMapping("/juzimi/sentence")
public class SentenceController {
	@Autowired
	private SentenceService ss;
	
	/**
	 * -页面跳转：发布句子
	 * 
	 * @param sentence
	 * @return
	 */
	@RequestMapping("/publishSentence")
	public String showPage(@ModelAttribute("sentence") Sentence sentence) {
		return "sentence/publishSentence";
	}
	
	/**
	 * -发布句子
	 * @Valid			开启对Sentence对象的数据校验
	 * BindingResult	封装了校验的结果
	 * 
	 * @param model
	 * @param sentence
	 * @param result
	 * @return
	 */
	@RequestMapping("/addSentence")
	public String addSentence(Model model, @Valid Sentence sentence, BindingResult result) {
		//数据校验
		if(result.hasErrors()) {
			return "sentence/publishSentence";
		}
		
		model.addAttribute("status", this.ss.addSentence(sentence));
		return "result";
	}
	
	/**
	 * -句子管理
	 */
	@RequestMapping("/admin_sentence")
	public String findSentenceAll(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "pro", defaultValue = "id") String pro){
		Page<Sentence> page = this.ss.findSentenceAllPageAndSort(start, size, Direction.ASC, pro);
		model.addAttribute("page", page);
		model.addAttribute("prop", pro);
		return "sentence/adminSentence";
	}
	
	/**
	 * -根据句子 id查询句子
	 */
	@RequestMapping("/findSentenceById")
	public String findSentenceById(Integer id,Model model){
		model.addAttribute("sentence", this.ss.findSentenceById(id));
		return "sentence/updateSentence";
	}
	
	/**
	 * -更新句子
	 */
	@RequestMapping("/updateSentence")
	public String updateSentence(Model model, @Valid Sentence sentence, BindingResult result){
		if(result.hasErrors()) {
			return "redirect:/juzimi/sentence/findSentenceById?id=" + sentence.getId();
		}
		
		model.addAttribute("status", this.ss.updateSentence(sentence));
		return "result";
	}
	
	/**
	 * -删除句子
	 */
	@RequestMapping("/delSentence")
	public String delSentence(Integer id){
		this.ss.deleteSentenceById(id);
		return "redirect:/juzimi/sentence/admin_sentence";
	}
}
