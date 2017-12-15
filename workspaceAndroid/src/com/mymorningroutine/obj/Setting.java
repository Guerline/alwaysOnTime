package com.mymorningroutine.obj;
import java.io.*;

public class Setting
{
	private String title;
	private Serializable value;

	public void setValue(Serializable value)
	{
		this.value = value;
	}

	public Serializable getValue()
	{
		return value;
	}
	
	


	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}}
