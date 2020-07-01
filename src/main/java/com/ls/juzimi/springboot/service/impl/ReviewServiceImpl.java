package com.ls.juzimi.springboot.service.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ls.juzimi.springboot.dao.ReviewDao;
import com.ls.juzimi.springboot.dao.SentenceDao;
import com.ls.juzimi.springboot.pojo.Review;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewDao rd;
	
	@Autowired
	private SentenceDao sd;
	
	/**
	 * 添加评论
	 */
	@Override
	@CacheEvict(value = "review", allEntries = true)
	public Integer add(Review review) {
		review.getSentence().setHotToday(review.getSentence().getHotToday() + 1);	//评论一次热度+1
		this.sd.save(review.getSentence());
		this.rd.save(review);
		return 0;
	}
	
	/**
	 * 根据id查找评论
	 */
	@Override
	@Cacheable(value = "review")
	public Review findReviewById(Integer id) {
		return this.rd.findOne(id);
	}
	
	/**
	 * 更新评论
	 */
	@Override
	@CacheEvict(value = "review", allEntries = true)
	public Integer updateReviewByContent(Review review) {
		//查询条件：本次评论内容
		Specification<Review> specContent = new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("content"), review.getContent()),
						cb.equal(root.get("id"), review.getId()));
			}
		};
		
		if(this.rd.findOne(specContent) != null) {
			System.out.println("评论内容与上次相同！");
			return 0;		//评论与上次相同不用报错，只不改变数据库内容即可。
		} else {
			this.rd.save(review);
			return 0;
		}
	}
	
	//功能：排序+分页查找用户发布的评论
	@Override
	@Cacheable(value = "review")
	public Page<Review> findReviewAllPageAndSortByUid(Integer page, Integer size, Direction dir,
			String pro, Integer uid) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		
		Page<Review> page1;
		if(uid != null) {		//如果有 uid，则查询该用户的情况
			//查询条件：用户id
			Specification<Review> spec = new Specification<Review>() {
				@Override
				public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("user").get("id"), uid);
				}
			};
			page1 = this.rd.findAll(spec, pageable);
		} else {				//否则，查询所有用户的情况
			page1 = this.rd.findAll(pageable);
		}
		
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
	
	//功能：排序+分页查找该句子的评论
	@Override
	@Cacheable(value = "review")
	public Page<Review> findReviewAllPageAndSortBySid(Integer page, Integer size, Direction dir,
			String pro, Integer sid) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		
		Page<Review> page1;
		if(sid != null) {		//如果有 sid，则查询该句子的情况
			//查询条件：用户id
			Specification<Review> spec = new Specification<Review>() {
				@Override
				public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("sentence").get("id"), sid);
				}
			};
			page1 = this.rd.findAll(spec, pageable);
		} else {				//否则，查询所有句子的情况
			page1 = this.rd.findAll(pageable);
		}
		
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
	
	//删除评论
	@Override
	@CacheEvict(value = "like_sentence", allEntries = true)
	public void delReviewById(Integer id) {
		this.rd.deleteReviewById(id);
	}
	
	//取消评论
	@Override
	@CacheEvict(value = "like_sentence", allEntries = true)
	public Integer delReviewBySidAndByUid(Integer sid, Integer uid) {
		Sentence sentence = this.sd.findOne(sid);
		if(sentence == null) {
			System.out.println("没有该句子！");
			return 999;
		}
		
		//查询条件：句子id和用户id
		Specification<Review> specSidAndUid = new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("sentence").get("id"), sid),
						cb.equal(root.get("user").get("id"), uid));
			}
		};
		Review review = this.rd.findOne(specSidAndUid);
		if(review == null) {
			System.out.println("该用户未评论该句子！");
			return 999;
		}
		this.rd.deleteReviewById(review.getId());
		
		sentence.setHotToday(sentence.getHotToday()-1);		//取消一个评论，热度-1
		this.sd.save(sentence);
		return 0;
	}
	
	/**
	 * -获取该用户最近发布10条评论
	 */
	@Override
	@Cacheable(value = "review")
	public List<Review> findReviewByRecentDevelopments(String name) {
		//查询条件：句子id和用户id
		Specification<Review> specUserName = new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("user").get("name"), name);
			}
		};
		
		Sort sort = new Sort(new Order(Direction.DESC, "publishDate"));
		Pageable pageable = new PageRequest(0, 10, sort);
		Page<Review> page = this.rd.findAll(specUserName, pageable);
		
		return page.getContent();
	}
}
