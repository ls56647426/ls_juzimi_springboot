package com.ls.juzimi.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ls.juzimi.springboot.pojo.JoinSentenceSet;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.service.JoinSentenceSetService;

@Controller
@RequestMapping("/juzimi/join_sentence_set")
public class JoinSentenceSetController {

	@Autowired
	private JoinSentenceSetService jsss;

	/**
	 * 添加句子到指定句集
	 * 
	 * @param model
	 * @param sentence
	 * @param sentenceSet
	 * @return
	 */
	@RequestMapping("/addSentenceToSentenceSet")
	private String addSentenceToSentenceSet(Model model, Sentence sentence, SentenceSet sentenceSet) {
		model.addAttribute("status", this.jsss.add(sentence, sentenceSet));
		return "result";
	}

	/**
	 * 
	 * @param model
	 * @param start
	 * @param size
	 * @param pro
	 * @param ssid
	 * @return
	 */
	@RequestMapping("/admin_join_sentence_set")
	private String findJoinSentenceSetAll(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "pro", defaultValue = "id") String pro,
			@RequestParam(value = "ssid", required = false) Integer ssid) {
		Page<JoinSentenceSet> page = this.jsss.findJoinSentenceSetAllPageAndSort(start, size, Direction.ASC, pro, ssid);
		System.out.println(ssid);
		model.addAttribute("page", page);
		model.addAttribute("prop", pro);
		if(ssid != null) {
			model.addAttribute("ssid", ssid);
		}
		return "join_sentence_set/adminJoinSentenceSet";
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delJoinSentenceSet")
	private String delJoinSentenceSet(Integer id) {
		this.jsss.deleteJoinSentenceSetById(id);
		return "redirect:/juzimi/join_sentence_set/admin_join_sentence_set";
	}
}
