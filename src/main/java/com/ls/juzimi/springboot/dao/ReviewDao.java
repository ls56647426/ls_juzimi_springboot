package com.ls.juzimi.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.pojo.Review;

public interface ReviewDao extends JpaRepository<Review, Integer>, JpaSpecificationExecutor<Review> {

	/**
	 * 根据id删除评论
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from review where id = ?", nativeQuery = true)
	void deleteReviewById(Integer id);
}
