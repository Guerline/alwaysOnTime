package com.mymorningroutine;

import com.mymorningroutine.obj.Routine;
import com.mymorningroutine.server.RoutineTable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.*;

public class RoutineDetailActivity extends Activity {

	Routine routine;
	ListView listView;
	RoutineActivitiesAdapter listAdapter;
	private TextView textTitleView;
	private EditText editTextTitle;
	protected  AlertDialog alertDialog = null;
	private static final Integer RESULT_CODE = 2;

	private Button editButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_routine_activities);
		listView = (ListView) findViewById(android.R.id.list);

		Intent i = getIntent();
		Integer itemId = i.getIntExtra("routineId", 0);
		if (itemId == 0) {
			routine = new Routine();
		}else {
			routine = Singleton.getRoutineTable(getApplicationContext()).getRoutine(itemId);
		}

		setTitle(routine.getTitle());
		editTextTitle =  new EditText(this);
		textTitleView = (TextView) findViewById(R.id.textTitle);
		editTextTitle.setText(routine.getTitle());
		textTitleView.setText(routine.getTitle());
		/*editButton = (Button) findViewById(R.id.action_edit_title_routine);
		editButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					showRoutineTitleDialog();
				}
				
			
		});*/
		
		listAdapter = new RoutineDetailActivitiesAdapter(getApplicationContext(), R.layout.list_routine_activity_detail,
				routine.getActivities()) {

			@Override
			public boolean deleteActivityFromRoutine(int position) {
				routine.getActivities().remove(position);
				RoutineActivitiesAdapter listAdapter = (RoutineActivitiesAdapter) listView.getAdapter();
				listAdapter.notifyDataSetChanged();
				listView.setAdapter(listAdapter);
				listView.invalidateViews();
				listView.refreshDrawableState();

				return true;
			}

			@Override
			public void moveItem(Integer position, Integer positionNext) {
				RoutineDetailActivity.this.moveItem(position, positionNext);
			}

		};
		listView.setAdapter(listAdapter);

		/*
		 * listView.setLongClickable(true);
		 * listView.setOnItemLongClickListener(new
		 * AdapterView.OnItemLongClickListener(){
		 * 
		 * @Override public boolean onItemLongClick(AdapterView<?> p1, final
		 * View p2, int p3, long p4) { final com.mymorningroutine.obj.Activity
		 * activity =
		 * Singleton.getActivityTable(getApplicationContext()).getActivity(p2.
		 * getId());
		 * 
		 * AlertDialog. Builder alertDialogBuilder = new AlertDialog.
		 * Builder(RoutineDetailActivity.this); // set title alertDialogBuilder.
		 * setTitle(getResources().getString(R.id.action_delete)) ; // set
		 * dialog message alertDialogBuilder .
		 * setMessage(getResources().getString(R.id.action_delete) + " '" +
		 * activity.getTitle() + "' from routine '" + routine.getTitle() ) .
		 * setCancelable(false) .
		 * setPositiveButton(getResources().getString(R.string.yes) , new
		 * DialogInterface. OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { // if this button is
		 * clicked, close current activity
		 * routine.getActivities().remove(listView.getPositionForView(p2));
		 * listAdapter.notifyDataSetChanged();
		 * 
		 * } }) . setNegativeButton(getResources().getString(R.string.no), new
		 * DialogInterface. OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { // if this button is
		 * clicked, just close // the dialog box and do nothing dialog.cancel()
		 * ; } }) ; // create alert dialog AlertDialog alertDialog =
		 * alertDialogBuilder.create(); // show it alertDialog.show();
		 * 
		 * 
		 * return true; }
		 * 
		 * });
		 */
		 
		LinearLayout titleLAyout = (LinearLayout) findViewById(R.id.layout_title);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set title
		// final Routine routine = routinep;
		alertDialogBuilder.setTitle(getResources().getString(R.string.activity_title));
		// set dialog message
		alertDialogBuilder.setCancelable(true).setView(editTextTitle).setPositiveButton(
			getResources().getString(R.string.action_save), new DialogInterface.OnClickListener() {


				public void onClick(DialogInterface dialog, int id) {
					// if this button is clicked, close
					// current activity
					// Delete activity
					String title = editTextTitle.getText().toString();
					if (title != null && title != "") {
						routine.setTitle(title);
						setTitle(title);
						textTitleView.setText(title);
						// routine.setActivities(new ArrayList());

					}

				}
			}).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});
		// create alert dialog
		alertDialog = alertDialogBuilder.create();
		
		

		titleLAyout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				alertDialog.show();
			}

		});

		
		 TextView addActivityButton = (TextView)
			findViewById(R.id.action_new_activity); 
			
			
		TextView activitiesTitle = (TextView)
			findViewById(R.id.textActivities);
			String actTitleString = activitiesTitle.getText().toString();
			activitiesTitle.setText(actTitleString.toUpperCase());
			
			//Drawable icon =
		// getResources().getDrawable(android.R.drawable.ic_menu_save);
	//	 addActivityButton.setCompoundDrawables(icon, null, null, null);
		 addActivityButton.setOnClickListener(new OnClickListener(){
		 
		  @Override public void onClick(View p1) { 
		  Class<?> itemActivity =	 DialogActivities.class; 
		  Intent intent = new Intent(getApplicationContext(), itemActivity);
		 intent.putExtra("routineId", routine.getId());
		 startActivityForResult(intent, RESULT_CODE);
		 
		  }
		  
		 
		 });
		 
		
		
		Button saveRoutineButton = (Button) findViewById(R.id.button_save_routine);
		saveRoutineButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				RoutineTable routineTable = Singleton.getRoutineTable(getApplicationContext());
				boolean result = false;
				if(routine.getId() > 0){
					result = routineTable.updateRoutine(routine);
				
				}else{
					result = routineTable.createRoutine(routine);
				}
				
			if (result) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_saved), Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.putExtra("reload",true);
				setResult(2, intent);
				finish();
			}else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_not_saved), Toast.LENGTH_SHORT);
			}
			}
		});

		Button cancelRoutineButton = (Button) findViewById(R.id.button_cancel);
		cancelRoutineButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				finish();
			}

		});
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		}
		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.routine_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.action_delete_routine){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RoutineDetailActivity.this);
			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.activity_title));
			// set dialog message
			alertDialogBuilder.setCancelable(true).setMessage(R.string.confirmation_delete_routine)
					.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// if this button is clicked, close
							// current activity
							// Delete activity
							Singleton.getRoutineTable(getApplicationContext()).deleteRoutine(routine.getId());
							
							
							Intent intent = new Intent();
							intent.putExtra("reload",true);
							setResult(2, intent);
							finish();
						}
					}).setNegativeButton(getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();

		}else if ( item.getItemId() == R.id.action_add_activity_routine){
			Class<?> itemActivity = DialogActivities.class;
			Intent intent = new Intent(getApplicationContext(), itemActivity);
			intent.putExtra("routineId", routine.getId());
			startActivityForResult(intent, RESULT_CODE);
		}else if ( item.getItemId() == R.id.action_edit_title_routine) {
			this.alertDialog.show();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == RESULT_CODE) && (data != null)) {
			Integer activityId = data.getIntExtra("activityId", 0);
			Log.e(getClass().toString(), "Returned activity id = " + activityId);
			if (activityId != 0) {
				com.mymorningroutine.obj.Activity act = Singleton.getActivityTable(getApplicationContext())
						.getActivity(activityId);
				routine.getActivities().add(act);
				listAdapter.notifyDataSetChanged();
			}
		}
		
	}

	public void moveItem(int position, int positionNext) {
		if ((position < positionNext && position < routine.getActivities().size() - 1)
				|| (position > positionNext && position >= 1)) {
			com.mymorningroutine.obj.Activity act = routine.getActivities().get(position);
			routine.getActivities().set(position, routine.getActivities().get(positionNext));
			routine.getActivities().set(positionNext, act);
			RoutineActivitiesAdapter rta = (RoutineActivitiesAdapter) listView.getAdapter();
			rta.notifyDataSetChanged();
			listView.setAdapter(rta);
			listView.invalidateViews();
			listView.refreshDrawableState();

		}
	}

}
