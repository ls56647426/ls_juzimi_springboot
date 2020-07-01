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

import com.ls.juzimi.springboot.dao.CategoryDao;
import com.ls.juzimi.springboot.pojo.Category;
import com.ls.juzimi.springboot.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao cd;
	
	@Override
	@CacheEvict(value = "category", allEntries = true)
	public Integer addCategory(Category category) {
		//查询条件
		Specification<Category> spec = new Specification<Category>() {
			@Override
			public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("name"), category.getName());
			}
		};
		
		//判断是否有该分类，如果没有，则save；如果有，则报错
		if(this.cd.findOne(spec) == null){
			this.cd.save(category);
			return 0;
		} else {
			System.out.println("添加失败，该分类已存在！");
			return 3;
		}
	}

	@Override
	@Cacheable(value = "category", key = "#id")
	public Category findCategoryById(Integer id) {
		return this.cd.findOne(id);
	}

	@Override
	@CacheEvict(value = "category", allEntries = true)
	public void deleteCategoryById(Integer id) {
		if(this.cd.findOne(id) == null) {
			System.out.println("删除失败，该id不存在！");
		} else {
			this.cd.deleteCategoryById(id);
		}
	}
	
	@Override
	@CacheEvict(value = "category", allEntries = true)
	public Integer updateCategoryName(Category category) {
		//查询条件：类名
		Specification<Category> specName = new Specification<Category>() {
			@Override
			public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("name"), category.getName());
			}
		};
		
		if(this.cd.findOne(specName) != null) {
			System.out.println("修改失败，与上次类名相同！");
			return 4;
		} else {
			this.cd.save(category);
			return 0;
		}
	}

	@Override
	@Cacheable(value = "category")
	public Page<Category> findCategoryAllPageAndSort(Integer page, Integer size, Direction dir, String pro) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		Page<Category> page1 = this.cd.findAll(pageable);
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
}
