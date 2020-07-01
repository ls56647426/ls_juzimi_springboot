package com.ls.juzimi.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.pojo.JoinSentenceSet;

@Repository
public interface JoinSentenceSetDao extends JpaRepository<JoinSentenceSet, Integer>, JpaSpecificationExecutor<JoinSentenceSet> {

	/**
	 * 根据id删除加入句集操作
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from join_sentence_set where id = ?", nativeQuery = true)
	void deleteJoinSentenceSetById(Integer id);
}
