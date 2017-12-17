package com.mymorningroutine;
import java.util.ArrayList;

import com.mymorningroutine.obj.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RoutineSectionFragment extends RefreshableFragment
{

		/**
		 * The fragment argument representing the section number for this
		 * fragment.*/
		private ArrayList<Routine> routinesList;
		protected static TextView textHelp;
		protected static ListView listView;
	private FloatingActionButton createRoutineButton;
	private FragmentActivity context;
	private String TAG = "RoutineSectionFragment";


		public RoutineSectionFragment(){
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
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState)
		{
			rootView = inflater.inflate(R.layout.fragment_routines, null);
			createRoutineButton = (FloatingActionButton) rootView.findViewById(R.id.button_add_routine);
			

				

			textHelpId = R.id.text_help_routines;
			initiateViews();
			
			
				
			/*listView.setOnItemLongClickListener(new OnItemLongClickListener(){
					

					@Override
					public boolean onItemLongClick(AdapterView<?> p1, View view, int p3, long p4)
					{
						// TODO: Implement this method
						Routine routine = Singleton.getRoutineTable(context).getRoutine(view.getId());
						showRoutineTitleDialog(routine);
						return true;
					}


				});*/
				
			setRetainInstance(true);
			return rootView;
		}
		
		
	 @Override
    public void onActivityCreated(Bundle savedInstanceState) {
     super.onActivityCreated(savedInstanceState);
     init();
    }
	
	public void init(){
		createRoutineButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						createRoutine();	
					}
					
					}
				);	
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		showHelpByDefault = sharedPreferences.getBoolean("prefShowTutorials", false);
		if(showHelpByDefault){
			showTextHelp();
		}
		listView.setOnItemClickListener(new OnItemClickListener(){
					

					@Override
					public void onItemClick(AdapterView<?> p1, View view, int p3, long p4)
					{
						
						Integer itemId = view.getId();

						Class<?> itemActivity = RoutineDetailActivity.class;
						Intent intent = new Intent(context, itemActivity);

						intent.putExtra("routineId", itemId);
						startActivityForResult(intent,2);


					}
				});
				
			loadRoutines();
	}

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}

	@Override
	protected void addItems()
	{
		createRoutine();
	}

		public void loadRoutines()
		{
			routinesList = Singleton.getRoutineTable(context).getRoutines();
			Log.v(TAG, context + " routines ");
			if(isAdded()){
			listView.setAdapter(new RoutinesAdapter(context, R.layout.list_routine_item, routinesList));
			}
		}

		@Override
		public void refreshFragment()
		{
			// TODO: Implement this method
			loadRoutines();
			
		}

		
		private void createRoutine()
		{

			Class<?> itemActivity = RoutineDetailActivity.class;
			Intent intent = new Intent(context, itemActivity);
			startActivityForResult(intent,2);

		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == 2 && data != null){
				boolean reload = data.getBooleanExtra("reload", false);
				if(reload){
					refreshFragment();
				}
			}
		}
		
	/*	
		public void showRoutineTitleDialog(Routine routinep){
			
			final EditText input = new EditText(context);
			if(routinep != null) {
			input.setText(routinep.getTitle());
			} else {
			input.setText(getResources().getString(R.string.activity_title));
			routinep = new Routine();
			}
			AlertDialog. Builder alertDialogBuilder = new AlertDialog. Builder(context);
// set title
			final Routine routine = routinep;
			alertDialogBuilder. setTitle(getResources().getString(R.string.activity_title)) ;
// set dialog message
						alertDialogBuilder
							. setCancelable(true)
							.setView(input)
				. setPositiveButton(getResources().getString(R.string.action_save) , new DialogInterface. OnClickListener() {
								public void onClick(DialogInterface dialog, int id)
								{
// if this button is clicked, close
// current activity
									//Delete activity
									String title = input.getText().toString();
									if(title != null && title != ""){
										routine.setTitle(title);
										//routine.setActivities(new ArrayList());
										Singleton.getRoutineTable(context).saveRoutine(routine);
										refreshFragment();
									}
								
								}
							})
							. setNegativeButton(getResources().getString(R.string.cancel) , new DialogInterface. OnClickListener() {
								public void onClick(DialogInterface dialog, int id)
								{
// if this button is clicked, just close
// the dialog box and do nothing
									dialog.cancel() ;
								}
							}) ;
// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
// show it
						alertDialog.show();
		
		}

		
		*/
		
	}
