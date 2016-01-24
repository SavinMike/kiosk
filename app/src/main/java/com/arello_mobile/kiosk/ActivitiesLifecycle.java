package com.arello_mobile.kiosk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActivitiesLifecycle implements Application.ActivityLifecycleCallbacks
{
	private static final String TAG = "ActivitiesLifecycle";
	public static final String KEY_ACTION_FOREGROUND = TAG + ".key_action_FOREGROUND";
	public static final String KEY_ACTION_BACKGROUND = TAG + ".key_action_BACKGROUND";

	private final Context mContext;

	private int mActivityCount;
	private Activity mCurrentActivity;

	public ActivitiesLifecycle(Context context)
	{
		mContext = context;
	}

	@Override
	public void onActivityCreated(final Activity activity, final Bundle savedInstanceState)
	{
		if (mCurrentActivity == null)
		{
			onActivitiesStarted();
		}
		mCurrentActivity = activity;
	}

	protected void onActivitiesStarted()
	{

	}

	@Override
	public void onActivityStarted(final Activity activity)
	{
		if (mActivityCount == 0)
		{
			onForeground();
		}
		incrementActivityCount();
	}

	public void onForeground()
	{
		mContext.sendBroadcast(new Intent(KEY_ACTION_FOREGROUND));
	}

	@Override
	public void onActivityResumed(final Activity activity)
	{
		mCurrentActivity = activity;
	}

	@Override
	public void onActivityPaused(final Activity activity)
	{

	}

	@Override
	public void onActivityStopped(final Activity activity)
	{
		decrementActivityCount();
		if (mActivityCount == 0)
		{
			((AppContext)mContext.getApplicationContext()).restoreApp(activity);
			onBackground();
		}
	}

	public void onBackground()
	{
		mContext.sendBroadcast(new Intent(KEY_ACTION_BACKGROUND));
	}

	@Override
	public void onActivitySaveInstanceState(final Activity activity, final Bundle outState)
	{

	}

	@Override
	public void onActivityDestroyed(final Activity activity)
	{
		if (mActivityCount == 0)
		{
			onActivitiesClosed();
		}
	}

	public void incrementActivityCount()
	{
		mActivityCount++;
	}

	public void decrementActivityCount()
	{
		mActivityCount--;
	}

	@SuppressWarnings("unused")
	public boolean isForeground()
	{
		return mActivityCount > 0;
	}

	public void onActivitiesClosed()
	{
		mCurrentActivity = null;
	}

	public Activity getCurrentActivity()
	{
		return mCurrentActivity;
	}
}
