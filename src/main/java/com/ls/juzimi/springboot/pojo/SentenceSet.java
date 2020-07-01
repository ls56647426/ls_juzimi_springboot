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
@Table(name = "sentence_set")
public class SentenceSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	//句集名
	@Expose
	@NotEmpty(message = "内容不能为空")
	@Column(name = "name", length = 8, nullable = true)
	private String name;
	
	//句集描述
	@Expose
	@Column(name = "descriptions", length = 255, nullable = true)
	private String descriptions;
	
	//发布人id
	@Expose
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "uid", nullable = true)
	private User user;
	
	//发布日期
	@Expose
	@Column(name = "publish_date", length = 20, nullable = true)
	private String publishDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	
	//喜欢数
	@Expose
	@Column(name = "numbers_of_like", length = 11, nullable = false)
	private Integer numbersOfLike;
	
	//加入句集
	@OneToMany(mappedBy = "sentenceSet", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE,
			CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	private Set<JoinSentenceSet> joinSentenceSets = new HashSet<JoinSentenceSet>();
	
	//被加入喜欢的句集——likeSentenceSet操作
	@OneToMany(mappedBy = "sentenceSet", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE,
			CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	private Set<LikeSentenceSet> likeSentenceSets = new HashSet<LikeSentenceSet>();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public Integer getNumbersOfLike() {
		return numbersOfLike;
	}

	public void setNumbersOfLike(Integer numbersOfLike) {
		this.numbersOfLike = numbersOfLike;
	}

	public Set<JoinSentenceSet> getJoinSentenceSets() {
		return joinSentenceSets;
	}

	public void setJoinSentenceSets(Set<JoinSentenceSet> joinSentenceSets) {
		this.joinSentenceSets = joinSentenceSets;
	}

	public Set<LikeSentenceSet> getLikeSentenceSets() {
		return likeSentenceSets;
	}

	public void setLikeSentenceSets(Set<LikeSentenceSet> likeSentenceSets) {
		this.likeSentenceSets = likeSentenceSets;
	}

	@Override
	public String toString() {
		return "SentenceSet [id=" + id + ", name=" + name + ", descriptions=" + descriptions + ", user="
				+ user.getNickname() + ", publish_date=" + publishDate + ", numbers_of_like=" + numbersOfLike + "]";
	}
}
