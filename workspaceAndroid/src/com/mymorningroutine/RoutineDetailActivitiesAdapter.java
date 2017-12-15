package com.mymorningroutine;

import java.util.List;

import com.mymorningroutine.obj.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mymorningroutine.utils.*;

public abstract class RoutineDetailActivitiesAdapter extends RoutineActivitiesAdapter 
{
	public RoutineDetailActivitiesAdapter(Context context, int resource,
									List<Activity>	 objects) {
		super(context,resource,objects);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		LinearLayout activityView;
		//Get the current alert object	
		Activity activity = getItem(position);

		//Inflate the view
		if(convertView==null)
		{
			activityView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(resource, activityView, true);
		}
		else
		{
			activityView = (LinearLayout) convertView;
		}
		TextView activityTextView = (TextView)activityView.findViewById(R.id.list_routine_activity_title_view);

		if(activityTextView != null){
			String title = activity.getTitle();
			activityTextView.setText(title);
			activityTextView.setId(Integer.valueOf(activity.getId().toString()));

			TextView textView= (TextView) activityView.findViewById(R.id.list_routine_activity_start_end);
			textView.setText(getContext().getResources().getString(R.string.activity_duration) + " : " + TimeUtils.getTimeFormat(activity.getDurationTime()));

			Button upButton = (Button) activityView.findViewById(R.id.list_routine_activity_up);
			upButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						moveItem(position, position -1 );
					}




				});

			Button downButton = (Button) activityView.findViewById(R.id.list_routine_activity_down);
			downButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						moveItem(position, position +1 );

					}


				});

			Button deleteButton = (Button) activityView.findViewById(R.id.list_routine_activity_delete);
			deleteButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						deleteActivityFromRoutine(position);

					}


				});
				
				
			if(position == getCount() -1 || position == 0){
				downButton.setEnabled(false);
				downButton.setVisibility(0);
			}
		}
		return activityView;
	}
	
	
	public abstract boolean deleteActivityFromRoutine(int position);
}
