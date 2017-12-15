package com.mymorningroutine.obj;

import java.util.ArrayList;

public class Routine {
	private int id;
	private String title;
	private String description;
	private ArrayList<Activity> activities;

	public static Integer ALL_ACTIVITIES_ID = -2;
	
	public Routine() {
		title = "";
		description = "";
		activities = new ArrayList<Activity>();
		
	}
	
	public Routine(int id, String title, ArrayList<Activity> activities) {
		super();
		this.id = id;
		this.title = title;
		this.activities = activities;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public ArrayList<Activity> getActivities() {
		return activities;
	}
	
	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}
	
	
}
