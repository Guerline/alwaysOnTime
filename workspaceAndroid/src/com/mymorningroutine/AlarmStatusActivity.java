package com.mymorningroutine;
import com.mymorningroutine.utils.SettingsEnum;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlarmStatusActivity extends Activity
{
	private Button stopRoutineButton;
	private  Button nextActivityButton;
    private TextView textView;
	private  static TextView textViewCountDown;
	private static TextView textViewTime;
	private  MediaPlayer mp;
	private  Integer postponeMinute;
	private  Integer notificationTypeValue;
	private  Integer endAlarmActionValue;
	private  Integer stopAlarmMethodValue;
	private   com.mymorningroutine.obj.Activity activity;
	public static NotificationCompat.Builder	mNotifyBuilder;
	public boolean alarmStatusOn = true;


	private static Boolean activityLast;
	private static Vibrator vibrator;

	public static int notifyID = 1;
	
	private static Resources resources;

	private static String TAG = "AlarmStatusActivity";

	private static Intent broadcast;

	public static  NotificationManager mNotificationManager;

	private static boolean keepScreenOnValue;

	private Button switchViewButton;

	
	public static void setCountDownText(String text){
		if(text == null){
			Log.e("null","null");
			return;
		}
		textViewCountDown.setText(text);
		textViewCountDown.invalidate();
		
		mNotifyBuilder.setContentText(text);
		mNotificationManager.notify(
			notifyID, mNotifyBuilder.build());
	}
	
	public static void setTimeText(String text){
		if(text == null){
			return;
		}
		textViewTime.setText(text);
		textViewTime.invalidate();
	}
	
	public  void startMusic()
	{
		Log.e(TAG,"startMusic");
		stopService(broadcast);
		bringActivityToFront();
		
		textViewCountDown.setText(resources.getString(R.string.dring));
		textViewTime.setText(resources.getString(R.string.dring));
		textViewTime.setVisibility(View.INVISIBLE);
		textViewCountDown.setVisibility(View.VISIBLE);
		
		

		//nextActivityButton.setText(resources.getString(R.string.postpone));
		
		startNotificationAlarm();
		
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		Log.e(TAG, "onNewIntent.AlarmStztusActivity");
		if (intent.getExtras() != null)
		{ 
			Long activityId = getIntent().getLongExtra(CurrentRoutineSectionFragment.INTENT_PARAM_ACTIVITY_ID, 0);

			if (activityId != 0)
			{
			boolean finished = intent.getBooleanExtra("finished", true);
		if (finished)
		{
			boolean music = intent.getBooleanExtra("music", false);
			if(music){
				endMusic();


			}else{
				startMusic();

			}
}
		}
		super.onNewIntent(intent);
		
		alarmStatusOn =true;
	}
	}
	
	

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mNotificationManager.cancel(notifyID);
	
	}


	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		alarmStatusOn =  true;
		super.onResume();
		
	}
	
	

	
	public void bringActivityToFront(){
		Intent intentActivity = new Intent(getApplicationContext(), AlarmStatusActivity.class);
		intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentActivity.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        intentActivity.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		if(keepScreenOnValue){
       		 intentActivity.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
		}
		intentActivity.addCategory(Intent.CATEGORY_LAUNCHER);
		intentActivity.setAction(Intent.ACTION_MAIN);
		startActivity(intentActivity);
	}
	public void endMusic()
	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		endAlarmActionValue = Integer.parseInt(sharedPreferences.getString("prefEndAlarmAction", "1"));
		alarmStatusOn = true;
		Log.e(TAG, "music finished" + endAlarmActionValue);
		stopNotificationAlarm(getApplicationContext());
		if(endAlarmActionValue == SettingsEnum.END_ALARM_ACTION_VALUE_NEXT_ACTIVITY){
			mNotificationManager.cancel(notifyID);
			Log.e(TAG, "start next activity");
			Intent intent2return = new Intent();
			intent2return.putExtra("action", "startNextActivity");	;
			setResult(MainActivity.START_NEXT_ACTIVITY_RESULT_CODE, intent2return);
			finish();
			/*	Intent intent2open = new Intent(this, MainActivity.class);
				intent2open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//	intent2open.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			//	intent2open.putExtra("action","startNextActivity");
		
			startActivity(intent2open);*/
				
		}else {
				postpone();
				Log.e(TAG, "postpone");
		}
		
		// finish actiity
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		alarmStatusOn =  true;
		Log.e(TAG, "onCreate = ");
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// Sets an ID for the notification, so it can be updated
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		notificationTypeValue = Integer.parseInt(sharedPreferences.getString("prefNotificationType", "1"));
		endAlarmActionValue = Integer.parseInt(sharedPreferences.getString("prefEndAlarmAction", "1"));
		stopAlarmMethodValue = Integer.parseInt(sharedPreferences.getString("prefStopAlarmMethod", "1"));
		keepScreenOnValue = sharedPreferences.getBoolean("prefKeepScreenOn", false);
		
		
		resources = getResources();
		broadcast = new Intent(this, BroadcastService.class);
		//add settig keep screen on
		if(keepScreenOnValue){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		setContentView(R.layout.activity_alarm_status);
		Long activityId = getIntent().getLongExtra(CurrentRoutineSectionFragment.INTENT_PARAM_ACTIVITY_ID, 0);
		
		if (activityId != 0)
		{
			activity = Singleton.getActivityTable(getApplicationContext()).getActivity(activityId);

		}
		else
		{
			Log.e(TAG, "Invalid activity id =" + activityId );
			finish();
			return;
			
		}
		
		Log.e(TAG, "onCreate = " + activity.getTitle());
		Intent intent = new Intent(this, AlarmStatusActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		mNotifyBuilder = new NotificationCompat.Builder(this)
			.setContentTitle(activity.getTitle())
			.setContentText("")
			.setSmallIcon(R.drawable.ic_launcher)
			.setOngoing(true).setContentIntent(pIntent);
			 
			
		int	numMessages = 0;
// Start of a loop that processes data and then notifies the user


		// Because the ID remains unchanged, the existing notification is
		// updated.
		mNotificationManager.notify(
			notifyID,
			mNotifyBuilder.build());
		activityLast = getIntent().getBooleanExtra(CurrentRoutineSectionFragment.INTENT_PARAM_ACTIVITY_LAST, false);
		nextActivityButton = (Button) findViewById(R.id.current_activity_stop_button);

		
		
		if(activity.getPostponeTime() == null){
		
		postponeMinute = Integer.parseInt(sharedPreferences.getString("prefPostponeTime", "1"));
		}else{
			postponeMinute = activity.getPostponeTime();
		}
				vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


		
		
		
	    textViewCountDown = (TextView) findViewById(R.id.countdown_text_view);
		textViewTime = (TextView) findViewById(R.id.current_time_text_view);
		textView = (TextView) findViewById(R.id.current_activity_text_view);
		//switchViewButton = (Button) findViewById(R.id.switch_view_button);
		textView.setText(activity.getTitle());
		textViewTime.setVisibility(View.INVISIBLE);
		textViewCountDown.setVisibility(View.VISIBLE);
		textViewCountDown.setOnLongClickListener(new View.OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method.
					//add dialog
					if(getResources().getString(R.string.dring).equals(textViewCountDown.getText())){
						postpone();
					return true;
					}
					return false;
				}
				
			
		});
		
		textViewTime.setOnLongClickListener(new View.OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					// TODO: Implement this method.
					//add dialog
					if(getResources().getString(R.string.dring).equals(textViewCountDown.getText())){
						postpone();
						return true;
					}
					return false;
				}


			});
		textView.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if(textViewCountDown.getVisibility() == View.INVISIBLE){
						textViewCountDown.setVisibility(View.VISIBLE);
						textViewTime.setVisibility(View.INVISIBLE);
					}else {
						textViewCountDown.setVisibility(View.INVISIBLE);
						textViewTime.setVisibility(View.VISIBLE);
					}
					
				}
				
			
		});
		startCountDownTimer(Double.valueOf(activity.getDurationTime()), false);
		if (activity.getBackgroundFilePath() != null)
		{
			textViewCountDown.setBackgroundColor(Color.TRANSPARENT);
			textViewTime.setBackgroundColor(Color.TRANSPARENT);
			Thread t = new Thread(){
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				@SuppressLint("NewApi")
				public void run(){
					Bitmap yourSelectedImage=BitmapFactory.decodeFile(activity.getBackgroundFilePath());

					//		Bitmap yourSelectedImageBg =	yourSelectedImage.createBitmap(textViewCountDown.getWidth(), textViewCountDown.getHeight(),yourSelectedImage.getConfig());
					//	yourSelectedImage.setHeight(textViewCountDown.getHeight());
					//	yourSelectedImage.setWidth(textViewCountDown.getWidth());

					Drawable d=new BitmapDrawable(yourSelectedImage);

					textViewCountDown.setBackground(d);
					textViewTime.setBackground(d);
//	textViewCountDown.setBackgroundDrawable(d);
					
				}
			};
			t.run();
			
			
		
			Log.e(TAG, "Setting activity background");
		}
		




	/*	activityCountDown = new CountDownTimer(activity.getDurationMins() * 60 * 1000, 1000){

			@Override
			public void onTick(long p1)
			{
				// TODO: Implement this method
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(p1);
				Date d = cal.getTime();	

			    textViewCountDown.setText(d.getMinutes() + ":" + (d.getSeconds() < 10 ?  "0" + d.getSeconds() : d.getSeconds()));

			}


			@Override
			public void onFinish()
			{
				// TODO: Implement this method
				nextActivityButton = (Button) findViewById(R.id.current_activity_stop_button);

				nextActivityButton.setText(getResources().getString(R.string.postpone));
				Toast.makeText(getApplicationContext(), "Alarm Received", Toast.LENGTH_LONG).show();

				startNotificationAlarm();

				countDownMusic = new CountDownTimer(20 * 1000, 1000){

					@Override
					public void onTick(long p1)
					{
						textViewCountDown.setText(getResources().getString(R.string.dring));
					}

					@Override
					public void onFinish()
					{
						MainActivity.getRoutineFragment().startNextActivity();
						AlarmStatusActivity.this.finish();
						stopVibrator();
						stopAlarm();
					}
				}.start();
			}




		};
        activityCountDown.start(); */

		nextActivityButton = (Button) findViewById(R.id.current_activity_stop_button);
		


		nextActivityButton.setOnClickListener(new OnClickListener(){

				

				@Override
				public void onClick(View p1)
				{
					Log.e(TAG, "nextActivityButton.currentRoutine");
					// TODO: Implement this method
					if (!nextActivityButton.getText().equals(getResources().getString(R.string.postpone)))
					{
						startNextActivity();
					}
					else 
					{
						
						postpone();


					/*	activityCountDown = new CountDownTimer(postponeMinute * 60 * 1000, 1000){

							@Override
							public void onTick(long p1)
							{
								// TODO: Implement this method
								Calendar cal = Calendar.getInstance();
								cal.setTimeInMillis(p1);
								Date d = cal.getTime();
								textViewCountDown.setText(d.getMinutes() + ":" + d.getSeconds());
							}


							@Override
							public void onFinish()
							{
								// TODO: Implement this method

								startNotificationAlarm();
								countDownMusic = new CountDownTimer(20 * 1000, 1000){

									@Override
									public void onTick(long p1)
									{}

									@Override
									public void onFinish()
									{
										MainActivity.getRoutineFragment().startNextActivity();
										AlarmStatusActivity.this.finish();
										stopAlarm();
									}
								}.start();
							}
						};*/
					//	activityCountDown.start();
					}}
			});


        stopRoutineButton = (Button) findViewById(R.id.current_routine_stop_button);
        stopRoutineButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{

				//	activityCountDown.cancel();
					stopService(new Intent(AlarmStatusActivity.this, BroadcastService.class));
				
					stopNotificationAlarm(getApplicationContext());
					
					Intent intent2open = new Intent(AlarmStatusActivity.this, MainActivity.class);
				//	intent2open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//	intent2open.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent2open.putExtra("action","endRoutine");
				//	startActivity(intent2open);
					

					setResult(Activity.RESULT_OK,intent2open);
					finish();
					
				}
			});
		setNextAlarmButton();
			
	}
	
	
	private void postpone(){
		Log.v(TAG, "Postponing activity");
		//	MainActivity.getRoutineFragment().postpone(postponeMinute); 
		stopNotificationAlarm(getApplicationContext());
		setNextAlarmButton();
		startCountDownTimer(Double.valueOf(postponeMinute), false);
	}

	private  void startNotificationAlarm()
	{
		// TODO: Implement this method
		Log.v(TAG, "Starting notification alarm");
		switch (notificationTypeValue)
		{
			case SettingsEnum.NOTIFICATION_TYPE_VALUE_SILENT:
				break;
			case SettingsEnum.NOTIFICATION_TYPE_VALUE_VIBRATE:
				startVibrator();
				break;
			case SettingsEnum.NOTIFICATION_TYPE_VALUE_RINGTONE:
				startAlarm();
				break;
			case SettingsEnum.NOTIFICATION_TYPE_VALUE_VIBRATE_RINGTONE: 
				startVibrator();
				startAlarm();
				break;
		}
		startCountDownTimer(0.5,true);
		
	}

	
	private void stopNotificationAlarm(Context context){
		Log.v(TAG, "Stopping notification alarm");
		context.stopService(broadcast);
		stopAlarm();
		stopVibrator();
	}
	private void setNextAlarmButton()
	{
		if (activityLast)
		{
			nextActivityButton.setText(resources.getString(R.string.done));
			nextActivityButton.setVisibility(View.GONE);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, 1.0f);
			stopRoutineButton.setLayoutParams(param);	
		}
		else
		{
			nextActivityButton.setText(resources.getString(R.string.next_activity));
		}
	} 

	public  void stopAlarm()
	{
		if (mp != null && mp.isPlaying())	{	
			mp.stop();
			mp.release();
			mp = null;
}
	}

	public void startAlarm()
	{
		if (activity.getMusicFilePath() != "" && activity.getMusicFilePath() != null)	{
			mp = new MediaPlayer();
			try	{
				Uri musicUri = Uri.parse(activity.getMusicFilePath());
				mp = MediaPlayer.create(this, musicUri);
				if(activity.getVolume() != 0){
					float volume =( 1.0f * activity.getVolume() )/ 100;
					mp.setVolume(volume,volume);
				}
				}
			
			catch (SecurityException e)
			{}
			catch (IllegalArgumentException e)
			{}
			catch (IllegalStateException e)
			{}
		}
		else
		{
			mp = MediaPlayer.create(this, R.raw.drakesong);
		}
		mp.start();
	}

	private void startVibrator()
	{
		long[] pattern = { 0, 200, 500 };
		vibrator.vibrate(pattern, 0);
	}

	private void stopVibrator()
	{
		if (vibrator != null)
		{
			vibrator.cancel();
		}
	}

	private void startCountDownTimer(Double duration, boolean music)
	{
		
		broadcast.putExtra("duration", duration);
		broadcast.putExtra("music", music);
		startService(broadcast);
		
		Log.e(TAG, "Started service for " + duration +" minutes");
	}

	
	@Override
	public void onBackPressed()
	{
		// Prevent destroying the activity
		this.moveTaskToBack(true);
	}
	
	private void startNextActivity(){
			stopService(broadcast);
			//	if(nextActivityButton.getText().equals(getResources().getString(R.string.next_activity))){
			stopNotificationAlarm(getApplicationContext());
		/*	Intent intent2open = new Intent(AlarmStatusActivity.this, MainActivity.class);
		//	intent2open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//	intent2open.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent2open.putExtra("action","startNextActivity");
			startActivity(intent2open);*/
			
			Intent intent2return = new Intent();
			intent2return.putExtra("action", "startNextActivity");
			setResult(MainActivity.START_NEXT_ACTIVITY_RESULT_CODE, intent2return);
			finish();

	}

	
}
