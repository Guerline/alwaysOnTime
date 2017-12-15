package com.mymorningroutine;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.*;
import com.mymorningroutine.obj.*;

public class ActivitySectionFragment extends RefreshableFragment {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private ArrayList<com.mymorningroutine.obj.Activity> activitiesList;

	private Button createActivityButton;
	protected static TextView textHelp;
	protected static ListView listView;
	private FragmentActivity context;

	public ActivitySectionFragment() {
		super();
	}

	@Override
	protected void addItems() {
		createActivity();
	}

	@Override
	protected void setTextHelp(TextView textView) {
		this.textHelp = textView;
	}

	@Override
	protected TextView getTextHelp() {
		return textHelp;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_activity, null);
		createActivityButton = (Button) rootView.findViewById(R.id.button_add_activity);
		

		textHelpId = R.id.text_help_activities;
		initiateViews();
		if (showHelpByDefault) {
			showTextHelp();
		}
		listView.setLongClickable(true);
	
		/*
		 * listView.setOnItemLongClickListener(new
		 * AdapterView.OnItemLongClickListener(){
		 * 
		 * @Override public boolean onItemLongClick(AdapterView<?> p1, View p2,
		 * int p3, long p4)
		 * 
		 * { final com.mymorningroutine.obj.Activity activity =
		 * Singleton.getActivityTable(context.getApplicationContext()).
		 * getActivity(p2.getId());
		 * 
		 * AlertDialog. Builder alertDialogBuilder = new AlertDialog.
		 * Builder(context); // set title alertDialogBuilder.
		 * setTitle(getResources().getString(R.string.action_delete)) ; // set
		 * dialog message alertDialogBuilder .
		 * setMessage(getResources().getString(R.string.action_delete) + " '" +
		 * activity.getTitle() + "' ?") . setCancelable(true) .
		 * setPositiveButton(getResources().getString(R.string.yes), new
		 * DialogInterface. OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { // if this button is
		 * clicked, close // current activity //Delete activity
		 * Singleton.getActivityTable(context.getApplicationContext()).
		 * deleteActivity(activity.getId()); } }) .
		 * setNegativeButton(getResources().getString(R.string.no) , new
		 * DialogInterface. OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { // if this button is
		 * clicked, just close // the dialog box and do nothing dialog.cancel()
		 * ; } }) ; // create alert dialog AlertDialog alertDialog =
		 * alertDialogBuilder.create(); // show it alertDialog.show();
		 * 
		 * 
		 * return true; } });
		 */

		return rootView;
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
     super.onActivityCreated(savedInstanceState);
     init();
    }
	
	public void init() {
		createActivityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				addItems();
			}

		});
		loadActivities();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		showHelpByDefault = sharedPreferences.getBoolean("prefShowTutorials", false);
		
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				LinearLayout layout = (LinearLayout) view;
				layout = (LinearLayout) layout.getChildAt(0);
				TextView item = (TextView) layout.getChildAt(0);
				Integer itemId = item.getId();

				Class<?> itemActivity = ActivityDetailActivity.class;
				Intent intent = new Intent(context, itemActivity);

				intent.putExtra("activityId", itemId);
				startActivityForResult(intent, 2);

			}
		});
	}
	
	
	private void createActivity() {

		Class<?> itemActivity = ActivityDetailActivity.class;
		Intent intent = new Intent(context, itemActivity);
		startActivityForResult(intent, 2);

	}

	@Override
	protected void setListView(ListView inListView) {
		listView = inListView;
	}

	@Override
	protected ListView getListView() {
		return listView;
	}

	public void loadActivities() {
		activitiesList = Singleton.getActivityTable(context).getActivities();
		ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(context, R.layout.list_goal_item, activitiesList);
		activitiesAdapter.sort(new Comparator<Activity>(){

				@Override
				public int compare(Activity p1, Activity p2)
				{
					// TODO: Implement this method
					int i = 0;
					i = p1.getTitle().compareTo(p2.getTitle());


					return i;
				}


			});
		listView.setAdapter(activitiesAdapter);
		
		
	}
	
		@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}

	@Override
	public void refreshFragment() {
		loadActivities();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && data != null) {
			boolean reload = data.getBooleanExtra("reload", false);
			if (reload) {
				loadActivities();
			}
		}
	}

}
