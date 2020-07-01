package com.ls.juzimi.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.ls.juzimi.springboot.pojo.LikeSentenceSet;
import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.pojo.User;

public interface LikeSentenceSetService {

	//添加喜欢
	public Integer add(SentenceSet sentenceSet, User user);
	public Integer add(Integer ssid, Integer uid);
	
	//排序+分页查找用户喜欢的句集
	public Page<LikeSentenceSet> findLikeSentenceSetAllPageAndSortByUid(Integer page, Integer size, Direction dir,
			String pro, Integer uid);
	
	//排序+分页查找喜欢该句集的用户
	public Page<LikeSentenceSet> findLikeSentenceSetAllPageAndSortBySsid(Integer page, Integer size, Direction dir,
			String pro, Integer ssid);
	
	public void delLikeSentenceSetById(Integer id);
	
	public Integer findLikeSentenceSetNumbersByUser(String name);
}
