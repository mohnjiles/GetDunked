package com.jt.getdunked;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.jt.getdunked.ChampionData.ChampIds;
import com.jt.getdunked.ChampionData.Champion;
import com.jt.getdunked.ChampionData.Champions;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private GridView gvChamps;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			gvChamps = (GridView) rootView.findViewById(R.id.gvChamps);

			new FetchChamps(getActivity(), gvChamps).execute();

			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	static class FetchChamps extends AsyncTask<Void, Void, ChampIds> {

		private Context cxt;
		private GridView gvChamps;

		private FetchChamps(Context c, GridView gv) {
			cxt = c;
			gvChamps = gv;
		}

		@Override
		protected ChampIds doInBackground(Void... params) {

			ChampIds champIds = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/na/v1.2/champion?api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
							ChampIds.class);

			return champIds;
		}

		// GOTTA GET THE DATA AGAIN BECAUSE DERP
		// DO IT PLS
		@Override
		protected void onPostExecute(ChampIds result) {
			// DatabaseHelper db = new DatabaseHelper(cxt);
			//
			// for (Champions champ : result.getChampions()) {
			// champList.add(db.getChampion(champ.getId()));
			// }
			//
			// ImageAdapter adapter = new ImageAdapter(cxt, champList);
			// gvChamps.setAdapter(adapter);

			new SetBlurbs(cxt, result, gvChamps).execute();

			super.onPostExecute(result);
		}
	}

	static class SetBlurbs extends AsyncTask<Void, Void, Void> {

		private Context cxt;
		private ChampIds champIds;
		private GridView gvChamps;
		private List<Champion> champList = new ArrayList<Champion>();
		long startTimeMillis;

		private SetBlurbs(Context c, ChampIds champIds, GridView gv) {
			cxt = c;
			this.champIds = champIds;
			gvChamps = gv;
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			if (!checkDatabase(cxt)) {
				try {
					copyDatabase(cxt);
					Log.w("Database Helper", "Databased was copied from assets!");
					
				} catch (IOException e) {
					throw new Error("Error copying Database: " + e.getMessage());
				}
			}

			DatabaseHelper db = new DatabaseHelper(cxt);
			startTimeMillis = System.currentTimeMillis();
			for (Champions champ : champIds.getChampions()) {
//
//				Blurb blurb = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=blurb&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								Blurb.class);
//				AllyTips allyTips = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=allytips&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								AllyTips.class);
//
//				EnemyTips enemyTips = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=enemytips&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								EnemyTips.class);
//
//				ChampInfo info = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=info&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								ChampInfo.class);
//				Lore lore = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=lore&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								Lore.class);
//
//				Partype partype = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=partype&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								Partype.class);
//
//				ChampPassive passive = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=passive&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								ChampPassive.class);
//
//				RecommendedItems recommended = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=recommended&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								RecommendedItems.class);
//				ChampSkins skins = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=skins&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								ChampSkins.class);
//
//				ChampSpell spell = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=spells&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								ChampSpell.class);
//
//				ChampStats stats = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=stats&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								ChampStats.class);
//				Log.w("stats", stats.toString());
//
//				Tags tags = JsonUtil
//						.fromJsonUrl(
//								"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
//										+ champ.getId()
//										+ "?champData=tags&api_key=762d01c9-8cf4-4dc9-8eff-aa26c92685da",
//								Tags.class);
//
//				Champion champion = new Champion();
//
//				champion.setTags(tags.getTags());
//				champion.setStats(stats.getStats());
//				champion.setSpells(spell.getSpells());
//				champion.setSkins(skins.getSkins());
//				champion.setRecommended(recommended.getRecommended());
//				champion.setPassive(passive.getPassive());
//				champion.setPartype(partype.getPartype());
//				champion.setLore(lore.getLore());
//				champion.setInfo(info.getInfo());
//				champion.setAllytips(allyTips.getAllytips());
//				champion.setEnemytips(enemyTips.getEnemytips());
//				champion.setBlurb(blurb.getBlurb());
//				champion.setId(champ.getId());
//				champion.setKey(tags.getKey());
//				champion.setName(blurb.getName());
//				champion.setTitle(blurb.getTitle());
//
//				db.addChampion(champion);
				Champion champion = db.getChampionName(champ.getId());
				champList.add(champion);

				
				Log.w("Champion Found", "Champion Found: " + champion.getName());
				//Log.w("Tips", "Tips: " + champion.getName() + "// " + champion.getAllytips().toString());

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// DatabaseHelper db = new DatabaseHelper(cxt);
			//
			// int i = 0;
			// for (Champions champ : champIds.getChampions()) {
			//
			// Champion champion = db.getChampion(champ.getId());
			// champion.setBlurb(blurbList.get(i).getBlurb());
			//
			// db.updateChampion(champion);
			// i++;
			// }
			long totalTime = System.currentTimeMillis() - startTimeMillis;
			Log.w("TIMER", "Time to get from db: " + totalTime / 1000.0f + " seconds");
			ImageAdapter adapter = new ImageAdapter(cxt, champList);
			gvChamps.setAdapter(adapter);

			super.onPostExecute(result);
		}
	}
	
	private static void copyDatabase(Context dbContext) throws IOException {
		InputStream myInput = dbContext.getAssets().open("championManager");
		String outFileName = dbContext.getDatabasePath("championManager").toString();
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	private static boolean checkDatabase(Context cxt) {
		SQLiteDatabase checkDB = null;
		boolean exist = false;
		try {
			String dbPath = cxt.getDatabasePath("championManager").toString();
			checkDB = SQLiteDatabase.openDatabase(dbPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			Log.v("db log", "database does't exist");
		}

		if (checkDB != null) {
			exist = true;
			checkDB.close();
		}
		return exist;
	}

}
