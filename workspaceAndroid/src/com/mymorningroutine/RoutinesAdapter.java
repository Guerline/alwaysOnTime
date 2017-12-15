package com.mymorningroutine;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mymorningroutine.obj.Activity;
import com.mymorningroutine.obj.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.content.*;
import android.util.*;

public class RoutinesAdapter extends ArrayAdapter<Routine>{
	private int resource;
	private String response;
	private Context context;

	private String TAG = "RoutinesAdapter";

	public RoutinesAdapter(Context context, int resource,
							 List<Routine> objects) {
		super(context, resource, objects);
		this.resource=resource;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		LinearLayout routineView;
		//Get the current alert object
		final Routine routine = getItem(position);
		
		//Inflate the view
		if(convertView==null)
		{
			routineView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(resource, routineView, true);
		}
		else
		{
			routineView = (LinearLayout) convertView;
		}
		//Get the text boxes from the listitem.xml file
		TextView goalTextView = (TextView)routineView.findViewById(R.id.list_goal_text_view);
		if(routine != null && goalTextView != null){
		//Assign the appropriate data from our alert object above
		goalTextView.setText(routine.getTitle());
		goalTextView.setId(routine.getId());
		routineView.setId(routine.getId());
			
		
			;
}

routineView.setId(routine.getId());

		return routineView;
	}


}
