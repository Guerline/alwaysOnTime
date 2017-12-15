package com.mymorningroutine;
import android.app.*;
import android.content.*;
import android.os.*;
import java.util.*;
import android.os.PowerManager.*;

public class MyBroadcastReceiver extends BroadcastReceiver
{

	private String TAG = "MyBroadcastReceiver";

	

		public MyBroadcastReceiver(){}


	@Override
	public void onReceive(Context context, Intent intent)
	{            
		if (intent.getExtras() != null)
		{
			boolean finished = intent.getBooleanExtra("finished", true);
			
			if(!finished){
				long millisUntilFinished = intent.getLongExtra("countdown", 0);
				Calendar cal = Calendar.getInstance();
				Date current = cal.getTime();
				AlarmStatusActivity.setTimeText(current.getHours() + ":" + (current.getMinutes() < 10 ?  "0" + current.getMinutes() : current.getMinutes()) + ":" + (current.getSeconds() < 10 ?  "0" + current.getSeconds() : current.getSeconds()));
				cal.setTimeInMillis(millisUntilFinished);
				Date d = cal.getTime();	

				AlarmStatusActivity.setCountDownText(d.getMinutes() + ":" + (d.getSeconds() < 10 ?  "0" + d.getSeconds() : d.getSeconds()));
				
				
			} else {
				//boolean finished = intent.getBooleanExtra("finished", true);
				WakeLocker.acquire(context);
				PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
																| PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
				wakeLock.acquire();
				
					KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); 
				 KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
				 keyguardLock.disableKeyguard();
				
				
			Intent intent2open = new Intent(context, AlarmStatusActivity.class);
			intent2open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2open.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent2open.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intent2open.putExtras(intent.getExtras());
			context.startActivity(intent2open);
			
			WakeLocker.release();
			}
			
		}
	}
}
