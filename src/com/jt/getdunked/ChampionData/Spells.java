
package com.jt.getdunked.championdata;

import java.util.List;

public class Spells{
   	private List cooldown;
   	private String cooldownBurn;
   	private List cost;
   	private String costBurn;
   	private String costType;
   	private String description;
   	private List effect;
   	private List effectBurn;
   	private Image image;
   	private String key;
   	private LevelTip leveltip;
   	private Number maxrank;
   	private String name;
   	private List range;
   	private String rangeBurn;
   	private String resource;
   	private String sanitizedDescription;
   	private String sanitizedTooltip;
   	private String tooltip;
   	private List vars;

 	public List getCooldown(){
		return this.cooldown;
	}
	public void setCooldown(List cooldown){
		this.cooldown = cooldown;
	}
 	public String getCooldownBurn(){
		return this.cooldownBurn;
	}
	public void setCooldownBurn(String cooldownBurn){
		this.cooldownBurn = cooldownBurn;
	}
 	public List getCost(){
		return this.cost;
	}
	public void setCost(List cost){
		this.cost = cost;
	}
 	public String getCostBurn(){
		return this.costBurn;
	}
	public void setCostBurn(String costBurn){
		this.costBurn = costBurn;
	}
 	public String getCostType(){
		return this.costType;
	}
	public void setCostType(String costType){
		this.costType = costType;
	}
 	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
 	public List getEffect(){
		return this.effect;
	}
	public void setEffect(List effect){
		this.effect = effect;
	}
 	public List getEffectBurn(){
		return this.effectBurn;
	}
	public void setEffectBurn(List effectBurn){
		this.effectBurn = effectBurn;
	}
 	public Image getImage(){
		return this.image;
	}
	public void setImage(Image image){
		this.image = image;
	}
 	public String getKey(){
		return this.key;
	}
	public void setKey(String key){
		this.key = key;
	}
 	public LevelTip getLeveltip(){
		return this.leveltip;
	}
	public void setLeveltip(LevelTip leveltip){
		this.leveltip = leveltip;
	}
 	public Number getMaxrank(){
		return this.maxrank;
	}
	public void setMaxrank(Number maxrank){
		this.maxrank = maxrank;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public List getRange(){
		return this.range;
	}
	public void setRange(List range){
		this.range = range;
	}
 	public String getRangeBurn(){
		return this.rangeBurn;
	}
	public void setRangeBurn(String rangeBurn){
		this.rangeBurn = rangeBurn;
	}
 	public String getResource(){
		return this.resource;
	}
	public void setResource(String resource){
		this.resource = resource;
	}
 	public String getSanitizedDescription(){
		return this.sanitizedDescription;
	}
	public void setSanitizedDescription(String sanitizedDescription){
		this.sanitizedDescription = sanitizedDescription;
	}
 	public String getSanitizedTooltip(){
		return this.sanitizedTooltip;
	}
	public void setSanitizedTooltip(String sanitizedTooltip){
		this.sanitizedTooltip = sanitizedTooltip;
	}
 	public String getTooltip(){
		return this.tooltip;
	}
	public void setTooltip(String tooltip){
		this.tooltip = tooltip;
	}
 	public List getVars(){
		return this.vars;
	}
	public void setVars(List vars){
		this.vars = vars;
	}
}
