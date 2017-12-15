package com.mymorningroutine.server;

import java.io.IOException;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.*;

public class BDD {

	protected static final String TABLE_ACTIVITIES = "RoutineActivities";
	public static final String KEY_ID = "_id";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DAYS = "days";
	public static final String KEY_ACTIVE = "active";
	protected static final String KEY_POSTPONE_TIME = "postpone_time";
	protected static final String KEY_FILEPATH = "file_path";
	protected static final String KEY_MUSIC_FILEPATH = "music_file_path";
	protected static final String KEY_VOLUME = "volume";
	protected static final String TABLE_ROUTINES = "Routines";
	protected static final String TABLE_ROUTINE_ACTIVITIES_LINK = "RoutineActivitiesLink";
//	public static final String KEY_ID = "_id";
	public static final String ROUTINE_ID = "routine_";
	public static final String ACTIVITY_ID = "activity_";
//	public static final String KEY_DESCRIPTION = "description";
//	public static final String KEY_TITLE = "title";
	public static final String KEY_POSITION = "position";
	
	
	
	protected SQLiteDatabase bdd;

	protected DatabaseHandler maBaseSQLite;

	public BDD(Context context){
		//On créer la BDD et sa table
		maBaseSQLite = new DatabaseHandler(context);

		try {

			maBaseSQLite.createDataBase();

		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(this.getClass().toString(), e.getMessage());
		}

		try {

			maBaseSQLite.openDataBase();

		}catch(SQLException sqle){

			throw sqle;

		}
	}

	public void open(){
		//on ouvre la BDD en écriture
		if(bdd == null) {
			bdd = maBaseSQLite.getWritableDatabase();
		}
	}

	public void close(){
		//on ferme l'accès à la BDD
		if(bdd.isOpen()) {
			bdd.close();
		}
	}
}
