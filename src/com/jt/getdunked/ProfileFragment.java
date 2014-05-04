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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jt.getdunked.SummonerData.Entries;
import com.jt.getdunked.SummonerData.League;
import com.jt.getdunked.SummonerData.PlayerStatSummaries;
import com.jt.getdunked.SummonerData.PlayerStats;
import com.jt.getdunked.SummonerData.Summoner;

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

	public class SetSummonerName extends AsyncTask<String, Void, Integer> {

		Locale l = Locale.getDefault();
		private String rankedTeam5v5;
		private String rankedTeam3v3;
		private String rankedSolo5v5;
		private int lpTeam5v5;
		private int lpTeam3v3;
		private int lpSolo5v5;
		private int winsTeam5v5;
		private int winsTeam3v3;
		private int winsSolo5v5;
		private int lossesTeam5v5;
		private int lossesTeam3v3;
		private int lossesSolo5v5;

		@Override
		protected Integer doInBackground(String... params) {
			Gson gson = new Gson();
			int summonerId = 0;

			Log.i("ProfileFragment", "AsyncTask started");

			// Take spaces out of input for URL
			params[0] = params[0].replace(" ", "");

			// URL for Summoner Name -> Summoner ID conversion
			String urlForId = "https://prod.api.pvp.net/api/lol/na/v1.4/summoner/by-name/"
					+ params[0].toLowerCase(l)
					+ "?api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da";
			String jsonResponseWithId = getJsonFromUrl(urlForId, "id").replace(
					"{\"" + params[0].toLowerCase(l) + "\":", "").replace("}}",
					"}");
			try {
				summonerId = gson.fromJson(jsonResponseWithId, Summoner.class)
						.getId().intValue();
			} catch (JsonSyntaxException e) {
				Log.w("GSON: Summoner ID", "GSON got null from API");
				return responseCodeId;
			}

			String playerStatsUrl = "https://prod.api.pvp.net/api/lol/na/v1.3/stats/by-summoner/"
					+ summonerId
					+ "/summary?season=SEASON4&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da";
			String leagueUrl = "https://prod.api.pvp.net/api/lol/na/v2.3/league/by-summoner/"
					+ summonerId
					+ "?api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da";
			String jsonResponseWithLeagues = getJsonFromUrl(leagueUrl, "league");
			String jsonResponseWithStats = getJsonFromUrl(playerStatsUrl,
					"stats");

			League[] leagues = null;
			PlayerStats stats = null;
			try {
				leagues = gson
						.fromJson(jsonResponseWithLeagues, League[].class);
				stats = gson.fromJson(jsonResponseWithStats, PlayerStats.class);
			} catch (JsonSyntaxException e) {
				Log.w("GSON: Leagues", "GSON got null from API");
				return responseCodeLeague;
			}
			

			for (League league : leagues) {
				switch (league.getQueue()) {
				case "RANKED_TEAM_5x5":
					for (Entries entry : league.getEntries()) {
						if (entry.getPlayerOrTeamId().equals(
								league.getParticipantId())) {
							rankedTeam5v5 = entry.getTier() + " "
									+ entry.getRank();
							lpTeam5v5 = entry.getLeaguePoints().intValue();
						}
					}
					break;
				case "RANKED_TEAM_3x3":
					for (Entries entry : league.getEntries()) {
						if (entry.getPlayerOrTeamId().equals(
								league.getParticipantId())) {
							rankedTeam3v3 = entry.getTier() + " "
									+ entry.getRank();
							lpTeam3v3 = entry.getLeaguePoints().intValue();
						}
					}
					break;
				case "RANKED_SOLO_5x5":
					for (Entries entry : league.getEntries()) {
						if (entry.getPlayerOrTeamId().equals(
								league.getParticipantId())) {
							rankedSolo5v5 = entry.getTier() + " "
									+ entry.getRank();
							lpSolo5v5 = entry.getLeaguePoints().intValue();
						}
					}
					break;
				}
			}

			for (PlayerStatSummaries summary : stats.getPlayerStatSummaries()) {
				switch (summary.getPlayerStatSummaryType()) {
				case "RankedTeam3x3":
					winsTeam3v3 = summary.getWins().intValue();
					lossesTeam3v3 = summary.getLosses().intValue();
					break;
				case "RankedTeam5x5":
					winsTeam5v5 = summary.getWins().intValue();
					lossesTeam5v5 = summary.getLosses().intValue();
					break;
				case "RankedSolo5x5":
					winsSolo5v5 = summary.getWins().intValue();
					lossesSolo5v5 = summary.getLosses().intValue();
					break;
				}
			}

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
			// Hide progress bar
			progressBar.setVisibility(View.INVISIBLE);

			Log.i("ResponseCodeId", "" + responseCodeId);
			Log.i("ResponseCodeLeague", "" + responseCodeLeague);

			/**
			 * Based on response code, do stuff: 200: Set text in the TextViews
			 * to values from API data 400+: Display the appropriate error
			 * message, based on response code.
			 */
			switch (result) {
			case 200:
				tvFiveLeague.setText(rankedTeam5v5 != null ? Utils
						.toSpecialCase(rankedTeam5v5) : "Unranked");
				tvSoloLeague.setText(rankedSolo5v5 != null ? Utils
						.toSpecialCase(rankedSolo5v5) : "Unranked");
				tvThreeLeague.setText(rankedTeam3v3 != null ? Utils
						.toSpecialCase(rankedTeam3v3) : "Unranked");

				tvFiveLp.setText(lpTeam5v5 + " League Points");
				tvThreeLp.setText(lpTeam3v3 + " League Points");
				tvSoloLp.setText(lpSolo5v5 + " League Points");

				tvWins5v5.setText(String.valueOf(winsTeam5v5));
				tvWins3v3.setText(String.valueOf(winsTeam3v3));
				tvWinsSolo.setText(String.valueOf(winsSolo5v5));

				tvLosses5v5.setText(String.valueOf(lossesTeam5v5));
				tvLosses3v3.setText(String.valueOf(lossesTeam3v3));
				tvLossesSolo.setText(String.valueOf(lossesSolo5v5));

				ivSoloIcon.setImageResource(rankedSolo5v5 != null ? Utils
						.getResIdByName(getActivity(), rankedSolo5v5
								.toLowerCase(l).replace(" ", "_"))
						: R.drawable.unranked);
				ivTeamFiveIcon.setImageResource(rankedTeam5v5 != null ? Utils
						.getResIdByName(getActivity(), rankedTeam5v5
								.toLowerCase(l).replace(" ", "_"))
						: R.drawable.unranked);
				ivTeamThreeIcon.setImageResource(rankedTeam3v3 != null ? Utils
						.getResIdByName(getActivity(), rankedSolo5v5
								.toLowerCase(l).replace(" ", "_"))
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
}