package com.mymorningroutine.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public  class TimeUtils
{

	/**
	 * Get the time in format mm:ss from seconds
	 * @param time in seconds
	 * @return time formatted
	 */
	public static String getTimeFormat(int time)
	{
		String viewDuration = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        Date date = new Date();
        date.setTime(time*1000);
        viewDuration = dateFormat.format(date);
		return viewDuration;
	}



}
