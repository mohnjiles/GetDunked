package com.jt.getdunked;

import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jt.getdunked.ChampionData.Champion;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private List<Champion> listChamps;

	public ImageAdapter(Context c, List<Champion> listChamps) {
		mContext = c;
		this.listChamps = listChamps;

		Collections.sort(listChamps, new TipsComparator());
	}

	@Override
	public int getCount() {
		return listChamps.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// Custom ViewHolder class to make scrolling smoother
	static class ViewHolder {
		ImageView iv = null;
		TextView tv = null;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;

		// If our view (in this case, one item from the gridview) is null, then
		// inflate it.
		// We do this because re-using views makes memory happy :)
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.grid_layout, null);
			vh = new ViewHolder();
			vh.tv = (TextView) convertView.findViewById(R.id.grid_item_text);
			vh.iv = (ImageView) convertView.findViewById(R.id.grid_item_image);
			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.tv.setText(listChamps.get(position).getName());
		vh.iv.setImageResource(Utils.getResIdByName(mContext,
				listChamps.get(position).getKey().toLowerCase() + "square"));

		return convertView;

	}

}
