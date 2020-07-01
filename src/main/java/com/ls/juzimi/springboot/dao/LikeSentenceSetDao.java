package com.ls.juzimi.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.pojo.LikeSentenceSet;

public interface LikeSentenceSetDao extends JpaRepository<LikeSentenceSet, Integer>, JpaSpecificationExecutor<LikeSentenceSet> {

	/**
	 * 根据id删除喜欢句集操作
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from like_sentence_set where id = ?", nativeQuery = true)
	void deleteLikeSentenceSetById(Integer id);
}
