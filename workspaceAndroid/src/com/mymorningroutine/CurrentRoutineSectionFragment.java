package com.mymorningroutine;
import java.util.ArrayList;
import java.util.Calendar;

import com.mymorningroutine.obj.Activity;
import com.mymorningroutine.obj.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



public class CurrentRoutineSectionFragment extends RefreshableFragment
{

	




	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String INTENT_PARAM_ACTIVITY_ID = "ACTIVITY_ID";
	public static final String INTENT_PARAM_ACTIVITY_LAST = "ACTIVITY_LAST";
	
	public static int ADD_ITEMS_REQUEST_CODE = 3;

	private TextView textHelp;
	private ListView listView;

	public static String title = "";
	private  Boolean postponed;
	private final static  ArrayList<Activity> activitiesList = new ArrayList<Activity>();
	private static int indexCurrentActivity;
	

//	private  CurrentRoutineSectionFragment routineFragment;

	private RoutineActivitiesAdapter listAdapter;
	private Routine currentRoutine;
	private Button buttonStartStopAlarm;
	private Button buttonLoadDailyActivities;
	private Activity currentActivity;
	private Button buttonLoadRoutine;
	private String TAG = "CurrentRoutineSectionFragment";

	private MenuItem playMenuButton;

	private FragmentActivity context;

	private boolean currentRoutineFiltered;

	private Integer currentRoutineId;



	public CurrentRoutineSectionFragment()
	{
		super();
	
	}
	@Override
	protected void setTextHelp(TextView textView)
	{
		this.textHelp = textView;
	}

	@Override
	protected TextView getTextHelp()
	{
		return textHelp;
	}
	
	@Override
	protected void setListView(ListView inListView)
	{
		listView = inListView;
	}

	@Override
	protected ListView getListView()
	{
		return listView;
	}
	
	public void setCurrentRoutine(Routine routine)
	{
		currentRoutine = routine;
	}

	public void setActivitiesList(ArrayList<Activity> newactivitiesList)
	{
		this.activitiesList.clear();
		this.activitiesList.addAll( newactivitiesList);
		
		showListView();
		RoutineActivitiesAdapter rta = (RoutineActivitiesAdapter) listView.getAdapter();
		rta.notifyDataSetChanged();
		listAdapter.notifyDataSetChanged();
		listView.setAdapter(listAdapter);
		listView.invalidate();
		listView.refreshDrawableState();
		
		
	}

	
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{

		
		rootView = inflater.inflate(R.layout.fragment_routine, null);
		textHelpId = R.id.text_help;
		initiateViews();
		
		if(showHelpByDefault){
			showTextHelp();
		}
		if(activitiesList.size() != 0){
			showListView();
		}
		

		indexCurrentActivity = 0;

		buttonStartStopAlarm = (Button)rootView.findViewById(R.id.button_start_stop_alarm);
		


	
		 setRetainInstance(true);
	//	routineFragment = this;
		return rootView;
	}

	
	@Override
	protected void addItems()
	{
		// TODO: Implement this method
		Intent intentRoutine = new Intent(context, DialogRoutines.class);
		startActivityForResult(intentRoutine, ADD_ITEMS_REQUEST_CODE);
		
	}
	
	
	
	
	public void startRoutine()
	{
		Log.e(TAG, "startRoutine.currentRoutine");
		if(activitiesList.size() > 0){
		//	if (getString(R.string.start).equals(buttonStartStopAlarm.getText().toString()))
			{
				currentActivity  = activitiesList.get(indexCurrentActivity);
				Log.d(TAG, "indexCurrentActivity = " + indexCurrentActivity);
				Log.d(TAG, activitiesList.get(0).getTitle());
				startStatusActivity();
			}
		} else{
			
		}
	}

	 @Override
    public void onActivityCreated(Bundle savedInstanceState) {
     super.onActivityCreated(savedInstanceState);
     init();
    }

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.e(TAG, "onActivityResult.currentRoutine");
		// TODO Auto-generated method stub

		Log.e(TAG, (data == null) + " data is null");
		Log.e(TAG, (resultCode == android.app.Activity.RESULT_OK )+ "resultCode");
		/*	if(data == null || resultCode != android.app.Activity.RESULT_OK){
		 Log.e(TAG,"null  result");
		 return;
		 } */
		if ((requestCode == ADD_ITEMS_REQUEST_CODE ) && (data != null)){
			Integer routineId = data.getIntExtra("routineId", 0);
			boolean filterByDay = data.getBooleanExtra("filterByDay", false);
			Log.d("DialogRoutines", "routine"+ routineId + (routineId == Routine.ALL_ACTIVITIES_ID));
			loadActivities(routineId, filterByDay);
		}
			
