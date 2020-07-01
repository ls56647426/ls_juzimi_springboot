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

import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.service.SentenceSetService;

@Controller
@RequestMapping("/juzimi/sentence_set")
public class SentenceSetController {
	
	@Autowired
	private SentenceSetService sss;
	
	/**
	 * -页面跳转：发布句集
	 * 
	 * @param sentenceSet
	 * @return
	 */
	@RequestMapping("/publishSentenceSet")
	public String showPage(@ModelAttribute("sentenceSet") SentenceSet sentenceSet) {
		return "sentence_set/publishSentenceSet";
	}
	
	/**
	 * -发布句集
	 * @Valid			开启对SentenceSet对象的数据校验
	 * BindingResult	封装了校验的结果
	 * 
	 * @param model
	 * @param sentenceSet
	 * @param result
	 * @return
	 */
	@RequestMapping("/addSentenceSet")
	public String addSentenceSet(Model model, @Valid SentenceSet sentenceSet, BindingResult result) {
		//数据校验
		if(result.hasErrors()) {
			return "sentence_set/publishSentenceSet";
		}
		
		model.addAttribute("status", this.sss.addSentenceSet(sentenceSet));
		return "result";
	}
	
	/**
	 * -句集管理
	 */
	@RequestMapping("/admin_sentence_set")
	public String findSentenceSetAll(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "9") int size,
			@RequestParam(value = "pro", defaultValue = "id") String pro){
		Page<SentenceSet> page = this.sss.findSentenceSetAllPageAndSort(start, size, Direction.ASC, pro);
		
		model.addAttribute("page", page);
		model.addAttribute("prop", pro);
		return "sentence_set/adminSentenceSet";
	}
	
	/**
	 * -根据句集id查询句集
	 */
	@RequestMapping("/findSentenceSetById")
	public String findSentenceSetById(Integer id,Model model){
		model.addAttribute("sentenceSet", this.sss.findSentenceSetById(id));
		return "sentence_set/updateSentenceSet";
	}
	
	/**
	 * -更新句集
	 */
	@RequestMapping("/updateSentenceSet")
	public String updateSentenceSet(Model model, @Valid SentenceSet sentenceSet, BindingResult result){
		if(result.hasErrors()) {
			return "redirect:/juzimi/sentence_set/findSentenceSetById?id=" + sentenceSet.getId();
		}
		
		model.addAttribute("status", this.sss.updateSentenceSet(sentenceSet));
		return "result";
	}
	
	/**
	 * -删除用户
	 */
	@RequestMapping("/delSentenceSet")
	public String delSentenceSet(Integer id){
		this.sss.deleteSentenceSetById(id);
		return "redirect:/juzimi/sentence_set/admin_sentence_set";
	}
}
