package com.ls.juzimi.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.ls.juzimi.springboot.pojo.JoinSentenceSet;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.SentenceSet;

public interface JoinSentenceSetService {
	
	//将句子加入句集
	public Integer add(Sentence sentence, SentenceSet sentenceSet);
	public Integer add(Integer sid, Integer ssid);
	
	//根据sid和ssid查找joinSentenceSet
	public JoinSentenceSet find(Integer sid, Integer ssid);
	
	public void deleteJoinSentenceSetById(Integer id);
	
	//排序+分页查找句集中的句子
	public Page<JoinSentenceSet> findJoinSentenceSetAllPageAndSort(Integer page, Integer size, Direction dir, String pro, Integer ssid);
}
