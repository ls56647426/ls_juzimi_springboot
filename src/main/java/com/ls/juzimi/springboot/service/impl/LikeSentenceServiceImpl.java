package com.ls.juzimi.springboot.service.impl;

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

import com.ls.juzimi.springboot.dao.LikeSentenceDao;
import com.ls.juzimi.springboot.dao.SentenceDao;
import com.ls.juzimi.springboot.dao.UserDao;
import com.ls.juzimi.springboot.pojo.LikeSentence;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.LikeSentenceService;

@Service
public class LikeSentenceServiceImpl implements LikeSentenceService {

	@Autowired
	private LikeSentenceDao lsd;
	
	@Autowired
	private SentenceDao sd;
	
	@Autowired
	private UserDao ud;
	
	/**
	 * 功能：喜欢该句子
	 * 返回值解释：
	 * 0：	喜欢成功
	 * 13：	喜欢失败，该句子已被喜欢
	 */
	@Override
	@CacheEvict(value = "like_sentence", allEntries = true)
	public Integer add(Sentence sentence, User user) {
		//检错
		if(sentence == null || user == null) {
			System.out.println("数据错误！");
			return 14;
		}
		
		//likeSentence与user和sentence映射：一对多
		//获取关联实体对象并关联
		LikeSentence likeSentence = new LikeSentence();
		
		likeSentence.setUser(user);
		user.getLikeSentences().add(likeSentence);
		
		likeSentence.setSentence(sentence);
		sentence.getLikeSentences().add(likeSentence);
		
		//查询条件：用户和句子id
		Specification<LikeSentence> specUserIDAndSenteceID = new Specification<LikeSentence>() {
			@Override
			public Predicate toPredicate(Root<LikeSentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(
					cb.equal(root.get("user").get("id"), user.getId()),
					cb.equal(root.get("sentence").get("id"), sentence.getId())
				);
			}
		};
		
		//判断该操作是否存在，如果不存在，则执行该操作；如果存在，则报错
		if(this.lsd.findOne(specUserIDAndSenteceID) != null) {
			System.out.println("该句子已被喜欢！");
			return 15;
		} else {
			sentence.setNumbersOfLike(sentence.getNumbersOfLike()+1);
			sentence.setHotToday(sentence.getHotToday()+10);
			this.sd.save(sentence);				//改变了句子的喜欢数和热度，级联操作也保存了likeSentence
			return 0;
		}
	}
	
	//重载add
	@Override
	@CacheEvict(value = {"like_sentence", "sentence"}, allEntries = true)
	public Integer add(Integer sid, Integer uid) {
		//获取sentence和user实体对象
		Sentence sentence = this.sd.findOne(sid);
		User user = this.ud.findOne(uid);
		
		//检错
		if(sentence == null || user == null) {
			System.out.println("数据错误！");
			return 14;
		}
		
		//likeSentence与user和sentence映射：一对多
		//获取关联实体对象并关联
		LikeSentence likeSentences = new LikeSentence();
		
		likeSentences.setUser(user);
		System.out.println("123456");
	//	user.getLikeSentences().add(likeSentences);
		System.out.println("123456");
		likeSentences.setSentence(sentence);
	//	sentence.getLikeSentences().add(likeSentences);
		System.out.println("123456");
		//查询条件：用户和句子id
		Specification<LikeSentence> specUserIDAndSenteceID = new Specification<LikeSentence>() {
			@Override
			public Predicate toPredicate(Root<LikeSentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(
					cb.equal(root.get("user").get("id"), uid),
					cb.equal(root.get("sentence").get("id"), sid)
				);
			}
		};
		
		//判断该操作是否存在，如果不存在，则执行该操作；如果存在，则报错
		if(this.lsd.findOne(specUserIDAndSenteceID) != null) {
			System.out.println("该句子已被喜欢！");
			return 15;
		} else {
			sentence.setNumbersOfLike(sentence.getNumbersOfLike()+1);
			sentence.setHotToday(sentence.getHotToday()+10);
			this.sd.save(sentence);				//改变了句子的喜欢数和热度，级联操作也保存了likeSentence
			return 0;
		}
	}
	
