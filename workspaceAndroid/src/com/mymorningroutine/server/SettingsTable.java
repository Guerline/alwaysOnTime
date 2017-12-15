package com.mymorningroutine.server;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;

public class SettingsTable extends BDD
{

	public static final String KEY_VALUE = "value";
	public static final String KEY_TITLE = "title";
	public static final String TABLE_SETTINGS = "Settings";
	
	public SettingsTable (Context context) {
		super(context);
	}
	
	public Map<String,String> getSettings(){
		Map<String,String> settings = new HashMap<String,String>();
		open();
		//	String query = "SELECT * FROM " + TABLE_ACTIVITIES;

		Cursor cursor = bdd.query(TABLE_SETTINGS,null,null,null,null,null,null);
		
		try {
			while(cursor.moveToNext()) {
				
				settings.put(cursor.getString(0), cursor.getString(1) );
			}
		} finally {
			cursor.close();
		}
		return settings;
	}
	
}
