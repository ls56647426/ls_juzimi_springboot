package com.ls.juzimi.springboot.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "sentence")
public class Sentence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Expose
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 11)
	private Integer id;
	
	//内容
	@Expose
	@NotEmpty(message = "内容不能为空")
	@Column(name = "content", length = 1024, nullable = true)
	private String content;
	
	//分类id
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "cid", nullable = true)
	private Category category;
	
	//发布人id
	@Expose
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "uid", nullable = true)
	private User user;
	
	//作者/作品id
	@Expose
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "wid", nullable = true)
	private Work work;
	
	//发布日期
	@Expose
	@Column(name = "publish_date", length = 20, nullable = true)
	private String publishDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	
	//喜欢数
	@Expose
	@Column(name = "numbers_of_like", length = 11, nullable = false)
	private Integer numbersOfLike = 0;
	
	//热度
	@Column(name = "hot_today", length = 11, nullable = false)
	private Integer hotToday = 0;
	
	//加入句集
	@OneToMany(mappedBy = "sentence", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private Set<JoinSentenceSet> joinSentenceSets = new HashSet<JoinSentenceSet>();
	
	//被加入喜欢的用户——likeSentece操作
	@OneToMany(mappedBy = "sentence", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private Set<LikeSentence> likeSentences = new HashSet<LikeSentence>();
	
	//被评论
	@OneToMany(mappedBy = "sentence", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private Set<Review> reviews = new HashSet<Review>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getNumbersOfLike() {
		return numbersOfLike;
	}

	public void setNumbersOfLike(Integer numbersOfLike) {
		this.numbersOfLike = numbersOfLike;
	}

	public Integer getHotToday() {
		return hotToday;
	}

	public void setHotToday(Integer hotToday) {
		this.hotToday = hotToday;
	}

	public Set<JoinSentenceSet> getJoinSentenceSets() {
		return joinSentenceSets;
	}

	public void setJoinSentenceSets(Set<JoinSentenceSet> joinSentenceSets) {
		this.joinSentenceSets = joinSentenceSets;
	}

	public Set<LikeSentence> getLikeSentences() {
		return likeSentences;
	}

	public void setLikeSentences(Set<LikeSentence> likeSentences) {
		this.likeSentences = likeSentences;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	@Override
	public String toString() {
		return "Sentence [id=" + id + ", content=" + content + ", publishDate=" + publishDate + ", numbers_of_like=" + numbersOfLike + ", hotToday=" + hotToday + "]";
	}
	
}
