package com.ls.juzimi.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.pojo.Sentence;

@Repository
public interface SentenceDao extends JpaRepository<Sentence, Integer>, JpaSpecificationExecutor<Sentence> {
	
	/**
	 * 根据id删除句子
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from sentence where id = ?", nativeQuery = true)
	void deleteSentenceById(Integer id);
}
