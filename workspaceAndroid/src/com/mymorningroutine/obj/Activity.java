package com.mymorningroutine.obj;

import java.util.ArrayList;

import com.mymorningroutine.Day;

public class Activity
{
	private Long id;
	private String title;
	private String description ;
	private ArrayList<Day> days ;
	private boolean active;
	private int durationTime;
	private  int postponeTime;
	private int volume;
	private String backgroundFilePath;
	private String musicFilePath;
	private static int number= 0;


	public Activity()
	{
		this.id = Long.valueOf(0);
		this.title = "Activity" + (number++);
		this.description = "";
		this.days = new ArrayList<Day>();
		this.active = true;
		this.durationTime = 500;
		this.backgroundFilePath = "";
		this.musicFilePath = "";
		this.postponeTime = 100;
		this.volume = 50;
	}

	public Activity(Long id, String title, String description, Integer durationMins, ArrayList<Day> days,
					boolean active, String backgroundFilePath, String musicFilePath, Integer postponeTime, int volume)
	{
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.days = days;
		this.active = active;
		this.durationTime = durationMins;
		this.backgroundFilePath = backgroundFilePath;
		this.musicFilePath = musicFilePath;
		this.postponeTime = postponeTime;
		this.volume = volume;
	}

	public void setVolume(Integer volume)
	{
		this.volume = volume;
	}

	public Integer getVolume()
	{
		return volume;
	}

	public void setPostponeTime(Integer postponeTime)
	{
		this.postponeTime = postponeTime;
	}

	public Integer getPostponeTime()
	{
		return postponeTime;
	}

	public void setMusicFilePath(String musicFilePath)
	{
		this.musicFilePath = musicFilePath;
	}

	public String getMusicFilePath()
	{
		return musicFilePath;
	}

	public void setBackgroundFilePath(String filePath)
	{
		this.backgroundFilePath = filePath;
	}

	public String getBackgroundFilePath()
	{
		return backgroundFilePath;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public ArrayList<Day> getDays()
	{
		return days;
	}

	public void setDays(ArrayList<Day> days)
	{
		this.days = days;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public Integer getDurationTime()
	{
		return durationTime;
	}

	public void setDurationTime(Integer durationMins)
	{
		//Not more than a day
		if (durationMins > 0 && durationMins < 1440)
		{
			this.durationTime = durationMins;
		}
	}


}
