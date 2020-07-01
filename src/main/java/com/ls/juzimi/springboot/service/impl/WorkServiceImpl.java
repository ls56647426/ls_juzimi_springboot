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

import com.ls.juzimi.springboot.dao.UserDao;
import com.ls.juzimi.springboot.dao.WorkDao;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.pojo.Work;
import com.ls.juzimi.springboot.service.WorkService;

@Service
public class WorkServiceImpl implements WorkService {
	
	@Autowired
	private WorkDao wd;
	
	@Autowired
	private UserDao ud;

	/**
	 * 返回值解释：
	 * 0：	创建成功
	 * 11：	创建失败，该作品已存在
	 */
	@Override
	@CacheEvict(value = "work", allEntries = true)
	public Integer addWork(Work work) {
		
		//sentence_set与user映射：一对多
		//查询条件：用户昵称
		Specification<User> specUserNickname = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("nickname"), work.getUser().getNickname());
			}
		};
		
		//获取关联实体对象并关联
		User user = this.ud.findOne(specUserNickname);
		user.getWorks().add(work);
		work.setUser(user);
		
		//查询条件：作品名和作者名
		Specification<Work> specWorkName = new Specification<Work>() {
			@Override
			public Predicate toPredicate(Root<Work> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("name"), work.getName()),
						cb.equal(root.get("user").get("nickname"), user.getNickname()));
			}
		};
		
		//判断是否有该句集名，如果没有，则save；如果有，则报错
		if(this.wd.findOne(specWorkName) != null) {
			System.out.println("该作品已存在！");
			return 17;
		} else {
			this.wd.save(work);
			return 0;
		}
	}
	
	@Override
	@Cacheable(value = "work", key = "#id")
	public Work findWorkById(Integer id) {
		return this.wd.findOne(id);
	}
	
	/**
	 * 返回值解释：
	 * 0：	修改成功
	 * -//12：	修改失败，与上次作品相同
	 */
	@Override
	@CacheEvict(value = "work", allEntries = true)
	public Integer updateWork(Work work) {
		//通过id获取具有完整关联的work实体对象
		Work newWork = this.wd.findOne(work.getId());
		
		if(work.getName().equals(newWork.getName()) &&
				work.getDescriptions().contentEquals(newWork.getDescriptions())) {
			System.out.println("与上次作品相同！");
			return 0;
		} else {
			//更新该实体内容
			newWork.setName(work.getName());
			newWork.setDescriptions(work.getDescriptions());
			this.wd.save(newWork);
			return 0;
		}
	}
	
	@Override
	@CacheEvict(value = "work", allEntries = true)
	public void deleteWorkById(Integer id) {
		if(this.wd.findOne(id) == null) {
			System.out.println("删除失败，该id不存在！");
		} else {
			this.wd.deleteWorkById(id);
		}
	}
	
	@Override
	@Cacheable(value = "work")
	public Page<Work> findWorkAllPageAndSort(Integer page, Integer size, Direction dir, String pro) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		Page<Work> page1 = this.wd.findAll(pageable);
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
	
	@Override
	@Cacheable(value = "work")
	public List<Work> findWorkByUid(Integer uid){
		//查询条件：user
		Specification<Work> specSentenceSetUid = new Specification<Work>() {
			@Override
			public Predicate toPredicate(Root<Work> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("user").get("id"), uid);
			}
		};
		List<Work> list = this.wd.findAll(specSentenceSetUid);
		System.out.println(list);
		return list;
	}
}
