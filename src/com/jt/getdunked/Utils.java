package com.jt.getdunked;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("DefaultLocale")
public class Utils {
	
	static int getResIdByName(Context context, String drawableName) {
		return context.getResources().getIdentifier(
				drawableName.toLowerCase(), "drawable",
				context.getPackageName());
	}

}
