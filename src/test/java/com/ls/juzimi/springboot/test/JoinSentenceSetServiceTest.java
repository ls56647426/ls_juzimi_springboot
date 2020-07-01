package com.ls.juzimi.springboot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ls.juzimi.springboot.Application;
import com.ls.juzimi.springboot.dao.JoinSentenceSetDao;
import com.ls.juzimi.springboot.dao.SentenceDao;
import com.ls.juzimi.springboot.dao.SentenceSetDao;
import com.ls.juzimi.springboot.pojo.JoinSentenceSet;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.service.JoinSentenceSetService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class })
public class JoinSentenceSetServiceTest {

	@Autowired
	private JoinSentenceSetService jsss;
	
	@Autowired
	private JoinSentenceSetDao jssd;
	
	@Autowired
	private SentenceDao sd;
	
	@Autowired
	private SentenceSetDao ssd;
	
	/**
	 * -测试成功：2019-11-10 22:03
	 */
	@Test
	public void addTest1() {
		//获取句子和句集实体对象
		Sentence sentence = this.sd.findOne(4);
		SentenceSet sentenceSet = this.ssd.findOne(4);
		JoinSentenceSet joinSentenceSet = new JoinSentenceSet();
		System.out.println(sentence);
		System.out.println(sentenceSet);
		//关联映射
		joinSentenceSet.setSentence(sentence);
		sentence.getJoinSentenceSets().add(joinSentenceSet);
		
		joinSentenceSet.setSentenceSet(sentenceSet);
		sentenceSet.getJoinSentenceSets().add(joinSentenceSet);
		
		System.out.println(joinSentenceSet);
		this.jssd.save(joinSentenceSet);
//		System.out.println(this.jsss.add(sentence, sentenceSet));
	}
	
	/**
	 * -测试成功：2019-11-10 22:04
	 */
	@Test
	public void addTest2() {
		Sentence sentence = this.sd.findOne(4);
		SentenceSet sentenceSet = this.ssd.findOne(3);
		System.out.println(this.jsss.add(sentence, sentenceSet));
	}
	
	/**
	 * -测试成功：2019-11-10 22:14
	 */
	@Test
	public void addTest3() {
		System.out.println(this.jsss.add(2, 3));
	}
	
	/**
	 * -测试成功：2019-11-10 22:19
	 */
	@Test
	public void findJoinSentenceSetAllPageAndSortTest() {
		Page<JoinSentenceSet> page = this.jsss.findJoinSentenceSetAllPageAndSort(0, 2, Direction.ASC, "id", 4);
		for(JoinSentenceSet joinSentenceSet : page) {
			System.out.println(joinSentenceSet);
		}
	}
}
