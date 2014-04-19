
package com.jt.getdunked.ChampionData;

import java.util.List;

public class Info{
   	private Number attack;
   	private Number defense;
   	private Number difficulty;
   	private Number magic;

 	public Number getAttack(){
		return this.attack;
	}
	public void setAttack(Number attack){
		this.attack = attack;
	}
 	public Number getDefense(){
		return this.defense;
	}
	public void setDefense(Number defense){
		this.defense = defense;
	}
 	public Number getDifficulty(){
		return this.difficulty;
	}
	public void setDifficulty(Number difficulty){
		this.difficulty = difficulty;
	}
 	public Number getMagic(){
		return this.magic;
	}
	public void setMagic(Number magic){
		this.magic = magic;
	}
}
