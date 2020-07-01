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
@Table(name = "join_sentence_set")
public class JoinSentenceSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	//句集id
	@ManyToOne
	@JoinColumn(name = "ssid", nullable = true)
	private SentenceSet sentenceSet;
	
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

	public SentenceSet getSentenceSet() {
		return sentenceSet;
	}

	public void setSentenceSet(SentenceSet sentenceSet) {
		this.sentenceSet = sentenceSet;
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
		return "JoinSentenceSet [id=" + id + ", sentenceSet=" + sentenceSet.getName() + ", sentence=" + sentence.getContent() + ", join_date="
				+ joinDate + "]";
	}
	
}
