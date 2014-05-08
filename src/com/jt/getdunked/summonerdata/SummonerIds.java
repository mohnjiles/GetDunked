package com.jt.getdunked.summonerdata;

import com.google.gson.annotations.Expose;

public class SummonerIds {

	@Expose
	private Integer id;
	@Expose
	private String name;
	@Expose
	private Integer profileIconId;
	@Expose
	private Long revisionDate;
	@Expose
	private Integer summonerLevel;
	@Expose
	private Status status;
	@Expose
	private String message;
	@Expose
	private Integer status_code;

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

	public Integer getProfileIconId() {
		return profileIconId;
	}

	public void setProfileIconId(Integer profileIconId) {
		this.profileIconId = profileIconId;
	}

	public Long getRevisionDate() {
		return revisionDate;
	}

	public void setRevisionDate(Long revisionDate) {
		this.revisionDate = revisionDate;
	}

	public Integer getSummonerLevel() {
		return summonerLevel;
	}

	public void setSummonerLevel(Integer summonerLevel) {
		this.summonerLevel = summonerLevel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus_code() {
		return status_code;
	}

	public void setStatus_code(Integer status_code) {
		this.status_code = status_code;
	}

}