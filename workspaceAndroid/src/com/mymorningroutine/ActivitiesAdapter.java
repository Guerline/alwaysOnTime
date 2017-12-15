package com.mymorningroutine;

import java.util.ArrayList;

import com.mymorningroutine.obj.Activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.*;

public class ActivitiesAdapter extends ArrayAdapter<Activity>{
	private int resource;
	
	
	public ActivitiesAdapter(Context context, int resource,
			ArrayList<Activity> objects) {
		super(context, resource, objects);
		this.resource=resource;
		
	}

	
	
	
	 @Override
	    public View getView(final int position, View convertView, ViewGroup parent)
	    {
	        LinearLayout activityView;
	        //Get the current alert object
	        Activity activity = getItem(position);
	         
	        //Inflate the view
	     /*  if(convertView==null)
	        { */
	        	activityView = new LinearLayout(getContext());
	            String inflater = Context.LAYOUT_INFLATER_SERVICE;
	            LayoutInflater vi;
	            vi = (LayoutInflater)getContext().getSystemService(inflater);
	            vi.inflate(resource, activityView, true);
	    /*   }
	      else
	        {
	        	activityView = (LinearLayout) convertView;
	        } */
			
			activityView.setId(Integer.valueOf(activity.getId().toString()));
	        //Get the text boxes from the listitem.xml file
	        TextView goalTextView = (TextView)activityView.findViewById(R.id.list_goal_text_view);
	         
			if(goalTextView != null){
	        //Assign the appropriate data from our alert object above
				
	       		goalTextView.setText(activity.getTitle());
	        	goalTextView.setId(Integer.valueOf(activity.getId().toString()));
				
				goalTextView.invalidate();
				goalTextView.refreshDrawableState();
			} else {
				Log.e(this.getClass().toString(), "TextView not found");
			}
	        return activityView;
	    }


}
