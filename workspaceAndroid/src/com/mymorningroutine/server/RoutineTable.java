package com.mymorningroutine.server;
import java.util.ArrayList;

import com.mymorningroutine.obj.Activity;
import com.mymorningroutine.obj.Routine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class RoutineTable extends BDD{
	
 	
	private Context context;

public RoutineTable (Context context) {
		super(context);
	this.context = context;
	}
	public ArrayList<Routine> getRoutines() {
        open();
	//	String query = "SELECT * FROM " + TABLE_ACTIVITIES;

		Cursor cursor = bdd.query(TABLE_ROUTINES,null,null,null,null,null,null);
		ArrayList<Routine> routines = new ArrayList<Routine>();
		Log.v("nbr of routines",cursor.getCount() + "");
		try {
		while(cursor.moveToNext()) {
	//	ArrayList<Day> days = Day. getDaysFromTableText( cursor.getString(5));
	
		Routine routine= new Routine(cursor.getInt(0), cursor.getString(1), null );
			getRoutineActivties(routine);
			routines.add(routine);
	 }
	 } finally {
	// cursor.close();
	 }
		return routines;
	}
	
	public Routine getRoutine(int id) {
    open();
    Routine routine = null;
	Log.v("routine id", id + "");
    String query = "SELECT * FROM " + TABLE_ROUTINES + " WHERE " + KEY_ID + "= " + id + "";
	 Cursor cursor = bdd.rawQuery(query, null);
	 try {
   	if (cursor.moveToFirst()) {
		 routine= new Routine(cursor.getInt(0), cursor.getString(1), null );
		getRoutineActivties(routine);
		}
		}
		finally {
			//cursor.close();
		}
		return routine;
	}

	public boolean updateRoutine(Routine routine) {
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, routine.getTitle());
		values.put(KEY_DESCRIPTION, routine.getDescription());
		open();
		Log.e("routineId Id", routine.getId() + " ");
		int rowsUpdated = bdd.update(TABLE_ROUTINES, values, KEY_ID+"= "+ routine.getId() + "", null);
		if(rowsUpdated != 1){
			return false;
		}
		bdd.delete(TABLE_ROUTINE_ACTIVITIES_LINK, ROUTINE_ID + "  = "+ routine.getId(), null);
		int i;
		int n = routine.getActivities().size();
		for( i= 0; i < n; i++){
			ContentValues actRoutLinkValues = new ContentValues();
			actRoutLinkValues.put(ROUTINE_ID, routine.getId());
			actRoutLinkValues.put(ACTIVITY_ID, routine.getActivities().get(i).getId());
			actRoutLinkValues.put(KEY_POSITION, i);
			actRoutLinkValues.put(KEY_ID, 0);
			bdd.insert(TABLE_ROUTINE_ACTIVITIES_LINK, null,actRoutLinkValues);
		}
		return true;
	}
	
	public boolean createRoutine(Routine routine) {
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, routine.getTitle());
		values.put(KEY_DESCRIPTION, routine.getDescription());
		
		open();
		long inserted = bdd.insert(TABLE_ROUTINES, null, values);
		Log.v("bdd",inserted + "");
		//close();
		if(inserted == 0){
			return false;
		}
		routine.setId(Math.round(inserted));
			int i;
			int n = routine.getActivities().size();
			for( i= 0; i < n; i++){
				ContentValues actRoutLinkValues = new ContentValues();
				actRoutLinkValues.put(ROUTINE_ID, routine.getId());
				actRoutLinkValues.put(ACTIVITY_ID, routine.getActivities().get(i).getId());
				actRoutLinkValues.put(KEY_POSITION, i);
				actRoutLinkValues.put(KEY_ID, 0);
				bdd.insert(TABLE_ROUTINE_ACTIVITIES_LINK, null,actRoutLinkValues);
			}
		
		return true;
	}
	
	public boolean saveRoutine(Routine routine){
		boolean routineSaved = false;
	if(routine.getId() > 0){
		routineSaved = updateRoutine(routine);
	}else{
		routineSaved =  createRoutine(routine);
	}
	return routineSaved;
	}
	
	
	public void getRoutineActivties(Routine routine){
		Integer routineId = routine.getId();
		Log.e("getRoutineActivities", "hey");
		ArrayList<Activity> activities =new ArrayList<Activity>();
		String query = "SELECT * FROM " + TABLE_ROUTINE_ACTIVITIES_LINK + " WHERE " + ROUTINE_ID + "= \"" + routineId + "\"";
		Cursor cursor = bdd.rawQuery(query,null);
		while(cursor.moveToNext()){
			Activity act = new ActivityTable(context).getActivity(cursor.getInt(1));
			if(act != null){
				activities.add(act);
			}
		}
		routine.setActivities(activities);
		
		
	}
	
	public Integer deleteRoutine(long id) {
		open();
		bdd.delete(TABLE_ROUTINE_ACTIVITIES_LINK,ROUTINE_ID + "= \"" + id + "\"",null);
		int returnCode = bdd.delete(TABLE_ROUTINES,KEY_ID + "= \"" + id + "\"", null);
		
		return returnCode;
	}
}
