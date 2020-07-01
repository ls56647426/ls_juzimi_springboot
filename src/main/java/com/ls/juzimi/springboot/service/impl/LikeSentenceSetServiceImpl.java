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

import com.ls.juzimi.springboot.dao.LikeSentenceSetDao;
import com.ls.juzimi.springboot.dao.SentenceSetDao;
import com.ls.juzimi.springboot.dao.UserDao;
import com.ls.juzimi.springboot.pojo.LikeSentenceSet;
import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.LikeSentenceSetService;

@Service
public class LikeSentenceSetServiceImpl implements LikeSentenceSetService {

	@Autowired
	private LikeSentenceSetDao lssd;
	
	@Autowired
	private SentenceSetDao ssd;
	
	@Autowired
	private UserDao ud;
	
	/**
	 * 添加喜欢
	 */
	@Override
	@CacheEvict(value = "like_sentence_set", allEntries = true)
	public Integer add(SentenceSet sentenceSet, User user) {
		//检错
		if(sentenceSet == null || user == null) {
			System.out.println("数据错误！");
			return 14;
		}
		
		//likeSentenceSet与user和sentenceSet映射：一对多
		//获取关联实体对象并关联
		LikeSentenceSet likeSentenceSet = new LikeSentenceSet();
		
		likeSentenceSet.setUser(user);
		user.getLikeSentenceSets().add(likeSentenceSet);
		
		likeSentenceSet.setSentenceSet(sentenceSet);
		sentenceSet.getLikeSentenceSets().add(likeSentenceSet);
		
		//查询条件：用户和句集id
		Specification<LikeSentenceSet> specUserIDAndSenteceSetID = new Specification<LikeSentenceSet>() {
			@Override
			public Predicate toPredicate(Root<LikeSentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(
					cb.equal(root.get("user").get("id"), user.getId()),
					cb.equal(root.get("sentenceSet").get("id"), sentenceSet.getId())
				);
			}
		};
		
		//判断该操作是否存在，如果不存在，则执行该操作；如果存在，则报错
		if(this.lssd.findOne(specUserIDAndSenteceSetID) != null) {
			System.out.println("该句集已被喜欢！");
			return 16;
		} else {
			sentenceSet.setNumbersOfLike(sentenceSet.getNumbersOfLike()+1);
			this.lssd.save(likeSentenceSet);
			return 0;
		}
	}

	/**
	 * 重载add
	 */
	@Override
	@CacheEvict(value = "like_sentence_set", allEntries = true)
	public Integer add(Integer ssid, Integer uid) {
		//获取sentenceSet和user实体对象
		SentenceSet sentenceSet = this.ssd.findOne(ssid);
		User user = this.ud.findOne(uid);
		
		//检错
		if(sentenceSet == null || user == null) {
			System.out.println("数据错误！");
			return 14;
		}
		
		//likeSentence与user和sentence映射：一对多
		//获取关联实体对象并关联
		LikeSentenceSet likeSentenceSet = new LikeSentenceSet();
		
		likeSentenceSet.setUser(user);
		user.getLikeSentenceSets().add(likeSentenceSet);
		
		likeSentenceSet.setSentenceSet(sentenceSet);
		sentenceSet.getLikeSentenceSets().add(likeSentenceSet);
		
		//查询条件：用户和句集id
		Specification<LikeSentenceSet> specUserIDAndSenteceSetID = new Specification<LikeSentenceSet>() {
			@Override
			public Predicate toPredicate(Root<LikeSentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(
					cb.equal(root.get("user").get("id"), uid),
					cb.equal(root.get("sentenceSet").get("id"), ssid)
				);
			}
		};
		
		//判断该操作是否存在，如果不存在，则执行该操作；如果存在，则报错
		if(this.lssd.findOne(specUserIDAndSenteceSetID) != null) {
			System.out.println("该句集已被喜欢！");
			return 16;
		} else {
			sentenceSet.setNumbersOfLike(sentenceSet.getNumbersOfLike()+1);
			this.lssd.save(likeSentenceSet);
			return 0;
		}
	}

	@Override
	@Cacheable(value = "like_sentence_set")
	public Page<LikeSentenceSet> findLikeSentenceSetAllPageAndSortByUid(Integer page, Integer size, Direction dir,
			String pro, Integer uid) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		
		Page<LikeSentenceSet> page1;
		if(uid != null) {		//如果有 uid，则查询该用户的情况
			//查询条件：用户id
			Specification<LikeSentenceSet> spec = new Specification<LikeSentenceSet>() {
				@Override
				public Predicate toPredicate(Root<LikeSentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("user").get("id"), uid);
				}
			};
			page1 = this.lssd.findAll(spec, pageable);
		} else {				//否则，查询所有用户的情况
			page1 = this.lssd.findAll(pageable);
		}
		
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}

	@Override
	@Cacheable(value = "like_sentence_set")
	public Page<LikeSentenceSet> findLikeSentenceSetAllPageAndSortBySsid(Integer page, Integer size, Direction dir,
			String pro, Integer ssid) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		
		Page<LikeSentenceSet> page1;
		if(ssid != null) {		//如果有 ssid，则查询该句集的情况
			//查询条件：用户id
			Specification<LikeSentenceSet> spec = new Specification<LikeSentenceSet>() {
				@Override
				public Predicate toPredicate(Root<LikeSentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("sentenceSet").get("id"), ssid);
				}
			};
			page1 = this.lssd.findAll(spec, pageable);
		} else {				//否则，查询所有句集的情况
			page1 = this.lssd.findAll(pageable);
		}
		
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}

	@Override
	@CacheEvict(value = "like_sentence_set", allEntries = true)
	public void delLikeSentenceSetById(Integer id) {
		this.lssd.deleteLikeSentenceSetById(id);
	}

	/**
	 * -查找该用户喜欢的句集
	 */
	@Override
	@Cacheable(value = "like_sentence_set")
	public Integer findLikeSentenceSetNumbersByUser(String name) {
		//查询条件：用户账号和密码
		Specification<LikeSentenceSet> specName = new Specification<LikeSentenceSet>() {
			@Override
			public Predicate toPredicate(Root<LikeSentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("user").get("name"), name);
			}
		};
		
		return (int)this.lssd.count(specName);
	}
}