			super.onActivityResult(requestCode, resultCode, data);
		
	}

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}
	
	public void init(){
		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	
		showHelpByDefault = sharedPreferences.getBoolean("prefShowTutorials", false);
		listAdapter = new RoutineActivitiesAdapter(context, R.layout.list_routine_activity, activitiesList){

			@Override
			public void moveItem(Integer position, Integer positionNext)
			{
				CurrentRoutineSectionFragment.this.moveItem(position, positionNext);
			}


		};
		listView.setAdapter(listAdapter);
		buttonStartStopAlarm.setOnClickListener(new OnClickListener() {

				

				@Override
				public void onClick(View p1)
				{
					startRoutine();
				}
			});
		//loadActivities(Routine.ALL_ACTIVITIES_ID, true);
		loadActivities(Routine.ALL_ACTIVITIES_ID, true);
		
	}
	
	public void loadActivities(Integer routineId, boolean filterByDay)
	{
		Log.d("routine id", routineId + "");
		ArrayList<Activity> allActs = null;
		Routine routine = null;
		currentRoutineId = routineId;
		currentRoutineFiltered =  filterByDay;
		if(routineId > 0){
			
			 routine = Singleton.getRoutineTable(context).getRoutine(routineId);
			setCurrentRoutine(routine);
			allActs  = routine.getActivities();
		}else if(routineId.equals( Routine.ALL_ACTIVITIES_ID)){
			 routine = new Routine();
			routine.setId(Routine.ALL_ACTIVITIES_ID);
			routine.setTitle(getResources().getString(R.string.all_activities));
			setCurrentRoutine(routine);
			allActs =Singleton.getActivityTable(context).getActivities();
		//	setActivitiesList( allActs);
			setCurrentRoutine(routine);
		}
		if(routine != null){
			title = routine.getTitle() + (filterByDay ? "(F)" : "");
			context.setTitle(title);
		}
		if(allActs != null){
			loadDailyActivities(allActs, filterByDay);
		}
	} 


	private void notifyAdapterDataChanged(){
		RoutineActivitiesAdapter rta = (RoutineActivitiesAdapter) listView.getAdapter();
		rta.notifyDataSetChanged();
		listView.setAdapter(rta);
		//	listAdapter.notifyDataSetChanged();
		//listView.setAdapter(listAdapter);
		listView.invalidate();
		listView.refreshDrawableState();
		
	}


	public void stopCurrentActivity()
	{
		Log.e(TAG, "stopCurrentActivity.currentRoutine");
		if (currentActivity != null)
		{
			//	activitiesList.remove(currentActivity);
			//	currentActivity = null;
		}
	}

	public boolean startNextActivity()
	{
		Log.e(TAG, "startNextActivity.currentRoutine");
		stopCurrentActivity();
		if( activitiesList == null){
			return false;
		}
			
		indexCurrentActivity++;
		Log.e(TAG, indexCurrentActivity + "");

		if (indexCurrentActivity < activitiesList.size())
		{
			currentActivity = activitiesList.get(indexCurrentActivity);
			Log.e(TAG, currentActivity.getTitle());
			startStatusActivity();
		} else{
			endRoutine();
		} 
		return true;

	}

	public void startStatusActivity()
	{
		Log.v(TAG, "startStatusActivity");
		Class<?> itemActivity = AlarmStatusActivity.class;
		if(isAdded()){
		Intent intentStatus = new Intent(context, itemActivity);
		intentStatus.putExtra(CurrentRoutineSectionFragment.INTENT_PARAM_ACTIVITY_ID, currentActivity.getId());

		intentStatus.putExtra(CurrentRoutineSectionFragment.INTENT_PARAM_ACTIVITY_LAST, (activitiesList.size() - 1 == indexCurrentActivity));
		//intentStatus.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivityForResult(intentStatus,MainActivity.START_NEXT_ACTIVITY_RESULT_CODE);
		}
	}



	public void loadDailyActivities(ArrayList<Activity> activities, boolean  filterByDay)
	{

		activitiesList.clear();
		
		if(filterByDay){
		
		for (Activity act : activities)
		{
			Day d = null;
			switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
			{
				case Calendar.MONDAY:
					d = Day.Mo;
					break;
				case Calendar.TUESDAY:
					d = Day.Tu;
					break;
				case Calendar.WEDNESDAY:
					d = Day.We;
					break;
				case Calendar.THURSDAY:
					d = Day.Th;
					break;
				case Calendar.FRIDAY:
					d = Day.Fr;
					break;
				case Calendar.SATURDAY:
					d = Day.Sa;
					break;
				case Calendar.SUNDAY:
					d = Day.Su;
					break;
			}
			
			if (act.getDays().contains(d))
			{
				activitiesList.add(act);
			} 

		}
		}else{
			activitiesList.addAll(activities);
		}

	//	showListView();
		currentRoutine = null;
		notifyAdapterDataChanged();



	}	




	public void endRoutine(){
		indexCurrentActivity = 0;
	}

	public void moveItem(int position, int positionNext)
	{
		if ((position < positionNext && position < activitiesList.size() - 1) ||
			(position > positionNext && position >= 1))
		{
			Activity         act = activitiesList.get(position);
			activitiesList.remove(act);
			activitiesList.add(positionNext, act);
			notifyAdapterDataChanged();
		}
	}


	@Override
	public void refreshFragment()
	{
			if(currentRoutineId !=  0){
			//	currentRoutine = Singleton.getRoutineTable(context).getRoutine(currentRoutineId);
				loadActivities(currentRoutineId,currentRoutineFiltered);
			}


	}

	

	
	



}
