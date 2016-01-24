package com.arello_mobile.kiosk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

public class AppContext extends Application
{
	private static final String TAG = "AppContext";

	private PowerManager.WakeLock wakeLock;
	private OnScreenOffReceiver onScreenOffReceiver;
	private ActivitiesLifecycle mCallback;


	@Override
	public void onCreate()
	{
		super.onCreate();
		registerKioskModeScreenOffReceiver();
		mCallback = new ActivitiesLifecycle(this);
		registerActivityLifecycleCallbacks(mCallback);
	}

	private void registerKioskModeScreenOffReceiver()
	{
		// register screen off receiver
		final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		onScreenOffReceiver = new OnScreenOffReceiver();
		registerReceiver(onScreenOffReceiver, filter);
	}

	public PowerManager.WakeLock getWakeLock()
	{
		if (wakeLock == null)
		{
			// lazy loading: first call, create wakeLock via PowerManager.
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
		}
		return wakeLock;
	}

	public void restoreApp(Activity activity)
	{
		// Restart activity
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
		Log.d(TAG, "restoreApp");
	}
}