
package com.jt.getdunked.ChampionData;

import java.util.List;

public class MetaChamp {
   	private List allytips;
   	private String blurb;
   	private List enemytips;
   	private Number id;
   	private Image image;
   	private Info info;
   	private String key;
   	private String lore;
   	private String name;
   	private String partype;
   	private Passive passive;
   	private List recommended;
   	private List skins;
   	private List spells;
   	private Stats stats;
   	private List tags;
   	private String title;

 	public List getAllytips(){
		return this.allytips;
	}
	public void setAllytips(List allytips){
		this.allytips = allytips;
	}
 	public String getBlurb(){
		return this.blurb;
	}
	public void setBlurb(String blurb){
		this.blurb = blurb;
	}
 	public List getEnemytips(){
		return this.enemytips;
	}
	public void setEnemytips(List enemytips){
		this.enemytips = enemytips;
	}
 	public Number getId(){
		return this.id;
	}
	public void setId(Number id){
		this.id = id;
	}
 	public Image getImage(){
		return this.image;
	}
	public void setImage(Image image){
		this.image = image;
	}
 	public Info getInfo(){
		return this.info;
	}
	public void setInfo(Info info){
		this.info = info;
	}
 	public String getKey(){
		return this.key;
	}
	public void setKey(String key){
		this.key = key;
	}
 	public String getLore(){
		return this.lore;
	}
	public void setLore(String lore){
		this.lore = lore;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public String getPartype(){
		return this.partype;
	}
	public void setPartype(String partype){
		this.partype = partype;
	}
 	public Passive getPassive(){
		return this.passive;
	}
	public void setPassive(Passive passive){
		this.passive = passive;
	}
 	public List getRecommended(){
		return this.recommended;
	}
	public void setRecommended(List recommended){
		this.recommended = recommended;
	}
 	public List getSkins(){
		return this.skins;
	}
	public void setSkins(List skins){
		this.skins = skins;
	}
 	public List getSpells(){
		return this.spells;
	}
	public void setSpells(List spells){
		this.spells = spells;
	}
 	public Stats getStats(){
		return this.stats;
	}
	public void setStats(Stats stats){
		this.stats = stats;
	}
 	public List getTags(){
		return this.tags;
	}
	public void setTags(List tags){
		this.tags = tags;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
}
