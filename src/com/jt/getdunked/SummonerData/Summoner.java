
package com.jt.getdunked.summonerdata;


public class Summoner{
   	private Number id;
   	private String name;
   	private Number profileIconId;
   	private Number revisionDate;
   	private Number summonerLevel;

 	public Number getId(){
		return this.id;
	}
	public void setId(Number id){
		this.id = id;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public Number getProfileIconId(){
		return this.profileIconId;
	}
	public void setProfileIconId(Number profileIconId){
		this.profileIconId = profileIconId;
	}
 	public Number getRevisionDate(){
		return this.revisionDate;
	}
	public void setRevisionDate(Number revisionDate){
		this.revisionDate = revisionDate;
	}
 	public Number getSummonerLevel(){
		return this.summonerLevel;
	}
	public void setSummonerLevel(Number summonerLevel){
		this.summonerLevel = summonerLevel;
	}
}
