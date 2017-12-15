package com.mymorningroutine.server;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mymorningroutine.Day;
import com.mymorningroutine.obj.Activity;

public class ActivityTable extends BDD{
	
 
public ActivityTable (Context context) {
		super(context);
	}
	public ArrayList<Activity> getActivities() {
        open();
	//	String query = "SELECT * FROM " + TABLE_ACTIVITIES;

		Cursor cursor = bdd.query(TABLE_ACTIVITIES,null,null,null,null,null,null);
		ArrayList<Activity> activities = new ArrayList<Activity>();
		try {
		while(cursor.moveToNext()) {
		ArrayList<Day> days = Day. getDaysFromTableText( cursor.getString(5));
			Activity act = new Activity(cursor.getLong(0), cursor.getString(1) , cursor.getString(2) , cursor.getInt(3) , days, (cursor.getInt(4)==1? true:false),cursor.getString(6), cursor.getString(7), cursor.getInt(8), cursor.getInt(9));
		activities.add(act);
	 }
	 } finally {
	 cursor.close();
	 }
		return activities;
	}
	
	public Activity getActivity(long id) {
    open();
    Activity act = null;
    String query = "SELECT * FROM " + TABLE_ACTIVITIES + " WHERE " + KEY_ID + "= \"" + id + "\"";
	 Cursor cursor = bdd.rawQuery(query, null);
	 
	 try {
   	if (cursor.moveToFirst()) {
		
		ArrayList<Day> days = Day. getDaysFromTableText( cursor.getString(cursor.getColumnIndexOrThrow(KEY_DAYS)));
		act = new Activity(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)) , 
							cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)) , cursor.getInt(cursor.getColumnIndexOrThrow(KEY_DURATION)) ,
						   days, (cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ACTIVE))==1? true:false),cursor.getString(cursor.getColumnIndexOrThrow(KEY_FILEPATH)),
							cursor.getString(cursor.getColumnIndexOrThrow(KEY_MUSIC_FILEPATH)), cursor.getInt(cursor.getColumnIndexOrThrow(KEY_POSTPONE_TIME)), 
							cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VOLUME)));
       
		}
		}
		finally {
			cursor.close();
		}
		return act;
	}

	public Integer deleteActivity(long id) {
		open();
		int returnCode = bdd.delete(TABLE_ACTIVITIES,KEY_ID + "= \"" + id + "\"", null);
		returnCode = bdd.delete(TABLE_ROUTINE_ACTIVITIES_LINK,ACTIVITY_ID + "= \"" + id + "\"", null);
		return returnCode;
	}
	
	public boolean updateActivity(Activity activity) {
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, activity.getTitle());
		values.put(KEY_DESCRIPTION, activity.getDescription());
		values.put(KEY_DURATION, activity.getDurationTime());
		values.put(KEY_DAYS, Day.getTableTextFromDays(activity.getDays()));
		values.put(KEY_ACTIVE, activity.isActive());
		values.put(KEY_FILEPATH,activity.getBackgroundFilePath());
		values.put(KEY_MUSIC_FILEPATH, activity.getMusicFilePath());
		values.put(KEY_POSTPONE_TIME, activity.getPostponeTime());
		values.put(KEY_VOLUME, activity.getVolume());
		
		
		
		open();
		int rowsUpdated = bdd.update(TABLE_ACTIVITIES, values, KEY_ID+"= "+ activity.getId() + "", null);
		if(rowsUpdated != 1){
			return false;
		}
		return true;
	}
	
	public boolean insertActivity(Activity activity) {
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, activity.getTitle());
		values.put(KEY_DESCRIPTION, activity.getDescription());
		values.put(KEY_DURATION, activity.getDurationTime());
		values.put(KEY_DAYS, Day.getTableTextFromDays(activity.getDays()));
		values.put(KEY_ACTIVE, activity.isActive());
		values.put(KEY_FILEPATH,activity.getBackgroundFilePath());
		values.put(KEY_MUSIC_FILEPATH, activity.getMusicFilePath());
		values.put(KEY_POSTPONE_TIME, activity.getPostponeTime());
		values.put(KEY_VOLUME, activity.getVolume());
		
		open();
		long inserted = bdd.insert(TABLE_ACTIVITIES, null, values);
		Log.v("bdd",inserted + "");
		activity.setId(inserted);
		
		if(inserted == 0){
			return false;
		}
		
		return true;
	}

}
