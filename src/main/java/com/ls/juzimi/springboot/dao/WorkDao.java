package com.ls.juzimi.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.pojo.Work;

public interface WorkDao extends JpaRepository<Work, Integer>, JpaSpecificationExecutor<Work> {

	/**
	 * -根据id删除作品
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from work where id = ?", nativeQuery = true)
	void deleteWorkById(Integer id);
}
