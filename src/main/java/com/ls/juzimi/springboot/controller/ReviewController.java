package com.ls.juzimi.springboot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ls.juzimi.springboot.pojo.Review;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.ReviewService;
import com.ls.juzimi.springboot.service.SentenceService;
import com.ls.juzimi.springboot.service.UserService;

@Controller
@RequestMapping("/juzimi/review")
public class ReviewController {

	@Autowired
	private ReviewService rs;
	
	@Autowired
	private UserService us;
	
	@Autowired
	private SentenceService ss;
	
	/**
	 * -页面跳转：发布评论
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/publishReview")
	public String publishReview(Model model, @RequestParam(value = "uid") Integer uid,
			@RequestParam(value = "sid") Integer sid) {
		//获取user和sentence实体
		User user = this.us.findUserById(uid);
		Sentence sentence = this.ss.findSentenceById(sid);
		//错误校验
		if(user == null || sentence == null) {
			System.out.println("数据错误！");
			model.addAttribute("status", 14);
			return "result";
		}
		//实体关联映射
		Review review = new Review();
		
		review.setUser(user);
		user.getReviews().add(review);
		
		review.setSentence(sentence);
		sentence.getReviews().add(review);
		
		model.addAttribute("review", review);
		return "review/addReview";
	}
	
	/**
	 * 发布评论
	 * 
	 * @param model
	 * @param sid
	 * @param uid
	 * @return
	 */
	@RequestMapping("/addReview")
	private String addReview(Model model, @Valid Review review, BindingResult result) {
		if(result.hasErrors()) {
			return "review/publishReview";
		}
		model.addAttribute("status", this.rs.add(review));
		return "result";
	}
	
	/**
	 * -根据评论 id查询评论
	 */
	@RequestMapping("/findReviewById")
	public String findReviewById(Integer id,Model model){
		model.addAttribute("review", this.rs.findReviewById(id));
		return "review/updateReview";
	}
	
	/**
	 * -更新评论
	 */
	@RequestMapping("/updateReview")
	public String updateReview(Model model, @Valid Review review, BindingResult result){
		if(result.hasErrors()) {
			return "redirect:/juzimi/review/findReviewById?id=" + review.getId();
		}
		model.addAttribute("status", this.rs.updateReviewByContent(review));
		return "result";
	}
	
	/**
	 * 分页+排序查找评论
	 * @param model
	 * @param start
	 * @param size
	 * @param pro
	 * @param uid
	 * @param sid
	 * @return
	 */
	@RequestMapping("/admin_review")
	private String findReviewAll(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@RequestParam(value = "pro", defaultValue = "id") String pro,
			@RequestParam(value = "uid", required = false) Integer uid,
			@RequestParam(value = "sid", required = false) Integer sid) {
		Page<Review> page = null;
		if(uid != null) {			//查找用户发布的评论
			page = this.rs.findReviewAllPageAndSortByUid(start, size, Direction.ASC, pro, uid);
			model.addAttribute("uid", uid);
		} else if(sid != null) {	//查找该句子的评论
			page = this.rs.findReviewAllPageAndSortBySid(start, size, Direction.ASC, pro, sid);
			model.addAttribute("sid", sid);
		} else {					//查找所有评论
			//随便选一个查找方式，后面的sid为空则查找全部操作
			page = this.rs.findReviewAllPageAndSortBySid(start, size, Direction.ASC, pro, sid);
			model.addAttribute("uid", uid);
			model.addAttribute("sid", sid);
		}
		model.addAttribute("page", page);
		model.addAttribute("prop", pro);
		return "review/adminReview";
	}
	
	/**
	 * 删除评论
	 * @param id
	 * @return
	 */
	@RequestMapping("/delReview")
	private String delReview(Integer id) {
		this.rs.delReviewById(id);
		return "redirect:/juzimi/review/admin_review";
	}
}
