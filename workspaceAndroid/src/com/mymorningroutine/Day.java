package com.mymorningroutine;
import java.util.ArrayList;
import android.util.*;
import java.util.*;
import android.content.res.*;

public enum Day {
	Mo,
	Tu,
	We,
	Th,
	Fr,
	Sa,
	Su;
	
	
	
	
	public static String getName(Day day) {
		String dayString = "";		
		dayString = String.valueOf(day);
		return dayString;
	}
	public static String getNameLocale(Resources resources,Day day) {
		String dayString = "";		
		dayString = resources.getString(getMapStringDaysId().get(day));
		return dayString;
	}
	
	public static  Map<Day,Integer> getMapDaysId(){
		 Map<Day,Integer> mapDaysId = new HashMap<Day,Integer>();
		mapDaysId.put(Day.Mo, R.id.checkMonday);
	  mapDaysId.put(Day.Tu, R.id.checkTuesday);
	  mapDaysId.put(Day.We, R.id.checkWednesday);
	  mapDaysId.put(Day.Th, R.id.checkThursday);
	  mapDaysId.put(Day.Fr, R.id.checkFriday);
	  mapDaysId.put(Day.Sa, R.id.checkSaturday);
	  mapDaysId.put(Day.Su, R.id.checkSunday);
	  return mapDaysId;
	}
	
	public static  Map<Day,Integer> getMapStringDaysId(){
		Map<Day,Integer> mapDaysId = new HashMap<Day,Integer>();
		mapDaysId.put(Day.Mo, R.string.monday);
		mapDaysId.put(Day.Tu, R.string.tuesday);
		mapDaysId.put(Day.We, R.string.wednesday);
		mapDaysId.put(Day.Th, R.string.thursday);
		mapDaysId.put(Day.Fr, R.string.friday);
		mapDaysId.put(Day.Sa, R.string.saturday);
		mapDaysId.put(Day.Su, R.string.sunday);
		return mapDaysId;
	}
	
	public static  ArrayList<Day> getDaysFromTableText(String days){
		ArrayList<Day> daysList = new ArrayList<Day>();
		if(days != null){
		String[] textDays = days.split(",");
     for(String textDay : textDays){
         daysList.add(getDayFromName(textDay.trim()));
     }}
		return daysList;
	}
	
	public static  String getTableTextFromDays(Resources resources, ArrayList<Day> days){
		String daysText = "";
		for(Day d : days)
		{
			daysText += getNameLocale(resources,d) + ", ";
		}

		return daysText;
	}
	
	public static  String getTableTextFromDays( ArrayList<Day> days){
		String daysText = "";
		if(days.size() > 0){
			for(Day d : days)
			{
				daysText += getName(d) + ", ";
			}
			daysText = daysText.substring(0, daysText.length() - 2);
		}
		Log.v("Days", daysText);
		return daysText;
	}
	
	public static Day getDayFromName(String name){
		Day day = null;
		
		if( "Fr".equals(name)){
			 	day = Fr;
		} else if( "Th".equals(name)){
			day = Th;
		}else if("Mo".equals(name)){
			day = Mo;
		}else if("Sa".equals(name)){
			day = Sa;
		}else if("Su".equals(name)){
			day = Su;
		}else if("Tu".equals(name)){
			day = Tu;
		}else if("We".equals(name)){
			day = We;
		}
		
		return day;
	}
	


}
