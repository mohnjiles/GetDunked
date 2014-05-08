package com.jt.getdunked;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.R.integer;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jt.getdunked.summonerdata.LeagueEntry;
import com.jt.getdunked.summonerdata.PlayerStatSummaries;
import com.jt.getdunked.summonerdata.PlayerStats;
import com.jt.getdunked.summonerdata.Summoner;
import com.jt.getdunked.summonerdata.SummonerIds;

public class ProfileFragment extends Fragment {

	private int responseCodeId;
	private int responseCodeLeague;

	@InjectView(R.id.layoutProfile)
	RelativeLayout layoutProfile;
	@InjectView(R.id.progressBar)
	ProgressBar progressBar;
	@InjectView(R.id.tvFiveLeague)
	TextView tvFiveLeague;
	@InjectView(R.id.tvSoloLeague)
	TextView tvSoloLeague;
	@InjectView(R.id.tvThreeLeague)
	TextView tvThreeLeague;
	@InjectView(R.id.tvThreeLP)
	TextView tvThreeLp;
	@InjectView(R.id.tvFiveLP)
	TextView tvFiveLp;
	@InjectView(R.id.tvSoloLP)
	TextView tvSoloLp;
	@InjectView(R.id.tvThreeWins)
	TextView tvWins3v3;
	@InjectView(R.id.tvThreeLosses)
	TextView tvLosses3v3;
	@InjectView(R.id.tvFiveWins)
	TextView tvWins5v5;
	@InjectView(R.id.tvFiveLosses)
	TextView tvLosses5v5;
	@InjectView(R.id.tvSoloWins)
	TextView tvWinsSolo;
	@InjectView(R.id.tvSoloLosses)
	TextView tvLossesSolo;
	@InjectView(R.id.ivSoloFiveIcon)
	ImageView ivSoloIcon;
	@InjectView(R.id.ivTeamFiveIcon)
	ImageView ivTeamFiveIcon;
	@InjectView(R.id.ivTeamThreeIcon)
	ImageView ivTeamThreeIcon;

	private List<Integer> integers = new ArrayList<>();

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

		new SetSummonerName().execute("Nwilliams239"); // Load some data for now

