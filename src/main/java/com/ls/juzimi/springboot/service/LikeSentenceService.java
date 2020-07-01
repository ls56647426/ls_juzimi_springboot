package com.ls.juzimi.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.ls.juzimi.springboot.pojo.LikeSentence;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.User;

public interface LikeSentenceService {

	//添加喜欢
	public Integer add(Sentence sentence, User user);
	public Integer add(Integer sid, Integer uid);
	
	//查找该用户是否喜欢该句子
	public Integer findLikeSentenceByUidAndBySid(Integer uid, Integer sid);
	
	//排序+分页查找用户喜欢的句子
	public Page<LikeSentence> findLikeSentenceAllPageAndSortByUid(Integer page, Integer size, Direction dir, String pro, Integer uid);
	
	//排序+分页查找喜欢该句子的用户
	public Page<LikeSentence> findLikeSentenceAllPageAndSortBySid(Integer page, Integer size, Direction dir, String pro, Integer sid);
	
	public void delLikeSentenceById(Integer id);
	public Integer delLikeSentenceBySidAndByUid(Integer sid, Integer uid);
	
	//查找用户喜欢的句子数量
	public Integer findLikeSentenceNumbersByUser(String name);
}
