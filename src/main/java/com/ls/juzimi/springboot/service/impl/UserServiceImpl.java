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

import com.ls.juzimi.springboot.dao.UserDao;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao ud;
	
	@Override
	@CacheEvict(value = "user", allEntries = true)
	public Integer addUser(User user) {
		
		//查询条件
		Specification<User> specName = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("name"), user.getName());
			}
		};
		Specification<User> specNickname = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("nickname"), user.getNickname());
			}
		};
		Specification<User> specMobile = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("mobile"), user.getMobile());
			}
		};
		
		//判断是否有该用户名，如果没有，则save；如果有，则报错
		if(this.ud.findOne(specName) != null){
			System.out.println("添加失败，该用户已存在！");
			return 1;
		} else if(this.ud.findOne(specNickname) != null) {
			System.out.println("添加失败，该昵称已存在");
			return 9;
		} else if(this.ud.findOne(specMobile) != null) {
			System.out.println("添加失败，该手机号已绑定");
			return 8;
		} else {
			this.ud.save(user);
			return 0;
		}
	}

	@Override
	@Cacheable(value = "user", key = "#id")
	public User findUserById(Integer id) {
		return this.ud.findOne(id);
	}

	@Override
	@CacheEvict(value = "user", allEntries = true)
	public Integer updateUserPasswordByName(User user) {
		//查询条件：账号
		Specification<User> specName = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("name"), user.getName());
			}
		};
		//查询条件：昵称
		Specification<User> specNickname = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("nickname"), user.getNickname()), cb.notEqual(root.get("id"), user.getId()));
			}
		};
		//查询条件：密码
		Specification<User> specPwd = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("password"), user.getPassword()), cb.equal(root.get("id"), user.getId()));
			}
		};
		
		//判断是否有该用户名，如果没有，则报错；如果有，则修改
		if(this.ud.findOne(specName) == null){				//判断是否有该用户
			System.out.println("修改失败，该用户不存在！");
			return 2;
		} else if(this.ud.findOne(specNickname) != null) {	//判断昵称是否重复
			System.out.println("修改失败，该昵称已存在");
			return 9;
		} else if(this.ud.findOne(specPwd) != null) {		//判断密码是否有变化
			System.out.println("修改失败，与上次密码相同！");
			return 7;
		} else {
			this.ud.save(user);
			return 0;
		}
	}

	@Override
	@CacheEvict(value = "user", allEntries = true)
	public void deleteUserById(Integer id) {
		if(this.ud.findOne(id) == null) {
			System.out.println("删除失败，该id不存在！");
		} else {
			this.ud.deleteUserById(id);
		}
	}
	
	public User findUserByNameAndByPassword(String name, String password) {
		//查询条件：用户名和密码
		Specification<User> specNameAndPwd = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("name"), name), cb.equal(root.get("password"), password));
			}
		};
		
		return this.ud.findOne(specNameAndPwd);
	}
	
	@Override
	@Cacheable(value = "user")
	public Page<User> findUserAllPageAndSort(Integer page, Integer size, Direction dir, String pro){
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		Page<User> page1 = this.ud.findAll(pageable);
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
	
	@Override
	@Cacheable(value = "user")
	public User findUserByName(String name) {
		return this.ud.findOne(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("name"), name);
			}
		});
	}
}
