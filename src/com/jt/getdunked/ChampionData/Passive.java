
package com.jt.getdunked.championdata;


public class Passive{
   	private String description;
   	private Image image;
   	private String name;
   	private String sanitizedDescription;

 	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
 	public Image getImage(){
		return this.image;
	}
	public void setImage(Image image){
		this.image = image;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public String getSanitizedDescription(){
		return this.sanitizedDescription;
	}
	public void setSanitizedDescription(String sanitizedDescription){
		this.sanitizedDescription = sanitizedDescription;
	}
}
