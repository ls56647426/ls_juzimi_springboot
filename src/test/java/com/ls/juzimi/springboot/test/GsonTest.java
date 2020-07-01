package com.ls.juzimi.springboot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.ls.juzimi.springboot.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class })
public class GsonTest {

	/**
	 * -暂时不能将string转json格式：2019-11-11 10:25
	 */
	@Test
	public void toJsonTest() {
		Gson gson = new Gson();
		System.out.println(gson.toJson("a=13"));
	}
}
