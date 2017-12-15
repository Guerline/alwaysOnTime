package com.mymorningroutine;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import java.util.*;
import com.mymorningroutine.utils.*;

public class BroadcastService extends Service
{

    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.mymorningroutine.countdown_activity";
    Intent bi = new Intent(COUNTDOWN_BR);
	private boolean started = false;
	private boolean music = false;
	public static boolean isRegistered = false;

	private CountDownTimer activityCountDown;

    @Override
	public void onCreate()
	{       
		super.onCreate();
		started = true;
		Log.e(TAG, "Starting timer...");




	}

	@Override
	public void onDestroy()
	{

		activityCountDown.cancel();
		Log.e(TAG, "Timer cancelled");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		
		if (started && intent != null)
		{
			Double duration = intent.getDoubleExtra("duration", 0);
			music = intent.getBooleanExtra("music", false);
			
			Log.v(TAG, "onStartCommand : " + intent.getExtras().toString());
			activityCountDown = new CountDownTimer(Long.valueOf(Math.round(duration * 1000)), 1000){



				@Override
				public void onTick(long millisUntilFinished)
				{
					if (!music)
					{	
						bi.removeExtra("finished");
						bi.putExtra("countdown", millisUntilFinished);
						bi.putExtra("time",System.currentTimeMillis());
						bi.putExtra("finished", false);
						sendBroadcast(bi);
					}

				}


				@Override
				public void onFinish()
				{
					// TODO: Implement this method

			


			
					Calendar cal =  Calendar.getInstance();
					long now = cal.getTimeInMillis();
					/*Intent intent2open = new Intent(getBaseContext(), AlarmStatusActivity.class);
					intent2open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent2open.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent2open.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				//	intent2open.putExtras(intent.getExtras());
					*/
					
					

					bi.putExtra("finished", true);
					bi.putExtra("music", music);
					//	startActivity(bi);
					sendBroadcast(bi);
					
				//	getBaseContext().startActivity(intent2open);

				//	wakeLock.release();
				}




			};
			activityCountDown.start();
		}
		started = false;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0)
	{       
		return null;
	}
}
