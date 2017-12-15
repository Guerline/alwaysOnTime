package com.mymorningroutine;
import java.util.ArrayList;

import com.mymorningroutine.obj.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

public class DialogRoutines extends android.app.Activity
{

	private ArrayList<Routine> routinesList;
	private ListView listView;

	private CheckBox filterCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_dialog_routines);
		
		listView = (ListView) findViewById(android.R.id.list);
		filterCheck = (CheckBox) findViewById(R.id.checkFilterByDay);
		loadRoutines();
		listView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int position, long p4)
				{
					Intent intent = new Intent();
					Routine routine = 	(Routine) p1.getItemAtPosition(position);
					boolean filterCheckBool = filterCheck.isChecked();
					intent.putExtra("routineId",routine.getId());
					intent.putExtra("filterByDay", filterCheckBool);
					Log.e("DialogRoutines", "routine"+ routine.getId());
					setResult(3, intent);
					finish();
				}


			});
	}

	public void loadRoutines()
	{
		routinesList = Singleton.getRoutineTable(getApplicationContext()).getRoutines();
		Routine routineAll = new Routine();
		routineAll.setId(Routine.ALL_ACTIVITIES_ID);
		routineAll.setTitle(getResources().getString(R.string.all_activities));
		routinesList.add(routineAll);
		listView.setAdapter(new RoutinesAdapter(this, R.layout.list_goal_item, routinesList));
	}


}
