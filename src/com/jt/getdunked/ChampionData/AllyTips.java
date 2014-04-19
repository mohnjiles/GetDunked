
package com.jt.getdunked.ChampionData;

import java.util.List;

public class AllyTips{
   	private List<?> allytips;
   	private Number id;
   	private String key;
   	private String name;
   	private String title;
   	
   	public AllyTips() {
   		
   	}
   	
   	public AllyTips(List<?> allytips, Number id, String key, String name, String title) {
   		this.allytips = allytips;
   		this.id = id;
   		this.key = key;
   		this.name = name;
   		this.title = title;
   	}

 	public List<?> getAllytips(){
		return this.allytips;
	}
	public void setAllytips(List<?> allytips){
		this.allytips = allytips;
	}
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
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
}
