
package com.jt.getdunked.ChampionData;

import java.util.List;

public class ChampSkins{
   	private Number id;
   	private String key;
   	private String name;
   	private List skins;
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
 	public List getSkins(){
		return this.skins;
	}
	public void setSkins(List skins){
		this.skins = skins;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
}
