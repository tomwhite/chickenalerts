package com.tom_e_white.chickenalerts;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// TODO: get state from prefs and populate controls
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void onToggleClicked(View view) {
	    boolean on = ((Switch) view).isChecked();
	    if (on) {
	    	enableAlerts();
	    } else {
	    	disableAlerts();
	    }
	}

	private void enableAlerts() {
		Log.i(getClass().getSimpleName(), "Enabling alerts");
		SharedPreferences prefs = getSharedPreferences("com.tom_e_white.chickenalerts", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("enabled", true);
		editor.commit();
		
		Calendar now = Calendar.getInstance();
		AlertScheduler scheduler = new AlertScheduler();
		Calendar nextAlert = scheduler.scheduleNextAlert(getApplicationContext(), now);
		
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		boolean today = now.get(Calendar.DATE) == nextAlert.get(Calendar.DATE);
		String text = getString(today ? R.string.next_alert_today : R.string.next_alert_tomorrow,
				timeFormat.format(nextAlert.getTime()));
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	private void disableAlerts() {
		Log.i(getClass().getSimpleName(), "Disabling alerts");
		
		SharedPreferences prefs = getSharedPreferences("com.tom_e_white.chickenalerts", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("enabled", false);
		editor.commit();
		
		Intent intent = new Intent(this, AlertReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				this.getApplicationContext(), 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

}
