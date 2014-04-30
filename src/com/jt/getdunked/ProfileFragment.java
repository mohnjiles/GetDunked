package com.jt.getdunked;

import java.io.IOException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.gson.Gson;
import com.jt.getdunked.SummonerData.Summoner;

public class ProfileFragment extends Fragment {

	@InjectView(R.id.section_label)
	TextView textView;

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ProfileFragment newInstance(int sectionNumber) {
		ProfileFragment fragment = new ProfileFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);

		ButterKnife.inject(this, rootView);

		new SetSummonerName().execute("SkögUL");

		return rootView;
	}

	public class SetSummonerName extends AsyncTask<String, Void, Integer> {

		Locale l = Locale.getDefault();
		
		@Override
		protected Integer doInBackground(String... params) {
			Summoner summoner = new Summoner();
			String url = "https://prod.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" + params[0].toLowerCase(l) + "?api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da";
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpEntity entity = null;
			HttpResponse response = null;
			String jsonResponse = "";
			
			HttpGet get = new HttpGet(url);
			try {
				response = client.execute(get);
				entity = response.getEntity();
				jsonResponse = EntityUtils.toString(entity);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jsonResponse = jsonResponse.replace("{\"" + params[0].toLowerCase(l) + "\":", "").replace("}}", "}");
			
			Gson gson = new Gson();
			summoner = gson.fromJson(jsonResponse, Summoner.class);
			Log.w("ProfileFragment", "AsyncTask started -- id: " + summoner.getId());
			return summoner.getId().intValue();
		}

		@Override
		protected void onPostExecute(Integer result) {
			textView.setText(result.toString());
			Log.w("ProfileFragment", "AsyncTask finished");
			super.onPostExecute(result);
		}
	}
}