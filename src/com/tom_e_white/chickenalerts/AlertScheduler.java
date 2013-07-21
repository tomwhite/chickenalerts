package com.tom_e_white.chickenalerts;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlertScheduler {
	
	public static final String TEST_ALERT = "testAlert";
	public static final String DELAY = "delay";

	public Calendar scheduleNextAlert(Context context, int delay) {
		return scheduleNextAlert(context, delay, Calendar.getInstance());
	}
	
	public Calendar scheduleNextAlert(Context context, int delay, Calendar cal) {
		Intent intent = new Intent(context, AlertReceiver.class);
		intent.putExtra(DELAY, delay);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, 0, intent, 0);
		AlertTimeCalculator calculator = new AlertTimeCalculator();
		Calendar nextAlert = calculator.calculateNextAlert(cal, delay);
		AlarmManager alarmManager =
				(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		// Don't wake device if asleep - wait until it is next turned on
		alarmManager.set(AlarmManager.RTC, nextAlert.getTimeInMillis(), pendingIntent);
		if (BuildConfig.DEBUG)
			Log.i(ChickenConstants.TAG, "Next chicken alert set for " + DateFormat.getTimeInstance(DateFormat.SHORT).format(nextAlert.getTime()));
		return nextAlert;
	}

	public Calendar scheduleTestAlert(Context context) {
		Intent intent = new Intent(context, AlertReceiver.class);
		intent.putExtra(TEST_ALERT, true);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, 0, intent, 0);
		Calendar nextAlert = Calendar.getInstance();
		nextAlert.add(Calendar.SECOND, 2);
		AlarmManager alarmManager =
				(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, nextAlert.getTimeInMillis(), pendingIntent);
		if (BuildConfig.DEBUG)
			Log.i(ChickenConstants.TAG, "Test chicken alert set for " + DateFormat.getTimeInstance(DateFormat.SHORT).format(nextAlert.getTime()));
		return nextAlert;
	}
	
	public void cancelNextAlert(Context context) {
		Intent intent = new Intent(context, AlertReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, 0, intent, 0);
		AlarmManager alarmManager =
				(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}
}
