package com.ls.juzimi.springboot.service.impl;

import java.util.ArrayList;
import java.util.List;

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

import com.ls.juzimi.springboot.dao.CategoryDao;
import com.ls.juzimi.springboot.dao.SentenceDao;
import com.ls.juzimi.springboot.dao.UserDao;
import com.ls.juzimi.springboot.dao.WorkDao;
import com.ls.juzimi.springboot.pojo.Category;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.pojo.Work;
import com.ls.juzimi.springboot.service.LikeSentenceService;
import com.ls.juzimi.springboot.service.SentenceService;

@Service
public class SentenceServiceImpl implements SentenceService {
	
	@Autowired
	private SentenceDao sd;
	
	@Autowired
	private UserDao ud;
	
	@Autowired
	private CategoryDao cd;
	
	@Autowired
	private LikeSentenceService lss;
	
	@Autowired
	private WorkDao wd;

	/**
	 * 返回值解释：
	 * 0：添加成功
	 * 5：添加失败，该句子已存在
	 */
	@Override
	@CacheEvict(value = "sentence", allEntries = true)
	public Integer addSentence(Sentence sentence) {
		
		//sentence与user映射：一对多
		//查询条件：用户昵称
		Specification<User> specUserNickname = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("nickname"), sentence.getUser().getNickname());
			}
		};
		//获取user实体对象并关联
		User user = this.ud.findOne(specUserNickname);
		sentence.setUser(user);
		user.getSentences().add(sentence);
		
		//查询条件：分类名
		Specification<Category> specCategoryName = new Specification<Category>() {
			@Override
			public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("name"), sentence.getCategory().getName());
			}
		};
		//获取category实体对象并关联
		Category category = this.cd.findOne(specCategoryName);
		sentence.setCategory(category);
		category.getSentences().add(sentence);
		
		if(sentence.getWork().getName() != "") {		//作品为可选项
			System.out.println("作品不为空！");
			//查询条件：作品名
			Specification<Work> specWorkName = new Specification<Work>() {
				@Override
				public Predicate toPredicate(Root<Work> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("name"), sentence.getWork().getName());
				}
			};
			//获取work实体对象并关联
			Work work = this.wd.findOne(specWorkName);
			
			if(work != null) {
				sentence.setWork(work);
				work.getSentences().add(sentence);
			} else {
				System.out.println("该作品尚未创建，请联系管理员，谢谢！");
				return 12;
			}
			
		} else {
			sentence.setWork(null);
		}
		
		//查询条件：句子内容
		Specification<Sentence> specCont = new Specification<Sentence>() {
			@Override
			public Predicate toPredicate(Root<Sentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("content"), sentence.getContent());
			}
		};
		//判断是否有该句子，如果没有，则save；如果有，则报错
		if(this.sd.findOne(specCont) == null){
			this.sd.save(sentence);
			return this.lss.add(sentence, user);
		} else {
			System.out.println("添加失败，该句子已存在！");
			return 5;
		}
	}
	
	@Override
	@Cacheable(value = "sentence", key = "#id")
	public Sentence findSentenceById(Integer id) {
		return this.sd.findOne(id);
	}
	
	/**
	 * 返回值解释：
	 * 0：	修改成功
	 * 10：	修改失败，与上次句子内容相同
	 */
	@Override
	@CacheEvict(value = "sentence", allEntries = true)
	public Integer updateSentence(Sentence sentence) {
		//通过id获取具有完整关联的sentence实体对象
		Sentence newSentence = this.sd.findOne(sentence.getId());
		//句子作品修改
		if(!sentence.getWork().getName().equals(newSentence.getWork() == null ? "" : newSentence.getWork().getName())) {
			//查询条件：类名
			Specification<Work> specWorkName = new Specification<Work>() {
				@Override
				public Predicate toPredicate(Root<Work> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("name"), sentence.getCategory().getName());
				}
			};
			newSentence.getWork().getSentences().remove(newSentence);		//删除原来作品的关联
			newSentence.setWork(this.wd.findOne(specWorkName));				//加入新的作品
			newSentence.getWork().getSentences().add(newSentence);			//加入新作品的关联
			System.out.println("作品已修改！");
		}
		//句子分类修改
		if(!sentence.getCategory().getName().equals(newSentence.getCategory().getName())) {
			//查询条件：类名
			Specification<Category> specCategoryName = new Specification<Category>() {
				@Override
				public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("name"), sentence.getCategory().getName());
				}
			};
			newSentence.getCategory().getSentences().remove(newSentence);		//删除原来分类的关联
			newSentence.setCategory(this.cd.findOne(specCategoryName));			//加入新的分类
			newSentence.getCategory().getSentences().add(newSentence);			//加入新分类的关联
			System.out.println("分类已修改！");
		}
		//句子发布人修改
		if(!sentence.getUser().getNickname().equals(newSentence.getUser().getNickname())) {
			//查询条件：用户昵称
			Specification<User> specUserNickname = new Specification<User>() {
				@Override
				public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("nickname"), sentence.getUser().getNickname());
				}
			};
			newSentence.getUser().getSentences().remove(newSentence);			//删除原来作者的关联
			newSentence.setUser(this.ud.findOne(specUserNickname));				//更新作者
			newSentence.getUser().getSentences().add(newSentence);				//加入新作者的关联
			System.out.println("发布人已修改！");
		}
		//句子内容修改
		if(!sentence.getContent().equals(newSentence.getContent())) {
			newSentence.setContent(sentence.getContent());
			this.sd.save(newSentence);
			System.out.println("句子内容已修改！");
			return 0;
		}
		System.out.println("与上次句子相同！！");
		return 0;
	}

	@Override
	@CacheEvict(value = "sentence", allEntries = true)
	public void deleteSentenceById(Integer id) {
		if(this.sd.findOne(id) == null) {
			System.out.println("删除失败，该id不存在！");
		} else {
			this.sd.deleteSentenceById(id);
		}
	}

	@Override
	@Cacheable(value = "sentence")
	public Page<Sentence> findSentenceAllPageAndSort(Integer page, Integer size, Direction dir, String pro) {
		Sort sort = new Sort(new Order(dir, pro));
		Pageable pageable = new PageRequest(page, size, sort);
		Page<Sentence> page1 = this.sd.findAll(pageable);
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
	
	@Override
	@Cacheable(value = "sentence")
	public Page<Sentence> findSentenceAllByHotToday(Integer page, Integer size){
		//查询条件：句子热度
		Specification<Sentence> specHotToday = new Specification<Sentence>() {
			@Override
			public Predicate toPredicate(Root<Sentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.notEqual(root.get("hotToday"), 0);
			}
		};
		
		Sort sort = new Sort(new Order(Direction.DESC, "hotToday"));
		Pageable pageable = new PageRequest(page, size, sort);
		Page<Sentence> page1 = this.sd.findAll(specHotToday, pageable);
		System.out.println("第" + page1.getNumber() + "页");
        System.out.println("实际显示" + page1.getNumberOfElements() + "条");
        System.out.println("每页最多显示" + page1.getSize() + "条");
		System.out.println("共" + page1.getTotalElements() + "条");
		System.out.println("共" + page1.getTotalPages() + "页");
		return page1;
	}
	
	/**
	 * -查询用户原创的句子数量
	 * -根据句子的作者是否为该用户来判断
	 */
	@Override
	@Cacheable(value = "sentence")
	public Integer findSentenceNumbersByOriginal(String name) {
		//查询条件：作者账号和密码
		Specification<Sentence> specUserName = new Specification<Sentence>() {
			@Override
			public Predicate toPredicate(Root<Sentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//				List<Predicate> predicateListAnd1 = new ArrayList<>();
//				predicateListAnd1.add(cb.isNotNull(root.get("work")));
//				predicateListAnd1.add(cb.equal(root.get("work").get("user").get("name"), name));
				
				List<Predicate> predicateListAnd2 = new ArrayList<>();
				predicateListAnd2.add(cb.isNull(root.get("work")));
				predicateListAnd2.add(cb.equal(root.get("user").get("name"), name));
				
//				Predicate resultAnd1[] = predicateListAnd1.toArray(new Predicate[predicateListAnd1.size()]);
				Predicate resultAnd2[] = predicateListAnd2.toArray(new Predicate[predicateListAnd2.size()]);
//				Predicate end = cb.or(cb.and(resultAnd1), cb.and(resultAnd2));
				Predicate end = cb.and(resultAnd2);
				return end;
			}
		};
		return (int)this.sd.count(specUserName);
	}
	
	@Override
	@Cacheable(value = "sentence")
	public List<Sentence> findSentenceByRecentDevelopments(String name) {
		//查询条件：句子热度
		Specification<Sentence> specUserName = new Specification<Sentence>() {
			@Override
			public Predicate toPredicate(Root<Sentence> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("user").get("name"), name);
			}
		};
		Sort sort = new Sort(new Order(Direction.DESC, "publishDate"));
		Pageable pageable = new PageRequest(0, 10, sort);
		Page<Sentence> page = this.sd.findAll(specUserName, pageable);
		
		return page.getContent();
	}
}
