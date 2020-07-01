package com.ls.juzimi.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.ls.juzimi.springboot.pojo.Sentence;

public interface SentenceService {
	
	public Integer addSentence(Sentence sentence);
	
	public Sentence findSentenceById(Integer id);
	
	public Integer updateSentence(Sentence sentence);
	
	public void deleteSentenceById(Integer id);
	
	public Page<Sentence> findSentenceAllPageAndSort(Integer page, Integer size, Direction dir, String pro);

	public Page<Sentence> findSentenceAllByHotToday(Integer page, Integer size);
	
	public Integer findSentenceNumbersByOriginal(String name);
	
	public List<Sentence> findSentenceByRecentDevelopments(String name);
}
