package com.ls.juzimi.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.pojo.LikeSentence;

public interface LikeSentenceDao extends JpaRepository<LikeSentence, Integer>, JpaSpecificationExecutor<LikeSentence> {

	/**
	 * 根据id删除喜欢句子操作
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from like_sentence where id = ?", nativeQuery = true)
	void deleteLikeSentenceById(Integer id);
}
