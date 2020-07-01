package com.ls.juzimi.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.ls.juzimi.springboot.pojo.SentenceSet;

public interface SentenceSetService {
	
	public Integer addSentenceSet(SentenceSet sentenceSet);
	
	public SentenceSet findSentenceSetById(Integer id);
	
	public Integer updateSentenceSet(SentenceSet sentenceSet);
	
	public void deleteSentenceSetById(Integer id);

	public Page<SentenceSet> findSentenceSetAllPageAndSort(Integer page, Integer size, Direction dir, String pro);
	
	//按uid查找句集
	public List<SentenceSet> findSentenceSetByUid(Integer uid);
}
