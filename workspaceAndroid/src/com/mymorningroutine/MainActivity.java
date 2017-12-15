package com.mymorningroutine;

import java.util.ArrayList;
import java.util.Locale;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.mymorningroutine.obj.DrawerItem;
import com.mymorningroutine.server.ActivityTable;
import com.mymorningroutine.server.RoutineTable;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends  FragmentActivity implements ActionBar.TabListener {
	public static final Integer CURRENT_ROUTINE_FRAGMENT_POSITION = 0;
	public static final Integer ACTIVITIES_FRAGMENT_POSITION = 1;
	public static final Integer SAVED_ROUTINES_FRAGMENT_POSITION = 2;

	private static SectionsPagerAdapter mSectionsPagerAdapter;
	ActivityTable activityTable;
	RoutineTable routineTable;
	SharedPreferences prefs = null;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private DrawerLayout mDrawerLayout;

	private ListView mDrawerList;

	private String[] mDrawerValues;

	private TypedArray navMenuIcons;

	private ArrayList<DrawerItem> navDrawerItems;

	private DrawerItemAdapter adapter;

	private String TAG;

	public static final int START_NEXT_ACTIVITY_RESULT_CODE = 35;

	public static final int STOP_ROUTINE_RESULT_CODE = 36;

	private MenuItem addMenuButton;

	@Override
	public void onTabReselected(ActionBar.Tab p1, FragmentTransaction p2) {
		
	}

	
	public static void reloadFragmentActivities() {
		CurrentRoutineSectionFragment routineFragment = (CurrentRoutineSectionFragment) mSectionsPagerAdapter
				.getItem(CURRENT_ROUTINE_FRAGMENT_POSITION);
		routineFragment.loadActivities(-2, true);

		ActivitySectionFragment activitiesFragment = (ActivitySectionFragment) mSectionsPagerAdapter
				.getItem(ACTIVITIES_FRAGMENT_POSITION);
		activitiesFragment.loadActivities();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		
	//	setTitleColor(getResources().getColor(R.drawable.home_actionbar_background));

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		prefs = getSharedPreferences("com.mymorningroutine.AlwaysOnTime", MODE_PRIVATE);
		/*
		 * Build Drawer
		 */

		 
		
		mDrawerValues = getResources().getStringArray(R.array.drawer_values);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		navMenuIcons = getResources().obtainTypedArray(R.array.drawer_icons);

		navDrawerItems = new ArrayList<DrawerItem>();

		// adding nav drawer items to array
		navDrawerItems.add(new DrawerItem(mDrawerValues[0], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new DrawerItem(mDrawerValues[1], null));
		navDrawerItems.add(new DrawerItem(mDrawerValues[2], null));
		navDrawerItems.add(new DrawerItem(mDrawerValues[3], navMenuIcons.getResourceId(3, -1)));
		// navDrawerItems.add(new DrawerItem(mDrawerValues[4],
		// navMenuIcons.getResourceId(4,-1)));
		navDrawerItems.add(new DrawerItem(mDrawerValues[5], navMenuIcons.getResourceId(5, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();
		// setting the nav drawer list adapter
		adapter = new DrawerItemAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		//getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), savedInstanceState);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		setTitle(mSectionsPagerAdapter.getPageTitle(0));
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// actionBar.setSelectedNavigationItem(position);
				setTitle(mSectionsPagerAdapter.getPageTitle(position));
				hidePlayMenuButton(position);
				// mSectionsPagerAdapter.getItem(position).refreshFragment();
			}

		});
	
		

		 

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}


	}


	/**
	 *
	 * @param position
	 */
	private void hidePlayMenuButton(int position) {
		if (addMenuButton != null) {
			if (position != CURRENT_ROUTINE_FRAGMENT_POSITION) {
				addMenuButton.setVisible(false);
			} else {
				addMenuButton.setVisible(true);

			}
		}
	}

	public ActivityTable getActivityTable() {
		if (activityTable == null) {
			activityTable = Singleton.getActivityTable(getApplicationContext());
		}
		return activityTable;
	}

	public RoutineTable getRoutineTable() {
		if (routineTable == null) {
			routineTable = Singleton.getRoutineTable(getApplicationContext());
		}
		return routineTable;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e(TAG, resultCode + " resultCode");
		Log.e(TAG, "MainActivity");
		if (requestCode == MainActivity.START_NEXT_ACTIVITY_RESULT_CODE && data != null) {
			String action = data.getStringExtra("action");
			if ("startNextActivity".equals(action)) {
				getRoutineFragment().startNextActivity();

			} else if ("endRoutine".equals(action)) {
				getRoutineFragment().endRoutine();

			} else if ("refreshRoutine".equals(action)) {
				((RefreshableFragment) mSectionsPagerAdapter.getItem(SAVED_ROUTINES_FRAGMENT_POSITION))
						.refreshFragment();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		addMenuButton = menu.getItem(0);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(TAG, "onOptionsItemSelected :" + item.getTitle());
		Integer menuItemId = item.getItemId();
		if (menuItemId == R.id.action_refresh_current_routine){
				
			((RefreshableFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem())).refreshFragment();
		} else if (menuItemId ==  android.R.id.home){
			if(mDrawerLayout.isDrawerOpen(mDrawerList)){
				mDrawerLayout.closeDrawer(mDrawerList);
			}else{
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}else if (menuItemId ==    R.id.action_switch_view){

			/*
			 * if(mViewPager.getCurrentItem() ==
			 * CURRENT_ROUTINE_FRAGMENT_POSITION){ mSectionsPagerAdapter =
			 * (MainActivity.SectionsPagerAdapter) mViewPager.getAdapter();
			 * if(mSectionsPagerAdapter.getItem(
			 * CURRENT_ROUTINE_FRAGMENT_POSITION) != null){
			 * ((CurrentRoutineSectionFragment)
			 * mSectionsPagerAdapter.getItem(CURRENT_ROUTINE_FRAGMENT_POSITION))
			 * .startRoutine(); }else{ mSectionsPagerAdapter = new
			 * SectionsPagerAdapter(getSupportFragmentManager(),null); } }
			 */
			 
			 
				float density = getResources().getDisplayMetrics().density;
				Toast.makeText(getApplicationContext(), "" + density, Toast.LENGTH_LONG);
				System.out.println(density);
				
			((RefreshableFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem())).addItems();


		/*
		 * case R.id.action_load_daily_activities:
		 * getRoutineFragment().loadDailyActivities(); break; case
		 * R.id.action_load_saved_routine:
		 * 
		 * Intent intentRoutine = new Intent(getApplicationContext(),
		 * DialogRoutines.class); startActivityForResult(intentRoutine, 3);
		 * 
		 * 
		 * break;
		 */
		}
		return super.onOptionsItemSelected(item);
	}

	public CurrentRoutineSectionFragment getRoutineFragment() {
		return (CurrentRoutineSectionFragment) mSectionsPagerAdapter.getItem(CURRENT_ROUTINE_FRAGMENT_POSITION);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
		setTitle(mSectionsPagerAdapter.getPageTitle(tab.getPosition()));
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

	}


	@Override
	protected void onResume() {
		super.onResume();

		if (prefs.getBoolean("firstrun", true)) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					Intent aboutActivity = new Intent();
					aboutActivity.setClass(MainActivity.this, AboutActivity.class);
					startActivity(aboutActivity);
					prefs.edit().putBoolean("firstrun", false).commit();
					dialogInterface.cancel();
				}
			});
			alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.cancel();
				}
			});
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setMessage(R.string.getting_started);
		}

		if (addMenuButton != null) {
			if (mViewPager.getCurrentItem() != CURRENT_ROUTINE_FRAGMENT_POSITION) {
				addMenuButton.setVisible(false);
			} else {
				addMenuButton.setVisible(true);

			}
		}
	}

	@Override
	protected void onStart() {
        super.onStart();
        //	AdView adView = new AdView(this, com.google.ads.AdSize.BANNER, "ca-app-pub-7999813655817518/6459194989");
        AdView adView = (AdView) findViewById(R.id.ad);

        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            // Prompt the user to install/update/enable Google Play services.
            GooglePlayServicesUtil.showErrorNotification(
                    e.getConnectionStatusCode(), getApplicationContext());

        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.linear_layout_main);

        // Initiate a generic request to load it with an ad
        AdRequest.Builder builder = new AdRequest.Builder();
       
        builder.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
        AdRequest adRequest = builder.build();

        adView.loadAd(adRequest);
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mSectionsPagerAdapter.getItem(CURRENT_ROUTINE_FRAGMENT_POSITION) != null
				&& mSectionsPagerAdapter.getItem(CURRENT_ROUTINE_FRAGMENT_POSITION).isAdded())
			getSupportFragmentManager().putFragment(outState, CurrentRoutineSectionFragment.class.getName(),
					mSectionsPagerAdapter.getItem(CURRENT_ROUTINE_FRAGMENT_POSITION));

		if (mSectionsPagerAdapter.getItem(ACTIVITIES_FRAGMENT_POSITION) != null
				&& mSectionsPagerAdapter.getItem(ACTIVITIES_FRAGMENT_POSITION).isAdded())
			getSupportFragmentManager().putFragment(outState, ActivitySectionFragment.class.getName(),
					mSectionsPagerAdapter.getItem(ACTIVITIES_FRAGMENT_POSITION));

		if (mSectionsPagerAdapter.getItem(SAVED_ROUTINES_FRAGMENT_POSITION) != null
				&& mSectionsPagerAdapter.getItem(SAVED_ROUTINES_FRAGMENT_POSITION).isAdded())
			getSupportFragmentManager().putFragment(outState, RoutineSectionFragment.class.getName(),
					mSectionsPagerAdapter.getItem(SAVED_ROUTINES_FRAGMENT_POSITION));

		super.onSaveInstanceState(outState);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private SparseArray <Fragment> fragmentList = new SparseArray<Fragment>();

		public SectionsPagerAdapter(FragmentManager fm, Bundle bundle) {
			super(fm);

			if (bundle != null) {
				Log.e(TAG, "bundle is not null");
				fragmentList.clear();
				if (fm.getFragment(bundle, CurrentRoutineSectionFragment.class.getClass().getName()) != null) {
					fragmentList.put(CURRENT_ROUTINE_FRAGMENT_POSITION,
							fm.getFragment(bundle, CurrentRoutineSectionFragment.class.getClass().getName()));
				} else {
					fragmentList.put(CURRENT_ROUTINE_FRAGMENT_POSITION, new CurrentRoutineSectionFragment());
				}

				if (fm.getFragment(bundle, ActivitySectionFragment.class.getClass().getName()) != null) {
					fragmentList.put(ACTIVITIES_FRAGMENT_POSITION,
							fm.getFragment(bundle, ActivitySectionFragment.class.getClass().getName()));
				} else {
					fragmentList.put(ACTIVITIES_FRAGMENT_POSITION, new ActivitySectionFragment());
				}

				if (fm.getFragment(bundle, RoutineSectionFragment.class.getClass().getName()) != null) {
					fragmentList.put(SAVED_ROUTINES_FRAGMENT_POSITION,
							fm.getFragment(bundle, RoutineSectionFragment.class.getClass().getName()));
				} else {
					fragmentList.put(SAVED_ROUTINES_FRAGMENT_POSITION, new RoutineSectionFragment());
				}

			} else {
				Log.e(TAG, "bundle is null");
			/*	Fragment currentRoutineFragment = Fragment.instantiate(getApplicationContext(),
						CurrentRoutineSectionFragment.class.getName()); */
						Fragment currentRoutineFragment = new CurrentRoutineSectionFragment();
				fragmentList.put(CURRENT_ROUTINE_FRAGMENT_POSITION, currentRoutineFragment);

			/*	Fragment activitiesFragment = ActivitySectionFragment.instantiate(getApplicationContext(),
						ActivitySectionFragment.class.getName());*/
				Fragment activitiesFragment = new ActivitySectionFragment();
				fragmentList.put(ACTIVITIES_FRAGMENT_POSITION, activitiesFragment);
				
			/*	Fragment routinesFragment = RoutineSectionFragment.instantiate(getApplicationContext(),
						RoutineSectionFragment.class.getName()); */
				Fragment routinesFragment = new RoutineSectionFragment();
				fragmentList.put(SAVED_ROUTINES_FRAGMENT_POSITION, routinesFragment);
		
			} 
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			switch (position) {
			case 2:
			/*	fragment = RoutineSectionFragment.instantiate(getApplicationContext(),
						RoutineSectionFragment.class.getName()); */
				fragment = fragmentList.get(2);
				break;
			case 1:
				/*fragment = ActivitySectionFragment.instantiate(getApplicationContext(),
						ActivitySectionFragment.class.getName()); */
				fragment = fragmentList.get(1);
				break;
			case 0:
			/*	fragment = CurrentRoutineSectionFragment.instantiate(getApplicationContext(),
						CurrentRoutineSectionFragment.class.getName()); */
				fragment = fragmentList.get(0);
				break;

			}
			return fragment;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = (Fragment) super.instantiateItem(container, position);
			fragmentList.put(position, fragment);
			return fragment;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			fragmentList.remove(position);
			super.destroyItem(container, position, object);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			String title = "";
			switch (position) {
			case 0:
				if(CurrentRoutineSectionFragment.title.isEmpty()){
					title = getString(R.string.title_current_routine).toUpperCase(l);
				}else{
					title = CurrentRoutineSectionFragment.title;
				}
				break;
			case 1:
				title =  getString(R.string.title_activities).toUpperCase(l);
			case 2:
				title =  getString(R.string.title_routines).toUpperCase(l);
			}
			return title;
		}
	}

	public class DrawerItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
			if (p3 < 3) {
				mViewPager.setCurrentItem(p3);
				mDrawerLayout.closeDrawers();
			} else if (p3 == 3) {
				Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
			} else if (p3 == 4) {
				Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
				startActivity(intent);
			}
		}
	}
}
