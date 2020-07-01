package com.ls.juzimi.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.ls.juzimi.springboot.pojo.Review;

public interface ReviewService {

	//添加评论
	public Integer add(Review review);
	
	public Review findReviewById(Integer id);
	
	public Integer updateReviewByContent(Review review);
	
	//查找用户发布的评论
	public Page<Review> findReviewAllPageAndSortByUid(Integer page, Integer size, Direction dir, String pro, Integer uid);
	
	//查找该句子的评论
	public Page<Review> findReviewAllPageAndSortBySid(Integer page, Integer size, Direction dir, String pro, Integer sid);
	
	//删除评论
	public void delReviewById(Integer id);
	//取消评论
	public Integer delReviewBySidAndByUid(Integer sid, Integer uid);

	//获取name用户的最近10条评论
	public List<Review> findReviewByRecentDevelopments(String name);
}