		return rootView;
	}

	class SetSummonerName extends AsyncTask<String, Void, Integer> {

		Locale l = Locale.getDefault();
		private String rankTeam5v5;
		private String rankTeam3v3;
		private String rankSolo5v5;
		private int lpTeam5v5;
		private int lpTeam3v3;
		private int lpSolo5v5;
		private int numWinsTeam5v5;
		private int numWinsTeam3v3;
		private int numWinsSolo5v5;
		private int numLossesTeam5v5;
		private int numLossesTeam3v3;
		private int numLossesSolo5v5;

		@Override
		protected Integer doInBackground(String... params) {
			Log.i("ProfileFragment", "doInBackground started");
			
			Gson gson = new Gson();
			SummonerDatabaseHelper db = new SummonerDatabaseHelper(getActivity());
			
			long startTimeFromDb = System.currentTimeMillis();
			int summonerId = db.getSummonerIdByName(params[0]);
			long totalTimeFromDb = System.currentTimeMillis() - startTimeFromDb;
			Log.w("DB Query", "Query length: " + totalTimeFromDb / 1000.0f + "s, result: " + summonerId);
			

			// Take spaces out of input for URL
			params[0] = params[0].toLowerCase(l).replace(" ", "");
			
			
			if (summonerId == 0) {
				Log.w("Summoner ID", "ID for " + params[0] + " not found in database, getting from web");
				
				// URL for Summoner Name -> Summoner ID conversion
				String urlForId = "https://prod.api.pvp.net/api/lol/na/v1.4/summoner/by-name/"
						+ params[0]
						+ "?api_key=" + Api.KEY.getKey();
				String jsonResponseWithId = getJsonFromUrl(urlForId, "id").replace(
						"{\"" + params[0] + "\":", "").replace("}}", "}");
				
				try {
					summonerId = gson.fromJson(jsonResponseWithId, Summoner.class)
							.getId().intValue();
					SummonerIds summoner = new SummonerIds();
					summoner.setId(summonerId);
					summoner.setName(params[0]);
					Log.w("Summoner ID", "ID for " + params[0] + " found from web, adding to DB");
					db.addSummoner(summoner);
				} catch (JsonSyntaxException e) {
					Log.w("GSON: Summoner ID", "GSON got null from API");
					return responseCodeId;
				}
			} else {
				Log.w("Summoner ID", "ID for " + params[0] + " found in database, id: " + summonerId);
			}



			String playerStatsUrl = "https://prod.api.pvp.net/api/lol/na/v1.3/stats/by-summoner/"
					+ summonerId
					+ "/summary?season=SEASON4&api_key=" + Api.KEY.getKey();
			String leagueUrl = "https://prod.api.pvp.net/api/lol/na/v2.3/league/by-summoner/"
					+ summonerId
					+ "/entry?api_key=" + Api.KEY.getKey();
			String jsonResponseWithLeagues = getJsonFromUrl(leagueUrl, "league");
			String jsonResponseWithStats = getJsonFromUrl(playerStatsUrl,
					"stats");

			LeagueEntry[] league = null;
			PlayerStats stats = null;
			try {
				league = gson.fromJson(jsonResponseWithLeagues,
						LeagueEntry[].class);
				stats = gson.fromJson(jsonResponseWithStats, PlayerStats.class);
			} catch (JsonSyntaxException e) {
				Log.w("GSON: Leagues", "GSON got null from API");
				return responseCodeLeague;
			}

			int team5v5 = 0;
			int team3v3 = 0;
			int solo5v5 = 0;
			for (LeagueEntry entry : league) {
				switch (entry.getQueueType()) {
				case "RANKED_TEAM_5x5":
					if (team5v5 == 0) {
						rankTeam5v5 = entry.getTier() + " " + entry.getRank();
						lpTeam5v5 = entry.getLeaguePoints().intValue();
						team5v5++;
					}
					break;
				case "RANKED_TEAM_3x3":
					if (team3v3 == 0) {
						rankTeam3v3 = entry.getTier() + " " + entry.getRank();
						lpTeam3v3 = entry.getLeaguePoints().intValue();
						team3v3++;
					}
					break;
				case "RANKED_SOLO_5x5":
					if (solo5v5 == 0) {
						rankSolo5v5 = entry.getTier() + " " + entry.getRank();
						lpSolo5v5 = entry.getLeaguePoints().intValue();
						solo5v5++;
					}
					break;
				}
			}

			for (PlayerStatSummaries summary : stats.getPlayerStatSummaries()) {
				switch (summary.getPlayerStatSummaryType()) {
				case "RankedTeam3x3":
					numWinsTeam3v3 = summary.getWins().intValue();
					numLossesTeam3v3 = summary.getLosses().intValue();
					break;
				case "RankedTeam5x5":
					numWinsTeam5v5 = summary.getWins().intValue();
					numLossesTeam5v5 = summary.getLosses().intValue();
					break;
				case "RankedSolo5x5":
					numWinsSolo5v5 = summary.getWins().intValue();
					numLossesSolo5v5 = summary.getLosses().intValue();
					break;
				}
			}
			
			//getTonsOfSummoners(34500000, 34500040);

			//getTonsOfSummoners(520358, 520398);
			return responseCodeLeague;
		}

		/**
		 * Shows our progress bar (spinny thing) and hides the rest of the
		 * layout
		 * 
		 * @author JT
		 */
		@Override
		protected void onPreExecute() {
			for (int i = 0; i < layoutProfile.getChildCount(); i++) {
				layoutProfile.getChildAt(i).setVisibility(View.INVISIBLE);
			}

			progressBar.setVisibility(View.VISIBLE);

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Integer result) {

			// Un-hide everything
			for (int i = 0; i < layoutProfile.getChildCount(); i++) {
				layoutProfile.getChildAt(i).setVisibility(View.VISIBLE);
			}
			// Hide progress spinner
			progressBar.setVisibility(View.INVISIBLE);

			Log.i("ResponseCodeId", "" + responseCodeId);
			Log.i("ResponseCodeLeague", "" + responseCodeLeague);

			/* *
			 * Based on response code, do stuff: <br>200: Set text in the
			 * TextViews to values from API data <br>400+: Display the
			 * appropriate error message, based on response code.
			 */
			switch (result) {
			case 200:
				tvFiveLeague.setText(rankTeam5v5 != null ? Utils
						.toSpecialCase(rankTeam5v5) : "Unranked");
				tvSoloLeague.setText(rankSolo5v5 != null ? Utils
						.toSpecialCase(rankSolo5v5) : "Unranked");
				tvThreeLeague.setText(rankTeam3v3 != null ? Utils
						.toSpecialCase(rankTeam3v3) : "Unranked");

				tvFiveLp.setText(lpTeam5v5 + " League Points");
				tvThreeLp.setText(lpTeam3v3 + " League Points");
				tvSoloLp.setText(lpSolo5v5 + " League Points");

				tvWins5v5.setText(String.valueOf(numWinsTeam5v5));
				tvWins3v3.setText(String.valueOf(numWinsTeam3v3));
				tvWinsSolo.setText(String.valueOf(numWinsSolo5v5));

				tvLosses5v5.setText(String.valueOf(numLossesTeam5v5));
				tvLosses3v3.setText(String.valueOf(numLossesTeam3v3));
				tvLossesSolo.setText(String.valueOf(numLossesSolo5v5));

				ivSoloIcon.setImageResource(rankSolo5v5 != null ? Utils
						.getResIdByName(getActivity(),
								rankSolo5v5.toLowerCase(l).replace(" ", "_"))
						: R.drawable.unranked);
				ivTeamFiveIcon.setImageResource(rankTeam5v5 != null ? Utils
						.getResIdByName(getActivity(),
								rankTeam5v5.toLowerCase(l).replace(" ", "_"))
						: R.drawable.unranked);
				ivTeamThreeIcon.setImageResource(rankTeam3v3 != null ? Utils
						.getResIdByName(getActivity(),
								rankTeam3v3.toLowerCase(l).replace(" ", "_"))
						: R.drawable.unranked);

				break;
			case 400:
			case 401:
				Toast.makeText(getActivity(), "Failed to load!",
						Toast.LENGTH_SHORT).show();
				break;
			case 404:
				Toast.makeText(getActivity(),
						"Failed to load! League data not found",
						Toast.LENGTH_SHORT).show();
				break;
			case 429:
				Toast.makeText(getActivity(),
						"Failed to load! Rate limit exceeded",
						Toast.LENGTH_SHORT).show();
				break;
			case 500:
				Toast.makeText(
						getActivity(),
						"Failed to load! Internal server error. Try again later",
						Toast.LENGTH_SHORT).show();
				break;
			case 503:
				Toast.makeText(
						getActivity(),
						"Failed to load! Service unavailable. Try again later.",
						Toast.LENGTH_SHORT).show();
				break;
			}

			Log.i("ProfileFragment", "AsyncTask finished");
			super.onPostExecute(result);
		}
	}

	/**
	 * Function to get JSON data from API as a String
	 * 
	 * @param url
	 *            URL (as String) of the API GET request
	 * @param responseCode
	 *            String tag to determine which response code to set
	 * @return
	 */
	private String getJsonFromUrl(String url, String responseCode) {
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
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (responseCode.equals("id")) {
			responseCodeId = response.getStatusLine().getStatusCode();
		} else {
			responseCodeLeague = response.getStatusLine().getStatusCode();
		}

		return jsonResponse;
	}
	
	private void getTonsOfSummoners(int start, int startPlusForty) {
		Gson gson = new Gson();
		
		for (int i = start; i < startPlusForty; i++) {
			integers.add(i);
		}
		Log.w("doInBackground", "Integers added");

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {

			Iterator<Integer> iterator = integers.iterator();
			StringBuilder sb = new StringBuilder();
			sb.append("https://prod.api.pvp.net/api/lol/na/v1.4/summoner/");

			while (iterator.hasNext()) {
				sb.append(iterator.next());
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}

			sb.append("?api_key=" + Api.KEY.getKey());
			String url = sb.toString();

			//Log.w("URL", "" + url);

			String json = getJsonFromUrl(url, "id");
			for (int j = 0; j < 40; j++) {
				json = json.replace("{\"" + (integers.get(j)) + "\":", "[")
						.replace("}}", "}]")
						.replace("\"" + (integers.get(j)) + "\":", "")
						.replace("{\"status\": ", "[");
			}
			//Log.w("JSON", json + "");
			if (!json.contains("<html>")) {
				SummonerIds[] ids = gson
						.fromJson(json, SummonerIds[].class);
				SummonerDatabaseHelper db = new SummonerDatabaseHelper(
						getActivity());

				boolean shouldGoBack = false;
				for (SummonerIds id : ids) {
					if (id.getStatus_code() != null) {
						try {
							long timeToSleep = 11000 - (System
									.currentTimeMillis() - startTime);
							Log.w("Sleep", "Start sleeping for "
									+ timeToSleep / 1000.0f + "s");
							if (timeToSleep > 0) {
								Thread.sleep(timeToSleep);
							}
							shouldGoBack = true;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if ((System.currentTimeMillis() - id.getRevisionDate()) / 1000.0f < 20000000){
						db.addSummoner(id);
						shouldGoBack = false;
					}
				}

				if (!shouldGoBack) {
					for (int q = 0; q < 40; q++) {
						integers.set(q, integers.get(q) - 40);
					}
				} else {
					startTime = System.currentTimeMillis();
				}
			} else {
				for (int q = 0; q < 40; q++) {
					integers.set(q, integers.get(q) - 40);
				}
			}

		}
	}
}