
package com.jt.getdunked.ChampionData;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class SpellVars {

    
    private String key;
    
    private String link;
    
    private double coeff;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public double getCoeff() {
        return coeff;
    }

    public void setCoeff(double coeff) {
        this.coeff = coeff;
    }

}
