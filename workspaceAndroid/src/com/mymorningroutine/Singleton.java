package com.mymorningroutine;
import com.mymorningroutine.server.*;
import android.content.*;

public class Singleton
{
	private static ActivityTable activityTable;
	private static RoutineTable routineTable;
	private static SettingsTable settingsTable;
	
	public static ActivityTable getActivityTable(Context context)
	{
		if (activityTable == null)	
		{
			activityTable = new ActivityTable(context);
		}
		return activityTable;	
	}

	public static RoutineTable getRoutineTable(Context context)
	{
		if (routineTable == null)	
		{
			routineTable = new RoutineTable(context);
		}
		return routineTable;	
	}
	
	public static SettingsTable getSettingsTable(Context context){
		if (settingsTable == null)	
		{
			settingsTable = new SettingsTable(context);
		}
		return settingsTable;
	}
}