	//查找该用户是否喜欢该句子
	@Override
	@Cacheable(value = "like_sentence")
	public Integer findLikeSentenceByUidAndBySid(Integer uid, Integer sid) {
		//查询条件：用户id和句子id
		Specification<LikeSentence> spec = new Specification<LikeSentence>() {
			@Override
			public Predicate toPredicate(Root<LikeSentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("user").get("id"), uid),
						cb.equal(root.get("sentence").get("id"), sid));
			}
		};
		return this.lsd.findOne(spec) != null ? 1 : 0;
	}
	
	//功能：排序+分页查找用户喜欢的句子
	@Override
	@Cacheable(value = "like_sentence")
	public Page<LikeSentence> findLikeSentenceAllPageAndSortByUid(Integer page, Integer size, Direction dir,
			String pro, Integer uid) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		
		Page<LikeSentence> page1;
		if(uid != null) {		//如果有 uid，则查询该用户的情况
			//查询条件：用户id
			Specification<LikeSentence> spec = new Specification<LikeSentence>() {
				@Override
				public Predicate toPredicate(Root<LikeSentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("user").get("id"), uid);
				}
			};
			page1 = this.lsd.findAll(spec, pageable);
		} else {				//否则，查询所有用户的情况
			page1 = this.lsd.findAll(pageable);
		}
		
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
	
	//功能：排序+分页查找喜欢该句子的用户
	@Override
	@Cacheable(value = "like_sentence")
	public Page<LikeSentence> findLikeSentenceAllPageAndSortBySid(Integer page, Integer size, Direction dir,
			String pro, Integer sid) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		
		Page<LikeSentence> page1;
		if(sid != null) {		//如果有 sid，则查询该句子的情况
			//查询条件：用户id
			Specification<LikeSentence> spec = new Specification<LikeSentence>() {
				@Override
				public Predicate toPredicate(Root<LikeSentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("sentence").get("id"), sid);
				}
			};
			page1 = this.lsd.findAll(spec, pageable);
		} else {				//否则，查询所有句子的情况
			page1 = this.lsd.findAll(pageable);
		}
		
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
	
	@Override
	@CacheEvict(value = "like_sentence", allEntries = true)
	public void delLikeSentenceById(Integer id) {
		this.lsd.deleteLikeSentenceById(id);
	}
	
	/**
	 * 取消喜欢
	 */
	@Override
	@CacheEvict(value = {"like_sentence", "sentence"}, allEntries = true)
	public Integer delLikeSentenceBySidAndByUid(Integer sid, Integer uid) {
		Sentence sentence = this.sd.findOne(sid);
		if(sentence == null) {
			System.out.println("没有该句子！");
			return 999;
		}
		
		if(sentence.getUser().getId() == uid) {
			System.out.println("发布者不能取消喜欢自己发布的句子");
			return 999;
		}
		
		
		//查询条件：句子id和用户id
		Specification<LikeSentence> specSidAndUid = new Specification<LikeSentence>() {
			@Override
			public Predicate toPredicate(Root<LikeSentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("sentence").get("id"), sid),
						cb.equal(root.get("user").get("id"), uid));
			}
		};
		LikeSentence likeSentence = this.lsd.findOne(specSidAndUid);
		if(likeSentence == null) {
			System.out.println("该用户未喜欢该句子！");
			return 999;
		}
		this.lsd.delete(likeSentence.getId());
		
		sentence.setNumbersOfLike(sentence.getNumbersOfLike() - 1);
		sentence.setHotToday(sentence.getHotToday()-10);
		this.sd.save(sentence);
		return 0;
	}
	
	/**
	 * -查询用户喜欢的句子数量
	 */
	@Override
	@Cacheable(value = "like_sentence")
	public Integer findLikeSentenceNumbersByUser(String name) {
		//查询条件：用户的账号
		Specification<LikeSentence> specName = new Specification<LikeSentence>() {
			@Override
			public Predicate toPredicate(Root<LikeSentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("user").get("name"), name);
			}
		};
		return (int)this.lsd.count(specName);
	}
}
