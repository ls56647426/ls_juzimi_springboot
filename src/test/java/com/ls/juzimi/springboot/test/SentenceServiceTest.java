package com.ls.juzimi.springboot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ls.juzimi.springboot.Application;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.service.SentenceService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class })
public class SentenceServiceTest {

	@Autowired
	private SentenceService ss;
	
	@Test
	public void findSentenceAllByHotTodayTest() {
		Page<Sentence> page = this.ss.findSentenceAllByHotToday(0, 5);
		for(Sentence sentence : page.getContent()) {
			System.out.println(sentence);
		}
	}
}
