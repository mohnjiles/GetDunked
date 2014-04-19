
package com.jt.getdunked.ChampionData;

import java.util.List;

public class ChampSpell{
   	private Number id;
   	private String key;
   	private String name;
   	private List spells;
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
 	public List getSpells(){
		return this.spells;
	}
	public void setSpells(List spells){
		this.spells = spells;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
}
