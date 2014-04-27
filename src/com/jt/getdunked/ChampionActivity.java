package com.jt.getdunked;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.jt.getdunked.ChampionData.Champion;
import com.jt.getdunked.ChampionData.Spell;
import com.jt.getdunked.ChampionData.Var;

public class ChampionActivity extends Activity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_champion);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.champion, menu);
		return true;
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Spells".toUpperCase(l);
			case 1:
				return "Stats".toUpperCase(l);
			case 2:
				return "Lore".toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
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

		@InjectView(R.id.tv_lore)
		TextView tvLore;
		@InjectView(R.id.tv_champ_title)
		TextView tvChampTitle;

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;

			Typeface tf = TypefaceCache.get(getActivity().getAssets(),
					"fonts/Roboto-Light.ttf");

			Intent intent = getActivity().getIntent();
			final int champId = intent.getIntExtra("id", 1);
			DatabaseHelper db = new DatabaseHelper(getActivity());
			Champion champ = db.getChampion(champId);

			db.close();

			getActivity().getActionBar().setTitle(champ.getName());
			getActivity().getActionBar().setIcon(
					Utils.getResIdByName(getActivity(), champ.getKey()
							.toLowerCase(Locale.getDefault()) + "square"));

			int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
			if (sectionNum == 1) {

				rootView = inflater.inflate(R.layout.fragment_champion,
						container, false);

				ExpandableListView lvSpells = (ExpandableListView) rootView
						.findViewById(R.id.lv_spells);
				ExpandableSpellListAdapter adapter = new ExpandableSpellListAdapter(
						getActivity(), champ);
				lvSpells.setDividerHeight(0);
				lvSpells.setDivider(null);
				lvSpells.setAdapter(adapter);

			} else if (sectionNum == 3) {
				rootView = inflater.inflate(R.layout.lore_layout, null);
				ButterKnife.inject(this, rootView);
				tvLore.setTypeface(tf);
				tvChampTitle.setTypeface(tf);

				tvLore.setText(Html.fromHtml(champ.getLore()));
				tvChampTitle.setText(champ.getName() + ", " + champ.getTitle());
			}

			return rootView;
		}
	}

	static String makeTooltip(Spell spell) {

		String tooltip = spell.getSanitizedTooltip();

		for (int i = 0; i < spell.getEffectBurn().size(); i++) {
			if (tooltip.contains("{{ e" + (i + 1) + " }}")) {
				tooltip = tooltip.replace("{{ e" + (i + 1) + " }}", spell
						.getEffectBurn().get(i));
			}
		}
		for (Var vars : spell.getVars()) {
			if (vars.getKey() != null && vars.getCoeff() != null
					&& tooltip.contains(vars.getKey())) {
				tooltip = tooltip.replace("{{ " + vars.getKey() + " }}", vars
						.getCoeff().toString());
			}
		}

		return tooltip;
	}

}
