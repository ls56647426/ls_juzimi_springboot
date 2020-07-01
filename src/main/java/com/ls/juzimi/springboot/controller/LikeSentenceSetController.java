package com.ls.juzimi.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ls.juzimi.springboot.pojo.LikeSentenceSet;
import com.ls.juzimi.springboot.service.LikeSentenceSetService;

@Controller
@RequestMapping("/juzimi/like_sentence_set")
public class LikeSentenceSetController {

	@Autowired
	private LikeSentenceSetService lsss;
	
	/**
	 * 喜欢该句集
	 * 
	 * @param model
	 * @param ssid
	 * @param uid
	 * @return
	 */
	@RequestMapping("/likeSentenceSet")
	private String likeSentenceSet(Model model, Integer ssid, Integer uid) {
		model.addAttribute("status", this.lsss.add(ssid, uid));
		return "result";
	}
	
	@RequestMapping("/admin_like_sentence_set")
	private String findLikeSentenceAll(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "pro", defaultValue = "id") String pro,
			@RequestParam(value = "uid", required = false) Integer uid,
			@RequestParam(value = "ssid", required = false) Integer ssid) {
		Page<LikeSentenceSet> page = null;
		if(uid != null) {			//根据用户查喜欢的句集
			page = this.lsss.findLikeSentenceSetAllPageAndSortByUid(start, size, Direction.ASC, pro, uid);
			model.addAttribute("uid", uid);
		} else if(ssid != null) {	//根据句集查已喜欢的用户
			page = this.lsss.findLikeSentenceSetAllPageAndSortBySsid(start, size, Direction.ASC, pro, ssid);
			model.addAttribute("ssid", ssid);
		} else {					//查所有喜欢句集操作
			//随便选一个查找方式，后面的ssid为空则查找全部操作
			page = this.lsss.findLikeSentenceSetAllPageAndSortBySsid(start, size, Direction.ASC, pro, ssid);
			model.addAttribute("uid", uid);
			model.addAttribute("ssid", ssid);
		}
		model.addAttribute("page", page);
		model.addAttribute("prop", pro);
		return "like_sentence_set/adminLikeSentenceSet";
	}
	
	@RequestMapping("/delLikeSentenceSet")
	private String delLikeSentenceSet(Integer id) {
		this.lsss.delLikeSentenceSetById(id);
		return "redirect:/juzimi/like_sentence_set/admin_like_sentence_set";
	}
}
