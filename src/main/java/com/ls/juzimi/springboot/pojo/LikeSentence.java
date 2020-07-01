package com.ls.juzimi.springboot.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "like_sentence")
public class LikeSentence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	//用户id
	@ManyToOne
	@JoinColumn(name = "uid", nullable = true)
	private User user;
	
	//句子id
	@ManyToOne
	@JoinColumn(name = "sid", nullable = true)
	private Sentence sentence;
	
	//加入日期
	@Column(name = "join_date", length = 20, nullable = true)
	private String joinDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Sentence getSentence() {
		return sentence;
	}

	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	public String getJoin_date() {
		return joinDate;
	}

	public void setJoin_date(String joinDate) {
		this.joinDate = joinDate;
	}

	@Override
	public String toString() {
		return "Likes [id=" + id + ", user=" + user.getNickname() + ", sentence=" + sentence.getContent() +
			", join_date=" + joinDate + "]";
	}
	
	
}
