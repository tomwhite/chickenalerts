package com.tom_e_white.chickenalerts;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlertScheduler {

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