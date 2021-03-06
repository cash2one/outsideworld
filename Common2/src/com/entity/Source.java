package com.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "source")
public class Source implements Model {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id = -1;

	@Column(nullable = false, length = 200)
	@NotNull
	private String name;

	/* URL */
	@Column(name = "entry", nullable = false, length = 200, unique = true)
	@NotNull
	@Pattern(regexp = "http://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?", message = "URL格式不正确！")
	private String url;

	@Column
	private int depth = 5;

	@Column(name = "fetch_interval")
	private int interval = 3;

	@Column(name = "domain")
	@NotNull
	private String domain;

	@Column(name = "format")
	private String format = "other";

	@Column(name = "channel")
	private String channel = "sucai";

	@OneToMany(targetEntity = Template.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "source_id")
	private Set<Template> tempaltes = new HashSet<Template>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Set<Template> getTempaltes() {
		return tempaltes;
	}

	public void setTempaltes(Set<Template> tempaltes) {
		this.tempaltes = tempaltes;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
}
