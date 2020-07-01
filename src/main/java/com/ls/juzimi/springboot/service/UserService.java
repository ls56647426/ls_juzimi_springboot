package com.ls.juzimi.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.ls.juzimi.springboot.pojo.User;

public interface UserService {
	
	public Integer addUser(User user);
	
	public User findUserById(Integer id);
	
	public User findUserByNameAndByPassword(String name, String password);
	
	public Integer updateUserPasswordByName(User user);
	
	public void deleteUserById(Integer id);

	public Page<User> findUserAllPageAndSort(Integer page, Integer size, Direction dir, String pro);

	public User findUserByName(String name);
}
