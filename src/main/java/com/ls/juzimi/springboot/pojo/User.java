package com.ls.juzimi.springboot.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	//账号
	@Expose
	@NotEmpty(message = "账号不能为空")
	@Pattern(regexp = "[a-zA-Z0-9]*", message = "账号不合法")
	@Length(min = 6, max = 18, message = "账号长度为6-18位")
	@Column(name = "name", length = 18, nullable = true)
	private String name;
	
	//密码
	@Expose
	@NotEmpty(message = "密码不能为空")
	@Pattern(regexp = "[a-zA-Z0-9.]*", message = "密码不合法")
	@Length(min = 8, max = 18, message = "密码长度为8-18位")
	@Column(name = "password", length = 18, nullable = true)
	private String password;
	
	//头像
	@Expose
	@Column(name = "head_portrait", length = 64, nullable = true)
	private String headPortrait;
	
	//昵称
	@Expose
	@NotEmpty(message = "昵称不能为空")
	@Length(min = 1, max = 8, message = "昵称长度为1-8位")
	@Column(name = "nickname", length = 8, nullable = true)
	private String nickname;
	
	//简介
	@Expose
	@Column(name = "profile", length = 255, nullable = true)
	private String profile;
	
	//性别
	@Expose
	@Column(name = "sex", length = 1, nullable = true)
	private String sex;
	
	//出生日期
	@Expose
	@Column(name = "borthday", length = 10, nullable = true)
	private String borthday;
	
	//手机号
	@Expose
	@Pattern(regexp = "[0-9]*", message = "手机号不合法")
	@Length(min = 11, max = 11, message = "手机号长度必须位11位")
	@Column(name = "mobile", length = 11, nullable = true)
	private String mobile;
	
	//发布的(原创)句子
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private Set<Sentence> sentences = new HashSet<Sentence>();
	
	//发布的(原创)句集
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private Set<SentenceSet> sentenceSets = new HashSet<SentenceSet>();
	
	//喜欢(包括原创)的句子——likeSentece操作
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private Set<LikeSentence> likeSentences = new HashSet<LikeSentence>();
	
	//喜欢(包括原创)的句集——likeSenteceSet操作
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private Set<LikeSentenceSet> likeSentenceSets = new HashSet<LikeSentenceSet>();
	
	//发布的评论
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private Set<Review> reviews = new HashSet<Review>();
	
	//作品
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private Set<Work> works = new HashSet<Work>();
	
	public Set<Sentence> getSentences() {
		return sentences;
	}
	public void setSentences(Set<Sentence> sentences) {
		this.sentences = sentences;
	}
	
	public Set<LikeSentence> getLikeSentences() {
		return likeSentences;
	}
	public void setLikeSentences(Set<LikeSentence> likeSentences) {
		this.likeSentences = likeSentences;
	}
	
	public Set<LikeSentenceSet> getLikeSentenceSets() {
		return likeSentenceSets;
	}
	public void setLikeSentenceSets(Set<LikeSentenceSet> likeSentenceSets) {
		this.likeSentenceSets = likeSentenceSets;
	}
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBorthday() {
		return borthday;
	}
	public void setBorthday(String borthday) {
		this.borthday = borthday;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public Set<SentenceSet> getSentenceSets() {
		return sentenceSets;
	}
	public void setSentenceSets(Set<SentenceSet> sentenceSets) {
		this.sentenceSets = sentenceSets;
	}
	
	public Set<Review> getReviews() {
		return reviews;
	}
	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}
	
	public Set<Work> getWorks() {
		return works;
	}
	public void setWorks(Set<Work> works) {
		this.works = works;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", nickname=" + nickname
				+ ", profile=" + profile + ", sex=" + sex + ", borthday=" + borthday + ", mobile="
				+ mobile + "]";
	}
	
	
}
