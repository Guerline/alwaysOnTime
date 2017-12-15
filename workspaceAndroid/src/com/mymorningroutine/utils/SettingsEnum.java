package com.mymorningroutine.utils;

public  class SettingsEnum
{
	public final static String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
	public final static int NOTIFICATION_TYPE_VALUE_VIBRATE = 1;
	public final static int NOTIFICATION_TYPE_VALUE_VIBRATE_RINGTONE = 3;
	public final static int NOTIFICATION_TYPE_VALUE_RINGTONE = 2;
	public final static int NOTIFICATION_TYPE_VALUE_SILENT = 0;
	
	public final static String POSTPONE_TIME = "POSTPONE_TIME";
	
	public final static String STOP_ALARM_METHOD = "STOP_ALARM_METHOD";
	public final static int STOP_ALARM_METHOD_VALUE_BUTTON = 1;
	public final static int STOP_ALARM_METHOD_VALUE_SHAKE = 2;
	
	public final static String END_ALARM_ACTION = "END_ALARM_ACTION";
	public final static int END_ALARM_ACTION_VALUE_POSTPONE = 1;
	public final static int END_ALARM_ACTION_VALUE_NEXT_ACTIVITY = 2;
	
	/*
	
			public static String getNotificationTypeValueText(int stringValue){
			String value = null;
			if(stringValue == NOTIFICATION_TYPE_VALUE_VIBRATE){
					value = "Vibrates";
			} else if(stringValue == NOTIFICATION_TYPE_VALUE_VIBRATE_RINGTONE){
				value = "Vibrates and rings";
			} else if(stringValue == NOTIFICATION_TYPE_VALUE_RINGTONE){
				value = "Rings";
			} else if(stringValue == NOTIFICATION_TYPE_VALUE_SILENT){
				value = "Silent";
			}
			return value;
		}
		
		
	public static String getEndAlarmActionValueText(int stringValue){
		String value = null;
		//TODO use @string 
		if(stringValue ==END_ALARM_ACTION_VALUE_POSTPONE ){
			value = "Automatically postpones";
		} else if(stringValue == END_ALARM_ACTION_VALUE_NEXT_ACTIVITY){
			value = "Automatically starts next activity";
		} 
		return value;
		}
	
	public static String getStopAlarmMethodText(int stringValue){
		String value = null;
		if(stringValue ==STOP_ALARM_METHOD_VALUE_BUTTON ){
			value = "Using button";
		} else if(stringValue == STOP_ALARM_METHOD_VALUE_SHAKE){
			value = "Shaking";
		} 
		return value;
	}*/
	
}
