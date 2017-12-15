package com.mymorningroutine;
import java.util.ArrayList;
import java.util.Map;

import com.mymorningroutine.server.ActivityTable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class ActivityEditActivity extends Activity
{
  private com.mymorningroutine.obj.Activity activity;
  private EditText textTitleView;
  private EditText textDescriptionView;
  private NumberPicker textDurationView;
  private ActivityTable table;
  

  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_activity_edit);
		initVariables();
		Intent i = getIntent();
		Integer activityId = i.getIntExtra("activityId", 0);
		if(activityId != 0) {
			activity = new ActivityTable(this).getActivity(activityId);

		if( activity !=null) {
			
			textTitleView.setText(activity.getTitle());
			
			textDescriptionView.setText(activity.getDescription());
			textDurationView.setValue(activity.getDurationTime());

       for(Day d : activity.getDays()){
		   if(d != null){
			   CheckBox check = (CheckBox) findViewById(Day.getMapDaysId().get(d));
			   check.setChecked(true);
		   }



}


			
		}
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		Integer menuItemId = item.getItemId();
		if(menuItemId == R.id.action_save){
				if(activity == null){
					activity = new com.mymorningroutine.obj.Activity();
				}
				Boolean toBeSaved = true;
				if(textTitleView.getText()!= null || textTitleView.getText().toString().isEmpty()){
					activity.setTitle(textTitleView.getText().toString());
				} else {
					toBeSaved = false;
					textTitleView.setBackgroundColor(Color.RED);
				}
				
				if(textDescriptionView.getText() != null){
					activity.setDescription(textDescriptionView.getText().toString());
				} else {
					toBeSaved = false;
					textDescriptionView.setBackgroundColor(Color.RED);
				}
				
				if(textDurationView.getValue() != 0){
					activity.setDurationTime(Integer.valueOf(textDurationView.getValue()));
				} else {
					toBeSaved = false;
					textDurationView.setBackgroundColor(Color.RED);
				}
				
				
				
				ArrayList<Day> days = new ArrayList<Day>();
				for(Map.Entry<Day, Integer> mapItem : Day.getMapDaysId().entrySet()){
					CheckBox check = (CheckBox) findViewById(mapItem.getValue());
					if(check.isChecked()) {
						days.add(mapItem.getKey());
					}
				}
				activity.setDays(days);
				
				if(toBeSaved){
				boolean returnState = false;
				if(activity.getId() == null){
					returnState = (table.insertActivity(activity) ? true: false);
				} else {
					returnState =	table.updateActivity(activity);
				}
				if(returnState){
					Toast.makeText(this, "Saved",Toast.LENGTH_SHORT).show();
					MainActivity.reloadFragmentActivities();
					finish();
				} else {
					Toast.makeText(this, "Save Failed",Toast.LENGTH_SHORT).show();
				}} else {
					Toast.makeText(this, "Correct the red fields before saving",Toast.LENGTH_SHORT).show();
				}
		}
		return super.onOptionsItemSelected(item);
	}

	

  protected void initVariables(){
	  table = new ActivityTable(this);
	  textTitleView = (EditText) findViewById(R.id.editTextTitle);
	  textDescriptionView = (EditText) findViewById(R.id.editTextDescription);
	  textDurationView = (NumberPicker) findViewById(R.id.editTextDuration);
	  textDurationView.setMinValue(1);
	  textDurationView.setMaxValue(60);
	  
	 }
	
}
