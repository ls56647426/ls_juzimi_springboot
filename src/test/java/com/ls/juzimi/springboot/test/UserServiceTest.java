package com.ls.juzimi.springboot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ls.juzimi.springboot.Application;
import com.ls.juzimi.springboot.dao.UserDao;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.UserService;

/**
 * SpringBoot 测试类
 *@RunWith:启动器
 *SpringJUnit4ClassRunner.class：让 junit 与 spring 环境进行整合
 *
 *@SpringBootTest(classes={App.class}) 1,当前类为 springBoot 的测试类
 *@SpringBootTest(classes={App.class}) 2,加载 SpringBoot 启动类。启动 springBoot
 *
 *junit 与 spring 整合 @Contextconfiguartion("classpath:applicationContext.xml")
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class })
public class UserServiceTest {
	@Autowired
	private UserService us;
	
	@Autowired
	private UserDao ud;

	@Test
	public void testAddUser() {
		User user = new User();
		user.setName("12345678910");
		user.setPassword("12345678910");
		this.us.addUser(user);
	}
	
	@Test
	public void testDeleteUser() {
		this.ud.delete(11);
	}
	
	@Test
	public void testFindUserById() {
		User user = this.us.findUserById(1);
		System.out.println(user);
		
		user = this.us.findUserById(1);
		System.out.println(user);
		
		user = this.us.findUserById(2);
		System.out.println(user);
		
		user = this.us.findUserById(1);
		System.out.println(user);
	}
	
	@Test
	public void testFindUserByNameAndByPassword() {
		User user = this.us.findUserByNameAndByPassword("ls56647426", "long123.");
		System.out.println(user);
	}
	
	@Test
	public void testUpdateUserPasswordByName() {
		User user = new User();
		user.setId(2);
		user.setName("12345678910");
		user.setPassword("12345678901");
		user.setNickname("永恒之火");
		user.setSex("男");
		user.setBorthday("4567-89-10");
		user.setMobile("12345678910");
		System.out.println(this.us.updateUserPasswordByName(user));
	}
}