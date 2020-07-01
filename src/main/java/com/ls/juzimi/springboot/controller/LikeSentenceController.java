package com.ls.juzimi.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ls.juzimi.springboot.pojo.LikeSentence;
import com.ls.juzimi.springboot.service.LikeSentenceService;

@Controller
@RequestMapping("/juzimi/like_sentence")
public class LikeSentenceController {

	@Autowired
	private LikeSentenceService lss;
	
	/**
	 * -喜欢该句子
	 * 
	 * @param model
	 * @param sid
	 * @param uid
	 * @return
	 */
	@RequestMapping("/likeSentence")
	private String likeSentence(Model model, Integer sid, Integer uid) {
		model.addAttribute("status", this.lss.add(sid, uid));
		return "result";
	}
	
	/**
	 * 
	 * @param model
	 * @param start
	 * @param size
	 * @param pro
	 * @param uid
	 * @param sid
	 * @return
	 */
	@RequestMapping("/admin_like_sentence")
	private String findLikeSentenceAll(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "pro", defaultValue = "id") String pro,
			@RequestParam(value = "uid", required = false) Integer uid,
			@RequestParam(value = "sid", required = false) Integer sid) {
		Page<LikeSentence> page = null;
		if(uid != null) {			//根据用户查喜欢的句子
			page = this.lss.findLikeSentenceAllPageAndSortByUid(start, size, Direction.ASC, pro, uid);
			model.addAttribute("uid", uid);
		} else if(sid != null) {	//根据句子查已喜欢的用户
			page = this.lss.findLikeSentenceAllPageAndSortBySid(start, size, Direction.ASC, pro, sid);
			model.addAttribute("sid", sid);
		} else {					//查所有喜欢句子操作
			//随便选一个查找方式，后面的sid为空则查找全部操作
			page = this.lss.findLikeSentenceAllPageAndSortBySid(start, size, Direction.ASC, pro, sid);
			model.addAttribute("uid", uid);
			model.addAttribute("sid", sid);
		}
		model.addAttribute("page", page);
		model.addAttribute("prop", pro);
		return "like_sentence/adminLikeSentence";
	}
	
	@RequestMapping("/delLikeSentence")
	private String delLikeSentence(Integer id) {
		this.lss.delLikeSentenceById(id);
		return "redirect:/juzimi/like_sentence/admin_like_sentence";
	}
}
