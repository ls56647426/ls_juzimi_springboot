package com.ls.juzimi.springboot.service.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ls.juzimi.springboot.dao.JoinSentenceSetDao;
import com.ls.juzimi.springboot.dao.SentenceDao;
import com.ls.juzimi.springboot.dao.SentenceSetDao;
import com.ls.juzimi.springboot.pojo.JoinSentenceSet;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.service.JoinSentenceSetService;

@Service
@Transactional
public class JoinSentenceSetServiceImpl implements JoinSentenceSetService {

	@Autowired
	private JoinSentenceSetDao jssd;
	
	@Autowired
	private SentenceDao sd;
	
	@Autowired
	private SentenceSetDao ssd;
	
	/**
	 * 功能：将句子加入句集
	 * 返回值解释：
	 * 0：	加入句集成功
	 * 13：	加入句集失败，该句子在该句集中已存在
	 */
	@Override
	@CacheEvict(value = "join_sentence_set", allEntries = true)
	public Integer add(Sentence sentence, SentenceSet sentenceSet) {
		//检错
		if(sentence == null || sentenceSet == null) {
			System.out.println("数据错误！");
			return 14;
		}
		
		//join_sentence_set与sentence_set和sentence映射：一对多
		//获取关联实体对象并关联
		JoinSentenceSet joinSentenceSet = new JoinSentenceSet();
		
		joinSentenceSet.setSentenceSet(sentenceSet);
		sentenceSet.getJoinSentenceSets().add(joinSentenceSet);
		
		joinSentenceSet.setSentence(sentence);
		sentence.getJoinSentenceSets().add(joinSentenceSet);
		
		//查询条件：句集和句子id
		Specification<JoinSentenceSet> specSentenceSetIDAndSenteceID = new Specification<JoinSentenceSet>() {
			@Override
			public Predicate toPredicate(Root<JoinSentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(
					cb.equal(root.get("sentenceSet").get("id"), sentenceSet.getId()),
					cb.equal(root.get("sentence").get("id"), sentence.getId())
				);
			}
		};
		
		//判断该操作是否存在，如果不存在，则执行该操作；如果存在，则报错
		if(this.jssd.findOne(specSentenceSetIDAndSenteceID) != null) {
			System.out.println("该句子已加入该句集！");
			return 13;
		} else {
			this.jssd.save(joinSentenceSet);
			return 0;
		}
	}
	
	//重载add函数
	@Override
	@CacheEvict(value = "join_sentence_set", allEntries = true)
	public Integer add(Integer sid, Integer ssid) {
		//join_sentence_set与sentence_set和sentence映射：一对多
		//获取关联实体对象
		JoinSentenceSet joinSentenceSet = new JoinSentenceSet();
		Sentence sentence = this.sd.findOne(sid);
		SentenceSet sentenceSet = this.ssd.findOne(ssid);
		
		//检错
		if(sentence == null || sentenceSet == null) {
			System.out.println("数据错误！");
			return 14;
		}
		
		//查询条件：句集和句子id
		Specification<JoinSentenceSet> specSentenceSetIDAndSenteceID = new Specification<JoinSentenceSet>() {
			@Override
			public Predicate toPredicate(Root<JoinSentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(
					cb.equal(root.get("sentenceSet").get("id"), ssid),
					cb.equal(root.get("sentence").get("id"), sid)
				);
			}
		};
		
		//判断该操作是否存在，如果不存在，则执行该操作；如果存在，则报错
		if(this.jssd.findOne(specSentenceSetIDAndSenteceID) != null) {
			System.out.println("该句子已加入该句集！");
			return 13;
		} else {
			//关联映射
			joinSentenceSet.setSentenceSet(sentenceSet);
			sentenceSet.getJoinSentenceSets().add(joinSentenceSet);
			
			joinSentenceSet.setSentence(sentence);
			sentence.getJoinSentenceSets().add(joinSentenceSet);
			
			this.jssd.save(joinSentenceSet);
			return 0;
		}
	}
	
	//根据sid和ssid查找joinSentenceSet
	@Override
	@Cacheable(value = "join_sentence_set")
	public JoinSentenceSet find(Integer sid, Integer ssid) {
		//查询条件：句集和句子id
		Specification<JoinSentenceSet> specSentenceSetIDAndSenteceID = new Specification<JoinSentenceSet>() {
			@Override
			public Predicate toPredicate(Root<JoinSentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(
					cb.equal(root.get("sentenceSet").get("id"), ssid),
					cb.equal(root.get("sentence").get("id"), sid)
				);
			}
		};
		return this.jssd.findOne(specSentenceSetIDAndSenteceID);
	}
	
	@Override
	@CacheEvict(value = "join_sentence_set", allEntries = true)
	public void deleteJoinSentenceSetById(Integer id) {
		if(this.jssd.findOne(id) == null) {
			System.out.println("删除失败，句集中无此句子！");
		} else {
			this.jssd.deleteJoinSentenceSetById(id);
		}
	}

	//功能：排序+分页查找句集中的句子
	@Override
	@Cacheable(value = "join_sentence_set")
	public Page<JoinSentenceSet> findJoinSentenceSetAllPageAndSort(Integer page, Integer size, Direction dir,
			String pro, Integer ssid) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		
		Page<JoinSentenceSet> page1;
		if(ssid != null) {		//如果有ssid，则查询该句集的情况
			//查询条件：句集id
			Specification<JoinSentenceSet> spec = new Specification<JoinSentenceSet>() {
				@Override
				public Predicate toPredicate(Root<JoinSentenceSet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("sentenceSet").get("id"), ssid);
				}
			};
			page1 = this.jssd.findAll(spec, pageable);
		} else {				//否则，查询所有句集的情况
			page1 = this.jssd.findAll(pageable);
		}
		
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
}
