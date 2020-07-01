package com.ls.juzimi.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.pojo.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
	/**
	 * 根据id删除分类
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from category where id = ?", nativeQuery = true)
	void deleteCategoryById(Integer id);
}
