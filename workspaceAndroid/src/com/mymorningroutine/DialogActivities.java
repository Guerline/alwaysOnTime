package com.mymorningroutine;
import java.util.ArrayList;

import com.mymorningroutine.obj.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DialogActivities extends android.app.Activity
{

	private ArrayList<Activity> activitiesList;
	private ListView listView;
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState)
	 {
	 // TODO: Implement this method
	 super.onCreate(savedInstanceState);
	 requestWindowFeature(Window.FEATURE_NO_TITLE);
	 setContentView(R.layout.activity_dialog_activities);
	 listView = (ListView) findViewById(android.R.id.list);
	 loadActivities();
	 listView.setOnItemClickListener(new OnItemClickListener(){

	 @Override
	 public void onItemClick(AdapterView<?> p1, View p2, int position, long p4)
	 {
	 Intent intent = new Intent();
	 intent.putExtra("activityId",p2.getId());
	 setResult(2, intent);
	 finish();
	 }


	 });
	 }

	 public void loadActivities()
	 {
	 activitiesList = Singleton.getActivityTable(getApplicationContext()).getActivities();
	 listView.setAdapter(new ActivitiesAdapter(this, R.layout.list_goal_item, activitiesList));
	 }

	
}
