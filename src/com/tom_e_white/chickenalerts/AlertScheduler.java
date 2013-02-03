package com.tom_e_white.chickenalerts;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlertScheduler {
	
	public static final String TEST_ALERT = "testAlert";

	public Calendar scheduleNextAlert(Context context) {
		return scheduleNextAlert(context, Calendar.getInstance());
	}
	
	public Calendar scheduleNextAlert(Context context, Calendar cal) {
		Intent intent = new Intent(context, AlertReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, 0, intent, 0);
		AlertTimeCalculator calculator = new AlertTimeCalculator();
		Calendar nextAlert = calculator.calculateNextAlert(cal);
		AlarmManager alarmManager =
				(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		// Don't wake device if asleep - wait until it is next turned on
		alarmManager.set(AlarmManager.RTC, nextAlert.getTimeInMillis(), pendingIntent);
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
