package com.ls.juzimi.springboot.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "review")
public class Review implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	//内容
	@NotEmpty(message = "内容不能为空")
	@Column(name = "content", length = 1024, nullable = true)
	private String content;
	
	//用户id
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "uid", nullable = true)
	private User user;
	
	//句子id
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "sid", nullable = true)
	private Sentence sentence;
	
	//发布日期
	@Column(name = "publish_date", length = 20, nullable = true)
	private String publishDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

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

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	@Override
	public String toString() {
		return "Review [id=" + id + ", content=" + content + ", user=" + user + ", sentence=" + sentence
				+ ", publish_date=" + publishDate + "]";
	}
	
	
}
