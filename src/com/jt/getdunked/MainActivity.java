package com.jt.getdunked;

import it.gmariotti.cardslib.library.view.CardView;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;

import com.jt.getdunked.championdata.AllyTips;
import com.jt.getdunked.championdata.Blurb;
import com.jt.getdunked.championdata.ChampIds;
import com.jt.getdunked.championdata.ChampInfo;
import com.jt.getdunked.championdata.ChampPassive;
import com.jt.getdunked.championdata.ChampSkins;
import com.jt.getdunked.championdata.ChampSpell;
import com.jt.getdunked.championdata.ChampStats;
import com.jt.getdunked.championdata.Champion;
import com.jt.getdunked.championdata.Champions;
import com.jt.getdunked.championdata.EnemyTips;
import com.jt.getdunked.championdata.Lore;
import com.jt.getdunked.championdata.Partype;
import com.jt.getdunked.championdata.RecommendedItems;
import com.jt.getdunked.championdata.Tags;

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
	private static SearchView searchItem;
	private static List<Champion> listChamps = new ArrayList<Champion>();
	private static ImageAdapter adapter;
	private ChampionDatabaseHelper db;

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

		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.argb(255, 0, 153, 204)));

		db = new ChampionDatabaseHelper(this);
		listChamps = db.getAllChampions();
		db.close();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments

		Bundle args = new Bundle();
		args.putInt("position", position);

		switch (position) {
		case 1:
			startActivity(new Intent(MainActivity.this, ProfileActivity.class));
			break;
		default:
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(args)).commit();
			break;
		}
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

			searchItem = (SearchView) menu.findItem(R.id.action_search)
					.getActionView();
			searchItem.setOnQueryTextListener(new ChampionSearchListener(
					listChamps, adapter));
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
		public static PlaceholderFragment newInstance(Bundle args) {
			PlaceholderFragment fragment = new PlaceholderFragment();
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
			adapter = new ImageAdapter(getActivity(), listChamps);
			gvChamps.setAdapter(adapter);

			gvChamps.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					ChampionDatabaseHelper db = new ChampionDatabaseHelper(getActivity());

					Log.w("onItemClick", "id: " + db.getChampionIdByPos(arg2));

					Intent intent = new Intent(getActivity(),
							ChampionActivity.class);
					intent.putExtra("id",
							(Integer) arg1.findViewById(R.id.grid_item_image)
									.getTag());
					startActivity(intent);

					db.close();
				}
			});

			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {

			super.onActivityCreated(savedInstanceState);
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	static class GetChampionsForGrid extends
			AsyncTask<Void, Void, List<Champion>> {

		private Context cxt;
		private GridView gvChamps;
		long startTimeMillis;

		private GetChampionsForGrid(Context c, GridView gv, CardView lv) {
			cxt = c;
			gvChamps = gv;
		}

		@Override
		protected List<Champion> doInBackground(Void... params) {

			ChampionDatabaseHelper db = new ChampionDatabaseHelper(cxt);
			startTimeMillis = System.currentTimeMillis();

			return db.getAllChampions();
		}

		@Override
		protected void onPostExecute(List<Champion> result) {

			long totalTime = System.currentTimeMillis() - startTimeMillis;
			Log.w("TIMER", "Time to get from db: " + totalTime / 1000.0f
					+ " seconds");
			listChamps = result;
			adapter = new ImageAdapter(cxt, result);
			gvChamps.setAdapter(adapter);

			super.onPostExecute(result);
		}
	}

	@SuppressWarnings("unused")
	private void addAllChampionsToDatabase(ChampIds champIds) {

		ChampionDatabaseHelper db = new ChampionDatabaseHelper(this);

		for (Champions champ : champIds.getChampions()) {

			Blurb blurb = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=blurb&api_key=" + Api.KEY.getKey(),
							Blurb.class);
			AllyTips allyTips = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=allytips&api_key=" + Api.KEY.getKey(),
							AllyTips.class);

			EnemyTips enemyTips = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=enemytips&api_key=" + Api.KEY.getKey(),
							EnemyTips.class);

			ChampInfo info = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=info&api_key=" + Api.KEY.getKey(),
							ChampInfo.class);
			Lore lore = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=lore&api_key=" + Api.KEY.getKey(),
							Lore.class);

			Partype partype = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=partype&api_key=" + Api.KEY.getKey(),
							Partype.class);

			ChampPassive passive = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=passive&api_key=" + Api.KEY.getKey(),
							ChampPassive.class);

			RecommendedItems recommended = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=recommended&api_key=" + Api.KEY.getKey(),
							RecommendedItems.class);
			ChampSkins skins = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=skins&api_key=" + Api.KEY.getKey(),
							ChampSkins.class);

			ChampSpell spell = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=spells&api_key=" + Api.KEY.getKey(),
							ChampSpell.class);

			ChampStats stats = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=stats&api_key=" + Api.KEY.getKey(),
							ChampStats.class);
			Log.w("stats", stats.toString());

			Tags tags = JsonUtil
					.fromJsonUrl(
							"https://prod.api.pvp.net/api/lol/static-data/na/v1.2/champion/"
									+ champ.getId()
									+ "?champData=tags&api_key=" + Api.KEY.getKey(),
							Tags.class);

			Champion champion = new Champion();

			champion.setTags(tags.getTags());
			champion.setStats(stats.getStats());
			champion.setSpells(spell.getSpells());
			champion.setSkins(skins.getSkins());
			champion.setRecommended(recommended.getRecommended());
			champion.setPassive(passive.getPassive());
			champion.setPartype(partype.getPartype());
			champion.setLore(lore.getLore());
			champion.setInfo(info.getInfo());
			champion.setAllytips(allyTips.getAllytips());
			champion.setEnemytips(enemyTips.getEnemytips());
			champion.setBlurb(blurb.getBlurb());
			champion.setId(champ.getId());
			champion.setKey(tags.getKey());
			champion.setName(blurb.getName());
			champion.setTitle(blurb.getTitle());

			db.addChampion(champion);

			Log.w("Champion Found", "Champion Found: " + champion.getName());

		}
	}

}