package com.jt.getdunked;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.widget.SearchView.OnQueryTextListener;

import com.jt.getdunked.ChampionData.Champion;

public class ChampionSearchListener implements OnQueryTextListener {

	private List<Champion> champList = new ArrayList<Champion>();
	private ImageAdapter adapter;
	private List<Champion> newChampList = new ArrayList<Champion>();

	public ChampionSearchListener(List<Champion> champList,
			ImageAdapter adapter) {
		this.champList.addAll(champList);
		this.adapter = adapter;
	}

	public boolean onQueryTextChange(String newText) {
		
		if (!newChampList.isEmpty())
			newChampList.clear();
		
		for (Champion champ : champList) {
			if (champ.getName().toLowerCase(Locale.getDefault()).contains(newText.toLowerCase())) {
				newChampList.add(champ);
			}
		}
		
		adapter.update(newChampList);
		
		return false;
	}

	public boolean onQueryTextSubmit(String query) {

		return true;
	}

	public boolean onClose() {
		return false;
	}

	protected boolean isAlwaysExpanded() {
		return false;
	}

}
