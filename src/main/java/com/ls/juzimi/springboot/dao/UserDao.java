package com.ls.juzimi.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.pojo.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
	/**
	 * 根据id删除用户
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from user where id = ?", nativeQuery = true)
	void deleteUserById(Integer id);
}

