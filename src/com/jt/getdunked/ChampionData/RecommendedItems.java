
package com.jt.getdunked.ChampionData;

import java.util.List;

public class RecommendedItems{
   	private Number id;
   	private String key;
   	private String name;
   	private List recommended;
   	private String title;

 	public Number getId(){
		return this.id;
	}
	public void setId(Number id){
		this.id = id;
	}
 	public String getKey(){
		return this.key;
	}
	public void setKey(String key){
		this.key = key;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public List getRecommended(){
		return this.recommended;
	}
	public void setRecommended(List recommended){
		this.recommended = recommended;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
}
