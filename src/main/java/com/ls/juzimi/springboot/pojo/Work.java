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
@Table(name = "work")
public class Work implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	//作品名
	@Expose
	@NotEmpty(message = "作品名不能为空")
	@Column(name = "name", length = 8, nullable = true)
	private String name;
	
	//作品简介
	@Column(name = "descriptions", length = 255, nullable = true)
	private String descriptions;
	
	//作者id
	@Expose
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "uid", nullable = true)
	private User user;
	
	//发布日期
	@Column(name = "publish_date", length = 20, nullable = true)
	private String publishDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	
	//属于该作品的句子
	@OneToMany(mappedBy = "work", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE,
			CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	private Set<Sentence> sentences = new HashSet<Sentence>();

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

	public Set<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(Set<Sentence> sentences) {
		this.sentences = sentences;
	}

	@Override
	public String toString() {
		return "Work [id=" + id + ", name=" + name + ", descriptions=" + descriptions + ", user=" +
				user.getNickname() + ", publish_date=" + publishDate + "]";
	}
	
	
}
