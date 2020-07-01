package com.ls.juzimi.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.ls.juzimi.springboot.pojo.Category;

public interface CategoryService {
	
	public Integer addCategory(Category category);
	
	public Category findCategoryById(Integer id);
	
	public void deleteCategoryById(Integer id);
	
	public Integer updateCategoryName(Category category);
	
	public Page<Category> findCategoryAllPageAndSort(Integer page, Integer size, Direction dir, String pro);
}
