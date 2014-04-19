
package com.jt.getdunked.ChampionData;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class ChampIds {

    
    private List<Champions> champions = new ArrayList<Champions>();

    public List<Champions> getChampions() {
        return champions;
    }

    public void setChampions(List<Champions> champions) {
        this.champions = champions;
    }

}
