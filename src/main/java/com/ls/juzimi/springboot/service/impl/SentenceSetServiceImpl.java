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
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.dao.SentenceSetDao;
import com.ls.juzimi.springboot.dao.UserDao;
import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.LikeSentenceSetService;
import com.ls.juzimi.springboot.service.SentenceSetService;

@Service
@Transactional
public class SentenceSetServiceImpl implements SentenceSetService {

	@Autowired
	private SentenceSetDao ssd;
	
	@Autowired
	private UserDao ud;
	
	@Autowired
	private LikeSentenceSetService lsss;
	
	/**
	 * 返回值解释：
	 * 0：	创建成功
	 * 11：	创建失败，该句集已存在
	 */
	@Override
	@CacheEvict(value = "sentence_set", allEntries = true)
	public Integer addSentenceSet(SentenceSet sentenceSet) {
		
		//sentence_set与user映射：一对多
		//查询条件：用户昵称
		Specification<User> specUserNickname = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("nickname"), sentenceSet.getUser().getNickname());
			}
		};
		
		//获取关联实体对象并关联
		User user = this.ud.findOne(specUserNickname);
		user.getSentenceSets().add(sentenceSet);
		sentenceSet.setUser(user);
		
		//查询条件：句集名
		Specification<SentenceSet> specSentenceSetName = new Specification<SentenceSet>() {
			@Override
			public Predicate toPredicate(Root<SentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("name"), sentenceSet.getName());
			}
		};
		
		//判断是否有该句集名，如果没有，则save；如果有，则报错
		if(this.ssd.findOne(specSentenceSetName) != null) {
			System.out.println("该句集已存在！");
			return 11;
		} else {
			this.ssd.save(sentenceSet);
			return this.lsss.add(sentenceSet, user);
		}
	}
	
	@Override
	@Cacheable(value = "sentence_set", key = "#id")
	public SentenceSet findSentenceSetById(Integer id) {
		return this.ssd.findOne(id);
	}

	/**
	 * 返回值解释：
	 * 0：	修改成功
	 * 12：	修改失败，与上次句集名相同
	 */
	@Override
	@CacheEvict(value = "sentence_set", allEntries = true)
	public Integer updateSentenceSet(SentenceSet sentenceSet) {
		//通过id获取具有完整关联的sentence_set实体对象
		SentenceSet newSentenceSet = this.ssd.findOne(sentenceSet.getId());
		
		if(sentenceSet.getName().equals(newSentenceSet.getName()) &&
				sentenceSet.getDescriptions().contentEquals(newSentenceSet.getDescriptions())) {
			System.out.println("与上次句集相同！");
			return 0;
		} else {
			//更新该实体内容
			newSentenceSet.setName(sentenceSet.getName());
			newSentenceSet.setDescriptions(sentenceSet.getDescriptions());
			this.ssd.save(newSentenceSet);
			return 0;
		}
	}

	@Override
	@CacheEvict(value = "sentence_set", allEntries = true)
	public void deleteSentenceSetById(Integer id) {
		if(this.ssd.findOne(id) == null) {
			System.out.println("删除失败，该id不存在！");
		} else {
			this.ssd.deleteSentenceSetById(id);
		}
	}

	@Override
	@Cacheable(value = "sentence_set")
	public Page<SentenceSet> findSentenceSetAllPageAndSort(Integer page, Integer size, Direction dir, String pro) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		Page<SentenceSet> page1 = this.ssd.findAll(pageable);
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}

	@Override
	@Cacheable(value = "sentence_set")
	public List<SentenceSet> findSentenceSetByUid(Integer uid){
		//查询条件：user
		Specification<SentenceSet> specSentenceSetUid = new Specification<SentenceSet>() {
			@Override
			public Predicate toPredicate(Root<SentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("user").get("id"), uid);
			}
		};
		List<SentenceSet> list = this.ssd.findAll(specSentenceSetUid);
		System.out.println(list);
		return list;
	}
}
