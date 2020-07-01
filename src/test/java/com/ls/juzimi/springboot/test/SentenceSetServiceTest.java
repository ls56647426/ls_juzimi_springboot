package com.ls.juzimi.springboot.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ls.juzimi.springboot.Application;
import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.service.SentenceSetService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class })
public class SentenceSetServiceTest {

	@Autowired
	private SentenceSetService sss;
	
	@Test
	public void findSentenceSetByUidTest() {
		List<SentenceSet> list = this.sss.findSentenceSetByUid(4);
		for(SentenceSet sentenceSet : list) {
			System.out.println(sentenceSet);
		}
	}
}
